package com.hybris.tlv.usecase.catastrophe

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.HttpClientFactory.Companion.CATASTROPHES_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.CATASTROPHES_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import database.AppDatabase

internal class CatastropheGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): CatastropheUseCases {

    private val catastropheDao = database.catastropheQueries

    override suspend fun syncCatastrophes(): Boolean = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.getStream<Catastrophe>(path = CATASTROPHES_URL)) {
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
        if (catastropheDao.isCatastropheEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating catastrophes")
            val catastrophes: List<Catastrophe> = loadFromJsonResource(path = CATASTROPHES_JSON)
            rewriteCatastrophes(catastrophes = catastrophes)
            true
        } else false
    }

    private fun rewriteCatastrophes(catastrophes: List<Catastrophe>) = catastropheDao.transaction {
        catastropheDao.truncateCatastrophe()
        catastrophes.forEach { catastropheDao.upsertCatastrophe(Catastrophe = it.toCatastropheSchema()) }
    }

    override suspend fun getRandomCatastrophe(): Catastrophe? = withContext(context = Dispatcher.IO) {
        catastropheDao.getRandomCatastrophe().executeAsOneOrNull()?.toCatastrophe()
    }

    companion object {
        private const val TAG = "Catastrophe"
    }
}
