package com.hybris.tlv.usecase.catastrophe

import io.ktor.client.HttpClient
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.CatastropheSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.CATASTROPHES_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.CATASTROPHES_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import database.AppDatabase

internal class CatastropheGateway(
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    database: AppDatabase
): CatastropheUseCases {

    private val catastropheDao = database.catastropheQueries

    override suspend fun syncCatastrophes() {
        val remoteVersion = config.remoteConfigs.catastrophesVersion
        val localVersion = config.localConfigs.catastrophesVersion
        Telemetry.info(tag = TAG, message = "Syncing catastrophes: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            when (val result = httpClient.getStream<Catastrophe>(path = CATASTROPHES_URL)) {
                is Result.Error -> Telemetry.error(tag = TAG, message = "Unable to get catastrophes", throwable = result.error)
                is Result.Success -> {
                    rewriteCatastrophes(catastrophes = result.list)
                    config.setConfigs { it.copy(catastrophesVersion = remoteVersion) }
                    Telemetry.info(tag = TAG, message = "Successful catastrophes sync")
                    return
                }
            }
        }
        if (catastropheDao.isCatastropheEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating catastrophes")
            val catastrophes: List<Catastrophe> = loadFromJsonResource(path = CATASTROPHES_JSON)
            rewriteCatastrophes(catastrophes = catastrophes)
        }
    }

    private fun rewriteCatastrophes(catastrophes: List<Catastrophe>) = catastropheDao.transaction {
        catastropheDao.truncateCatastrophe()
        catastrophes.forEach { catastropheDao.upsertCatastrophe(Catastrophe = it.toCatastropheSchema()) }
    }

    override suspend fun getRandomCatastrophe(): Catastrophe? =
        catastropheDao.getRandomCatastrophe().executeAsOneOrNull()?.toCatastrophe()

    private fun Catastrophe.toCatastropheSchema(): CatastropheSchema =
        CatastropheSchema(
            id = id,
            description = description,
        )

    private fun CatastropheSchema.toCatastrophe(): Catastrophe =
        Catastrophe(
            id = id,
            description = description,
        )

    companion object {
        private const val TAG = "Catastrophe"
    }
}
