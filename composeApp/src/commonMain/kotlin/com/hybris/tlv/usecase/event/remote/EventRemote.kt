package com.hybris.tlv.usecase.event.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.usecase.event.model.Event

internal interface EventRemote {

    /**
     * Get events from the API.
     */
    suspend fun getEvents(): Result<Event>
}
