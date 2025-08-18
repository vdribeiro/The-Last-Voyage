package com.hybris.tlv.usecase.credits

import com.hybris.tlv.usecase.credits.model.Credits

internal interface CreditsUseCases {

    /**
     * Get [Credits] from the database.
     */
    suspend fun getCredits(): List<Credits>
}
