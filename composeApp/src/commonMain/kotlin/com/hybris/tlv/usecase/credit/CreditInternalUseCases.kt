package com.hybris.tlv.usecase.credit

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.credit.model.Credit

internal interface CreditInternalUseCases {

    /**
     * Syncs the remote [Credit] data to local.
     */
    suspend fun syncCredits(): SyncResult

    /**
     * Prepopulate local [Credit].
     */
    suspend fun prepopulateCredits()
}
