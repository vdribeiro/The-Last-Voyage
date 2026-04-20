package com.hybris.tlv.domain.usecase.credit

import com.hybris.tlv.domain.credit.Credit

internal interface CreditUseCases {

    /**
     * Sync [Credit]s.
     */
    suspend fun syncCredits(): Boolean

    /**
     * Prepopulate [Credit]s.
     */
    suspend fun prepopulateCredits(): Boolean

    /**
     * Get all [Credit]s.
     */
    suspend fun getCredits(): List<Credit>
}
