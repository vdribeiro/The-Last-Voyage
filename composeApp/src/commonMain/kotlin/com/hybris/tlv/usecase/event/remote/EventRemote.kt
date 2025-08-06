package com.hybris.tlv.usecase.event.remote

import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.event.model.Event
import kotlinx.coroutines.flow.Flow

internal interface EventRemote {

    /**
     * Rewrite [events] in the API.
     */
    suspend fun rewriteEvents(events: List<Event>): Flow<SyncResult>

    /**
     * Get events from the API given the [queryMap].
     */
    suspend fun getEvents(queryMap: QueryMap = QueryMap()): Flow<Result<Event>>
}
