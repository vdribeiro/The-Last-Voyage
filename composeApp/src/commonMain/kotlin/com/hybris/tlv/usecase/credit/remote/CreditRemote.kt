package com.hybris.tlv.usecase.credit.remote

import com.hybris.tlv.http.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.credit.model.Credit
import kotlinx.coroutines.flow.Flow

internal interface CreditRemote {

    /**
     * Rewrite [credits] in the API.
     */
    suspend fun rewriteCredits(credits: List<Credit>): Flow<SyncResult>

    /**
     * Get credits from the API given the [queryMap].
     */
    suspend fun getCredits(queryMap: QueryMap = QueryMap()): Flow<Result<Credit>>
}
