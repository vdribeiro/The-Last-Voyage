package com.hybris.tlv.domain.usecase.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.resource.JsonResource
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.database.asFlow
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.serializer.loadFromJsonResource
import com.hybris.tlv.domain.usecase.event.model.Event
import database.AppDatabase

internal class EventGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): EventUseCases {

    private val eventDao = database.eventQueries

    override suspend fun syncEvents(): Boolean = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.get<Event>(path = URL.Events)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get events", throwable = result.error)
                false
            }

            is Result.Success -> {
                rewriteEvents(events = result.list)
                Telemetry.info(tag = TAG, message = "Successful events sync")
                true
            }
        }
    }

    override suspend fun prepopulateEvents(): Boolean = withContext(context = Dispatcher.IO) {
        if (eventDao.isEventEmpty().awaitAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating events")
            val events: List<Event> = loadFromJsonResource(json = JsonResource.Events)
            rewriteEvents(events = events)
            true
        } else false
    }

    private suspend fun rewriteEvents(events: List<Event>) = eventDao.transactionWithResult {
        eventDao.truncateEvent()
        events.forEach { eventDao.upsertEvent(Event = it.toEventSchema()) }
    }

    override suspend fun getRandomEvent(ids: Set<String>): List<Event> = withContext(context = Dispatcher.IO) {
        val event = eventDao.getRandomEvent(ids = ids).awaitAsOneOrNull()?.toEvent() ?: return@withContext emptyList()
        val treeNodes = mutableListOf(event)
        val nodesToVisit = mutableListOf(event.id)
        while (nodesToVisit.isNotEmpty()) {
            val currentParentId = nodesToVisit.removeFirstOrNull() ?: continue
            val children = eventDao.getChildEvents(parentId = currentParentId).awaitAsList().map { it.toEvent() }
            if (children.isNotEmpty()) {
                treeNodes.addAll(elements = children)
                nodesToVisit.addAll(elements = children.map { it.id })
            }
        }
        treeNodes
    }

    override fun observeEvents(): Flow<List<Event>> =
        eventDao.getEvents().asFlow { it.toEvent() }

    companion object Companion {
        private const val TAG = "Event"
    }
}
