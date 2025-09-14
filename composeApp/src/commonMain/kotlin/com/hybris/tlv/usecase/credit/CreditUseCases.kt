package com.hybris.tlv.usecase.credit

import com.hybris.tlv.usecase.credit.model.Credit

internal interface CreditUseCases {

    /**
     * Sync [Credit]s.
     */
    suspend fun syncCredits()

    /**
     * Prepopulate [Credit]s.
     */
    suspend fun prepopulateCredits()

    /**
     * Get all [Credit]s.
     */
    suspend fun getCredits(): List<Credit>
}
