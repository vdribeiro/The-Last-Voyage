package com.hybris.tlv.domain.usecase.ship

import kotlin.math.abs
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.data.resource.JsonResource
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.serializer.loadFromJsonResource
import com.hybris.tlv.domain.usecase.ship.model.Engine
import com.hybris.tlv.domain.usecase.ship.model.Ship
import database.AppDatabase

internal class ShipGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): ShipUseCases {

    private val engineDao = database.engineQueries

    override suspend fun syncEngines(): Boolean = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.get<Engine>(path = URL.Engines)) {
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
        if (engineDao.isEngineEmpty().awaitAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating engines")
            val engines: List<Engine> = loadFromJsonResource(json = JsonResource.Engines)
            rewriteEngines(engines = engines)
            true
        } else false
    }

    private suspend fun rewriteEngines(engines: List<Engine>) = engineDao.transactionWithResult {
        engineDao.truncateEngine()
        engines.forEach { engineDao.upsertEngine(Engine = it.toEngineSchema()) }
    }

    override suspend fun getEngines(): List<Engine> = withContext(context = Dispatcher.IO) {
        engineDao.getEngines().awaitAsList().map { it.toEngine() }.sortedBy { it.velocity }
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
