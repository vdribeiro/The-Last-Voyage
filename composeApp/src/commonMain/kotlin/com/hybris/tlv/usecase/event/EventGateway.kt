package com.hybris.tlv.usecase.event

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.EventSchema
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.HttpClientFactory.Companion.EVENTS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.EVENTS_JSON
import com.hybris.tlv.serializer.decode
import com.hybris.tlv.serializer.encode
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.space.model.TravelOutcome
import database.AppDatabase

internal class EventGateway(
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    database: AppDatabase
): EventUseCases {

    private val eventDao = database.eventQueries

    override suspend fun syncEvents() = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.value.eventsVersion
        val localVersion = config.localConfigs.value.eventsVersion
        Telemetry.info(tag = TAG, message = "Syncing events: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            when (val result = httpClient.getStream<Event>(path = EVENTS_URL)) {
                is Result.Error -> Telemetry.error(tag = TAG, message = "Unable to get events", throwable = result.error)
                is Result.Success -> {
                    rewriteEvents(events = result.list)
                    config.setConfigs { it.copy(eventsVersion = remoteVersion) }
                    Telemetry.info(tag = TAG, message = "Successful events sync")
                    return@withContext
                }
            }
        }
        if (eventDao.isEventEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating events")
            val events: List<Event> = loadFromJsonResource(path = EVENTS_JSON)
            rewriteEvents(events = events)
        }
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

    private fun Event.toEventSchema(): EventSchema =
        EventSchema(
            id = id,
            description = description,
            parentId = parentId,
            outcome = encode(value = outcome)
        )

    private fun EventSchema.toEvent(): Event =
        Event(
            id = id,
            description = description,
            parentId = parentId,
            outcome = decode<TravelOutcome>(value = outcome)
        )

    companion object Companion {
        private const val TAG = "Event"
    }
}
