package com.hybris.tlv.usecase.translation.remote

import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.translation.model.domain.Translation
import kotlinx.coroutines.flow.Flow

internal interface TranslationRemote {

    /**
     * Rewrite [translations] in the API.
     */
    suspend fun rewriteTranslations(translations: List<Translation>): Flow<SyncResult>

    /**
     * Get translations from the API given the [queryMap].
     */
    suspend fun getTranslations(queryMap: QueryMap = QueryMap()): Flow<Result<Translation>>
}
