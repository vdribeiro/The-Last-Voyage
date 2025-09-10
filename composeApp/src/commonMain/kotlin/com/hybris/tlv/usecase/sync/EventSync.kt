package com.hybris.tlv.usecase.sync

import com.hybris.tlv.database.EventSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.EVENTS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.json
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.sync.model.SyncResult
import database.AppDatabase
import io.ktor.client.HttpClient

internal class EventSync(
    private val httpClient: HttpClient,
    database: AppDatabase
) {

    private val eventDao = database.eventQueries

    suspend fun syncEvents(): SyncResult =
        when (val result = httpClient.getStream<Event>(path = EVENTS_URL)) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> rewriteEvents(events = result.list).let { SyncResult.Success }
        }

    suspend fun prepopulateEvents() {
        if (eventDao.isEventEmpty().executeAsList().isEmpty()) {
            val events: List<Event> = loadFromJson(path = "files/events.json")
            rewriteEvents(events = events)
        }
    }

    private fun rewriteEvents(events: List<Event>) = eventDao.transaction {
        eventDao.truncateEvent()
        events.forEach { eventDao.upsertEvent(Event = it.toEventSchema()) }
    }

    private fun Event.toEventSchema(): EventSchema =
        com.hybris.tlv.database.EventSchema(
            id = id,
            description = description,
            parentId = parentId,
            outcome = outcome?.let { json.encodeToString(value = it) }
        )
}