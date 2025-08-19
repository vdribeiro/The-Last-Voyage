package com.hybris.tlv.usecase.credit

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.credit.model.Credit
import kotlinx.coroutines.flow.Flow

internal interface CreditInternalUseCases {

    /**
     * Rewrites the local and remote [Credit] data.
     */
    suspend fun rewrite(): Flow<SyncResult>

    /**
     * Syncs the remote [Credit] data to local.
     */
    suspend fun syncCredits(): Flow<SyncResult>

    /**
     * Prepopulate local [Credit].
     */
    suspend fun prepopulateCredits()
}
