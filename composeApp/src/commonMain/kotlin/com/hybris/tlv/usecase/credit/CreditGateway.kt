package com.hybris.tlv.usecase.credit

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.CreditSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.CREDITS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.credit.model.Credit
import database.AppDatabase
import io.ktor.client.HttpClient

internal class CreditGateway(
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    database: AppDatabase
): CreditUseCases {

    private val creditDao = database.creditQueries

    override suspend fun syncCredits() {
        if (config.remoteConfigs.creditsVersion > config.localConfigs.creditsVersion) {
            when (val result = httpClient.getStream<Credit>(path = CREDITS_URL)) {
                is Result.Error -> Logger.error(tag = TAG, message = result.error)
                is Result.Success -> rewriteCredits(credits = result.list)
            }
            config.localConfigs = config.localConfigs.copy(creditsVersion = config.remoteConfigs.creditsVersion)
        }
    }

    override suspend fun prepopulateCredits() {
        if (creditDao.isCreditEmpty().executeAsList().isEmpty()) {
            val credits: List<Credit> = loadFromJson(path = "files/credits.json")
            rewriteCredits(credits = credits)
        }
    }

    private fun rewriteCredits(credits: List<Credit>) = creditDao.transaction {
        creditDao.truncateCredit()
        credits.forEach { creditDao.upsertCredit(Credit = it.toCreditSchema()) }
    }

    override suspend fun getCredits(): List<Credit> =
        creditDao.getCredits().executeAsList().map { it.toCredit() }

    private fun Credit.toCreditSchema(): CreditSchema =
        CreditSchema(
            id = id,
            link = link,
            type = type
        )

    private fun CreditSchema.toCredit(): Credit =
        Credit(
            id = id,
            link = link,
            type = type
        )

    companion object Companion {
        private const val TAG = "Credit"
    }
}
