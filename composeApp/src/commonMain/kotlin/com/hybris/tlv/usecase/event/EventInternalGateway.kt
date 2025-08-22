package com.hybris.tlv.usecase.event

import com.hybris.tlv.http.Result
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.event.local.EventLocal
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.event.remote.EventRemote
import com.hybris.tlv.usecase.sync.model.SyncResult

internal class EventInternalGateway(
    private val eventApi: EventRemote,
    private val eventDao: EventLocal
): EventInternalUseCases {

    override suspend fun syncEvents(): SyncResult =
        when (val result = eventApi.getEvents()) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> eventDao.rewriteEvents(events = result.list).let { SyncResult.Success }
        }

    override suspend fun prepopulateEvents() {
        if (eventDao.isEventEmpty()) {
            val events: List<Event> = loadFromJson(path = "files/events.json")
            eventDao.rewriteEvents(events = events)
        }
    }
}
