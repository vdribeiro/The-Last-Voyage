package com.hybris.tlv.usecase.ship

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.EngineSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.ENGINES_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.ENGINES_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import database.AppDatabase
import io.ktor.client.HttpClient
import kotlin.math.abs

internal class ShipGateway(
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    database: AppDatabase
): ShipUseCases {

    private val engineDao = database.engineQueries

    override suspend fun syncEngines() {
        if (config.remoteConfigs.enginesVersion > config.localConfigs.enginesVersion) {
            when (val result = httpClient.getStream<Engine>(path = ENGINES_URL)) {
                is Result.Error -> Telemetry.error(tag = TAG, message = "Unable to get engines", throwable = result.error)
                is Result.Success -> rewriteEngines(engines = result.list)
            }
            config.localConfigs = config.localConfigs.copy(enginesVersion = config.remoteConfigs.enginesVersion)
        }
    }

    override suspend fun prepopulateEngines() {
        if (engineDao.isEngineEmpty().executeAsList().isEmpty()) {
            val engines: List<Engine> = loadFromJsonResource(path = ENGINES_JSON)
            rewriteEngines(engines = engines)
        }
    }

    private fun rewriteEngines(engines: List<Engine>) = engineDao.transaction {
        engineDao.truncateEngine()
        engines.forEach { engineDao.upsertEngine(Engine = it.toEngineSchema()) }
    }

    override suspend fun getEngines(): List<Engine> =
        engineDao.getEngines().executeAsList().map { it.toEngine() }.sortedBy { it.velocity }

    override suspend fun repairShip(ship: Ship): Ship {
        var integrity = ship.integrity
        var materials = ship.materials
        val fuel = if (ship.fuel < 0) 0 else ship.fuel
        val cryopods = if (ship.cryopods < 0) 0 else ship.cryopods

        if (integrity <= 0) {
            // Attempt to repair the ship
            val repairAmount = abs(n = integrity) + 1
            if (materials >= repairAmount) {
                integrity = 1
                materials -= repairAmount
            } else {
                integrity = 0
                materials = 0
            }
        }

        if (materials < 0) {
            // Equalize loss
            val materialDeficit = abs(n = materials)
            integrity = if (integrity > materialDeficit) integrity - materialDeficit else 0
            materials = 0
        }

        return ship.copy(
            integrity = integrity,
            materials = materials,
            fuel = fuel,
            cryopods = cryopods
        )
    }

    private fun Engine.toEngineSchema(): EngineSchema =
        EngineSchema(
            id = id,
            description = description,
            velocity = velocity,
        )

    private fun EngineSchema.toEngine(): Engine =
        Engine(
            id = id,
            description = description,
            velocity = velocity,
        )

    companion object Companion {
        private const val TAG = "Ship"
    }
}
