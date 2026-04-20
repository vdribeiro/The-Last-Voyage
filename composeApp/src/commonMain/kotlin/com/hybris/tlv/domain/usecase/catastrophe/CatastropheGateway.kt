package com.hybris.tlv.domain.usecase.catastrophe

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.resource.JsonResource
import com.hybris.tlv.data.resource.loadResource
import com.hybris.tlv.domain.catastrophe.Catastrophe
import database.AppDatabase

internal class CatastropheGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): CatastropheUseCases {

    private val catastropheDao = database.catastropheQueries

    override suspend fun syncCatastrophes(): Boolean = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.get<Catastrophe>(path = URL.Catastrophes)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get catastrophes", throwable = result.error)
                false
            }

            is Result.Success -> {
                rewriteCatastrophes(catastrophes = result.list)
                Telemetry.info(tag = TAG, message = "Successful catastrophes sync")
                true
            }
        }
    }

    override suspend fun prepopulateCatastrophes(): Boolean = withContext(context = Dispatcher.IO) {
        if (catastropheDao.isCatastropheEmpty().awaitAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating catastrophes")
            val catastrophes: List<Catastrophe> = loadResource(json = JsonResource.Catastrophes)
            rewriteCatastrophes(catastrophes = catastrophes)
            true
        } else false
    }

    private suspend fun rewriteCatastrophes(catastrophes: List<Catastrophe>) = catastropheDao.transactionWithResult {
        catastropheDao.truncateCatastrophe()
        catastrophes.forEach { catastropheDao.upsertCatastrophe(Catastrophe = it.toCatastropheSchema()) }
    }

    override suspend fun getRandomCatastrophe(): Catastrophe? = withContext(context = Dispatcher.IO) {
        catastropheDao.getRandomCatastrophe().awaitAsOneOrNull()?.toCatastrophe()
    }

    override suspend fun getCatastrophes(): List<Catastrophe> = withContext(context = Dispatcher.IO) {
        catastropheDao.getCatastrophes().awaitAsList().map { it.toCatastrophe() }
    }

    companion object {
        private const val TAG = "Catastrophe"
    }
}
