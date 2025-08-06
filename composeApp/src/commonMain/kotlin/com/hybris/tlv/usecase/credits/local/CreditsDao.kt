package com.hybris.tlv.usecase.credits.local

import com.hybris.tlv.usecase.credits.mapper.toCredits
import com.hybris.tlv.usecase.credits.mapper.toCreditsSchema
import com.hybris.tlv.usecase.credits.model.Credits
import database.AppDatabase

internal class CreditsDao(
    database: AppDatabase
): CreditsLocal {

    private val creditsDao = database.creditsQueries

    override fun isCreditsEmpty(): Boolean =
        creditsDao.isCreditsEmpty().executeAsList().isEmpty()

    override fun rewriteCredits(credits: List<Credits>) = creditsDao.transaction {
        creditsDao.truncateCredits()
        credits.forEach { creditsDao.upsertCredits(Credits = it.toCreditsSchema()) }
    }

    override fun getCredits(): List<Credits> =
        creditsDao.getCredits().executeAsList().map { it.toCredits() }
}
