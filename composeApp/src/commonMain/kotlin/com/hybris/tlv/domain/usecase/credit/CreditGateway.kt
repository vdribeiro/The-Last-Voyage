package com.hybris.tlv.domain.usecase.credit

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.resource.JsonResource
import com.hybris.tlv.data.resource.loadResource
import com.hybris.tlv.domain.usecase.credit.model.Credit
import database.AppDatabase

internal class CreditGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): CreditUseCases {

    private val creditDao = database.creditQueries

    override suspend fun syncCredits() = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.get<Credit>(path = URL.Credits)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get credits", throwable = result.error)
                false
            }

            is Result.Success -> {
                rewriteCredits(credits = result.list)
                Telemetry.info(tag = TAG, message = "Successful credits sync")
                true
            }
        }
    }

    override suspend fun prepopulateCredits(): Boolean = withContext(context = Dispatcher.IO) {
        if (creditDao.isCreditEmpty().awaitAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating credits")
            val credits: List<Credit> = loadResource(json = JsonResource.Credits)
            rewriteCredits(credits = credits)
            true
        } else false
    }

    private suspend fun rewriteCredits(credits: List<Credit>) = creditDao.transactionWithResult {
        creditDao.truncateCredit()
        credits.forEach { creditDao.upsertCredit(Credit = it.toCreditSchema()) }
    }

    override suspend fun getCredits(): List<Credit> = withContext(context = Dispatcher.IO) {
        creditDao.getCredits().awaitAsList().map { it.toCredit() }
    }

    companion object Companion {
        private const val TAG = "Credit"
    }
}
