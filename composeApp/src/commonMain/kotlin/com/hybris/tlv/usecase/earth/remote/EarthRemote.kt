package com.hybris.tlv.usecase.earth.remote

import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.earth.model.Catastrophe
import kotlinx.coroutines.flow.Flow

internal interface EarthRemote {

    /**
     * Rewrite [catastrophes] in the API.
     */
    suspend fun rewriteCatastrophes(catastrophes: List<Catastrophe>): Flow<SyncResult>

    /**
     * Get catastrophes from the API given the [queryMap].
     */
    suspend fun getCatastrophes(queryMap: QueryMap = QueryMap()): Flow<Result<Catastrophe>>
}
