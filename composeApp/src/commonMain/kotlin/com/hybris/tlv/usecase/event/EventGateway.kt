package com.hybris.tlv.usecase.event

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.serializer.loadFromJsonResource
import com.hybris.tlv.resource.JsonResource
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.event.model.Event
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
        if (eventDao.isEventEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating events")
            val events: List<Event> = loadFromJsonResource(json = JsonResource.Events)
            rewriteEvents(events = events)
            true
        } else false
    }

    private fun rewriteEvents(events: List<Event>) = eventDao.transaction {
        eventDao.truncateEvent()
        events.forEach { eventDao.upsertEvent(Event = it.toEventSchema()) }
    }

    override suspend fun getRandomEvent(ids: Set<String>): List<Event> = withContext(context = Dispatcher.IO) {
        val event = eventDao.getRandomEvent(ids = ids).executeAsOneOrNull()?.toEvent() ?: return@withContext emptyList()
        val treeNodes = mutableListOf(event)
        val nodesToVisit = mutableListOf(event.id)
        while (nodesToVisit.isNotEmpty()) {
            val currentParentId = nodesToVisit.removeFirstOrNull() ?: continue
            val children = eventDao.getChildEvents(parentId = currentParentId).executeAsList().map { it.toEvent() }
            if (children.isNotEmpty()) {
                treeNodes.addAll(elements = children)
                nodesToVisit.addAll(elements = children.map { it.id })
            }
        }
        treeNodes
    }

    companion object Companion {
        private const val TAG = "Event"
    }
}
