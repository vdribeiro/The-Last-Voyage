package com.hybris.tlv.usecase.credit

import com.hybris.tlv.usecase.credit.local.CreditLocal
import com.hybris.tlv.usecase.credit.model.Credit

internal class CreditGateway(
    private val creditDao: CreditLocal
): CreditUseCases {

    override suspend fun getCredits(): List<Credit> =
        creditDao.getCredits()
}
