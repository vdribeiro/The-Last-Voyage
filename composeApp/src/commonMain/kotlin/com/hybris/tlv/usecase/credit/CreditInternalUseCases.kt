package com.hybris.tlv.usecase.credit

import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.sync.model.SyncResult

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
