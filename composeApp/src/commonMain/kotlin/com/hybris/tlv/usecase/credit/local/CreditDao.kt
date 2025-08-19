package com.hybris.tlv.usecase.credit.local

import com.hybris.tlv.usecase.credit.mapper.toCredit
import com.hybris.tlv.usecase.credit.mapper.toCreditSchema
import com.hybris.tlv.usecase.credit.model.Credit
import database.AppDatabase

internal class CreditDao(
    database: AppDatabase
): CreditLocal {

    private val creditDao = database.creditQueries

    override fun isCreditEmpty(): Boolean =
        creditDao.isCreditEmpty().executeAsList().isEmpty()

    override fun rewriteCredits(credits: List<Credit>) = creditDao.transaction {
        creditDao.truncateCredit()
        credits.forEach { creditDao.upsertCredit(Credit = it.toCreditSchema()) }
    }

    override fun getCredits(): List<Credit> =
        creditDao.getCredits().executeAsList().map { it.toCredit() }
}
