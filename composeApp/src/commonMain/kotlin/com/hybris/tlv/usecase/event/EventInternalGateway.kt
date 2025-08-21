package com.hybris.tlv.usecase.event

import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.event.local.EventLocal
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.event.remote.EventRemote

internal class EventInternalGateway(
    private val eventApi: EventRemote,
    private val eventDao: EventLocal
): EventInternalUseCases {

    override suspend fun syncEvents(): SyncResult =
        when (val result = eventApi.getEvents()) {
            is Result.Error -> {
                prepopulateEvents()
                SyncResult.Error(error = result.error)
            }

            is Result.Success -> {
                eventDao.rewriteEvents(events = result.list)
                SyncResult.Success
            }
        }

    override suspend fun prepopulateEvents() {
        if (eventDao.isEventEmpty()) {
            val events: List<Event> = loadFromJson(path = "files/events.json")
            eventDao.rewriteEvents(events = events)
        }
    }
}
