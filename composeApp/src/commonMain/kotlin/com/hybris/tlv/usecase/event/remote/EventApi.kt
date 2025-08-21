package com.hybris.tlv.usecase.event.remote

import com.hybris.tlv.http.EVENTS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.usecase.event.model.Event
import io.ktor.client.HttpClient

internal class EventApi(
    private val httpClient: HttpClient
): EventRemote {

    override suspend fun getEvents(): Result<Event> =
        httpClient.getStream(url = EVENTS_URL)
}
