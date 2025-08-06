package com.hybris.tlv.usecase.ship.remote

import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.ship.model.Engine
import kotlinx.coroutines.flow.Flow

internal interface ShipRemote {

    /**
     * Rewrite [engines] in the API.
     */
    suspend fun rewriteEngines(engines: List<Engine>): Flow<SyncResult>

    /**
     * Get engines from the API given the [queryMap].
     */
    suspend fun getEngines(queryMap: QueryMap = QueryMap()): Flow<Result<Engine>>
}
