package com.hybris.tlv.usecase.ship

import kotlin.math.abs
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.HttpClientFactory.Companion.ENGINES_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.ENGINES_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import database.AppDatabase

internal class ShipGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): ShipUseCases {

    private val engineDao = database.engineQueries

    override suspend fun syncEngines(): Boolean = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.getStream<Engine>(path = ENGINES_URL)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get engines", throwable = result.error)
                false
            }

            is Result.Success -> {
                rewriteEngines(engines = result.list)
                Telemetry.info(tag = TAG, message = "Successful engines sync")
                true
            }
        }
    }

    override suspend fun prepopulateEngines(): Boolean = withContext(context = Dispatcher.IO) {
        if (engineDao.isEngineEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating engines")
            val engines: List<Engine> = loadFromJsonResource(path = ENGINES_JSON)
            rewriteEngines(engines = engines)
            true
        } else false
    }

    private fun rewriteEngines(engines: List<Engine>) = engineDao.transaction {
        engineDao.truncateEngine()
        engines.forEach { engineDao.upsertEngine(Engine = it.toEngineSchema()) }
    }

    override suspend fun getEngines(): List<Engine> = withContext(context = Dispatcher.IO) {
        engineDao.getEngines().executeAsList().map { it.toEngine() }.sortedBy { it.velocity }
    }

    override suspend fun repairShip(ship: Ship): Ship = withContext(context = Dispatcher.Default) {
        val sensorRange = ship.sensorRange
        var integrity = ship.integrity
        var materials = ship.materials
        val fuel = ship.fuel.coerceAtLeast(minimumValue = 0)
        val cryopods = ship.cryopods.coerceAtLeast(minimumValue = 0)

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

        ship.copy(
            sensorRange = sensorRange,
            integrity = integrity,
            materials = materials,
            fuel = fuel,
            cryopods = cryopods
        )
    }

    companion object Companion {
        private const val TAG = "Ship"
    }
}
