package com.hybris.tlv.usecase.ship

import kotlin.math.abs
import io.ktor.client.HttpClient
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
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_CRYOPODS
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_FUEL
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_INTEGRITY
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_MATERIALS
import database.AppDatabase

internal class ShipGateway(
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    database: AppDatabase
): ShipUseCases {

    private val engineDao = database.engineQueries

    override suspend fun syncEngines() {
        val remoteVersion = config.remoteConfigs.value.enginesVersion
        val localVersion = config.localConfigs.value.enginesVersion
        Telemetry.info(tag = TAG, message = "Syncing engines: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            when (val result = httpClient.getStream<Engine>(path = ENGINES_URL)) {
                is Result.Error -> Telemetry.error(tag = TAG, message = "Unable to get engines", throwable = result.error)
                is Result.Success -> {
                    rewriteEngines(engines = result.list)
                    config.setConfigs { it.copy(enginesVersion = remoteVersion) }
                    Telemetry.info(tag = TAG, message = "Successful engines sync")
                    return
                }
            }
        }
        if (engineDao.isEngineEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating engines")
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
        val preferences = config.preferences.value

        val sensorRange = if (preferences.cheatSensorRange) 100 else ship.sensorRange
        var integrity = if (preferences.cheatIntegrity) MAX_INTEGRITY else ship.integrity
        var materials = if (preferences.cheatMaterials) MAX_MATERIALS else ship.materials
        val fuel = if (preferences.cheatFuel) MAX_FUEL else ship.fuel.coerceAtLeast(minimumValue = 0)
        val cryopods = if (preferences.cheatCryopods) MAX_CRYOPODS else ship.cryopods.coerceAtLeast(minimumValue = 0)

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
            sensorRange = sensorRange,
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
            fuelConsumption = fuelConsumption,
            cost = cost
        )

    private fun EngineSchema.toEngine(): Engine =
        Engine(
            id = id,
            description = description,
            velocity = velocity,
            fuelConsumption = fuelConsumption,
            cost = cost
        )

    companion object Companion {
        private const val TAG = "Ship"
    }
}
