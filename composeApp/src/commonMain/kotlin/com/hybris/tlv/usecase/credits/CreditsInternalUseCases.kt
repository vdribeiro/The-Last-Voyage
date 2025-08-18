package com.hybris.tlv.usecase.credits

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.credits.model.Credits
import kotlinx.coroutines.flow.Flow

internal interface CreditsInternalUseCases {

    /**
     * Rewrites the local and remote [Credits] data.
     */
    suspend fun rewrite(): Flow<SyncResult>

    /**
     * Syncs the remote [Credits] data to local.
     */
    suspend fun syncCredits(): Flow<SyncResult>

    /**
     * Prepopulate local [Credits].
     */
    suspend fun prepopulateCredits()
}
