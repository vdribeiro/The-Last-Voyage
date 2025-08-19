package com.hybris.tlv.usecase.credit

import com.hybris.tlv.usecase.credit.model.Credit

internal interface CreditUseCases {

    /**
     * Get [Credit]s from the database.
     */
    suspend fun getCredits(): List<Credit>
}
