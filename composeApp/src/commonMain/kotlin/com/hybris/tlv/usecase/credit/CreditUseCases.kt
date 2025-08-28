package com.hybris.tlv.usecase.credit

import com.hybris.tlv.usecase.credit.model.Credit

internal interface CreditUseCases {

    /**
     * Get all [Credit]s.
     */
    suspend fun getCredits(): List<Credit>
}
