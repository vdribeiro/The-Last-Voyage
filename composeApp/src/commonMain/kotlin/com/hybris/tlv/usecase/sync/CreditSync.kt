package com.hybris.tlv.usecase.sync

import com.hybris.tlv.database.CreditSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.CREDITS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.sync.model.SyncResult
import database.AppDatabase
import io.ktor.client.HttpClient

internal class CreditSync(
    private val httpClient: HttpClient,
    database: AppDatabase
) {

    private val creditDao = database.creditQueries

    suspend fun syncCredits(): SyncResult =
        when (val result = httpClient.getStream<Credit>(path = CREDITS_URL)) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> rewriteCredits(credits = result.list).let { SyncResult.Success }
        }

    suspend fun prepopulateCredits() {
        if (creditDao.isCreditEmpty().executeAsList().isEmpty()) {
            val credits: List<Credit> = loadFromJson(path = "files/credits.json")
            rewriteCredits(credits = credits)
        }
    }

    private fun rewriteCredits(credits: List<Credit>) = creditDao.transaction {
        creditDao.truncateCredit()
        credits.forEach { creditDao.upsertCredit(Credit = it.toCreditSchema()) }
    }

    private fun Credit.toCreditSchema(): CreditSchema =
        com.hybris.tlv.database.CreditSchema(
            id = id,
            link = link,
            type = type
        )
}