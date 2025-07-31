package com.hybris.tlv.usecase.credits.remote

import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.credits.model.Credits
import kotlinx.coroutines.flow.Flow

internal interface CreditsRemote {

    /**
     * Rewrite [credits] in the API.
     */
    suspend fun rewriteCredits(credits: List<Credits>): Flow<SyncResult>

    /**
     * Get credits from the API given the [queryMap].
     */
    suspend fun getCredits(queryMap: QueryMap = QueryMap()): Flow<Result<Credits>>
}
