package com.hybris.tlv.usecase.credits

import com.hybris.tlv.usecase.credits.local.CreditsLocal
import com.hybris.tlv.usecase.credits.model.Credits

internal class CreditsGateway(
    private val creditsDao: CreditsLocal
): CreditsUseCases {

    override suspend fun getCredits(): List<Credits> =
        creditsDao.getCredits()
}
