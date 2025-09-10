package com.hybris.tlv.usecase.sync

import com.hybris.tlv.database.EngineSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.ENGINES_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.sync.model.SyncResult
import database.AppDatabase
import io.ktor.client.HttpClient

internal class ShipSync(
    private val httpClient: HttpClient,
    database: AppDatabase
) {

    private val engineDao = database.engineQueries

    suspend fun syncEngines(): SyncResult =
        when (val result = httpClient.getStream<Engine>(path = ENGINES_URL)) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> rewriteEngines(engines = result.list).let { SyncResult.Success }
        }

    suspend fun prepopulateEngines() {
        if (engineDao.isEngineEmpty().executeAsList().isEmpty()) {
            val engines: List<Engine> = loadFromJson(path = "files/engines.json")
            rewriteEngines(engines = engines)
        }
    }

    private fun rewriteEngines(engines: List<Engine>) = engineDao.transaction {
        engineDao.truncateEngine()
        engines.forEach { engineDao.upsertEngine(Engine = it.toEngineSchema()) }
    }

    private fun Engine.toEngineSchema(): EngineSchema =
        com.hybris.tlv.database.EngineSchema(
            id = id,
            description = description,
            velocity = velocity,
        )
}