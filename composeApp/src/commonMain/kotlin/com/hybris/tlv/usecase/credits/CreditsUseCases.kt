package com.hybris.tlv.usecase.credits

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.credits.model.Credits
import kotlinx.coroutines.flow.Flow

internal interface CreditsUseCases {

    /**
     * Rewrites the local and remote [Credits] data.
     */
    suspend fun setup(): Flow<SyncResult>

    /**
     * Syncs the remote [Credits] data to local.
     */
    suspend fun syncCredits(): Flow<SyncResult>

    /**
     * Prepopulate local [Credits].
     */
    suspend fun prepopulateCredits()

    /**
     * Get [Credits] from the database.
     */
    suspend fun getCredits(): List<Credits>
}
