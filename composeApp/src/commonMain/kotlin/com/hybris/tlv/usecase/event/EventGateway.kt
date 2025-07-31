package com.hybris.tlv.usecase.event

import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.event.local.EventLocal
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.event.remote.EventRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import thelastvoyage.composeapp.generated.resources.Res

internal class EventGateway(
    private val eventApi: EventRemote,
    private val eventDao: EventLocal
): EventUseCases {

    private suspend fun loadEventsFromJson(): List<Event> = runCatching {
        val jsonString = Res.readBytes(path = "files/events.json").decodeToString()
        json.decodeFromString<List<Event>>(string = jsonString)
    }.getOrDefault(defaultValue = emptyList())

    override suspend fun setup(): Flow<SyncResult> {
        val events = loadEventsFromJson()
        eventDao.rewriteEvents(events = events)
        return eventApi.rewriteEvents(events = events)
    }

    override suspend fun syncEvents(): Flow<SyncResult> =
        eventApi.getEvents(queryMap = QueryMap().apply {
            paginate = true
            limit = 1000
        }).map { result ->
            when (result) {
                is Result.Error -> {
                    prepopulateEvents()
                    SyncResult.Error(error = result.error)
                }

                is Result.PartialSuccess -> SyncResult.Loading(
                    progress = result.list.size.toFloat(),
                    total = result.total.toFloat()
                )

                is Result.Success -> {
                    eventDao.rewriteEvents(events = result.list)
                    SyncResult.Success
                }
            }
        }

    override suspend fun prepopulateEvents() {
        if (eventDao.isEventEmpty()) {
            val events = loadEventsFromJson()
            eventDao.rewriteEvents(events = events)
        }
    }

    override suspend fun getEvents(): List<Event> =
        eventDao.getEvents()

    override suspend fun getRandomEvent(ids: Set<String>): List<Event> =
        eventDao.getRandomEvent(ids = ids)
}
