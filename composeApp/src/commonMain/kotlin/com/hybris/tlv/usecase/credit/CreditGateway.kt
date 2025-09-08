package com.hybris.tlv.usecase.credit

import com.hybris.tlv.database.CreditSchema
import com.hybris.tlv.usecase.credit.model.Credit
import database.AppDatabase

internal class CreditGateway(
    database: AppDatabase
): CreditUseCases {

    private val creditDao = database.creditQueries

    override suspend fun getCredits(): List<Credit> =
        creditDao.getCredits().executeAsList().map { it.toCredit() }

    private fun CreditSchema.toCredit(): Credit =
        Credit(
            id = id,
            link = link,
            type = type
        )
}
