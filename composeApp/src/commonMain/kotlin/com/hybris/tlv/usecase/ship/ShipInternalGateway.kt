package com.hybris.tlv.usecase.ship

import com.hybris.tlv.http.Result
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.ship.remote.ShipRemote
import com.hybris.tlv.usecase.sync.model.SyncResult

internal class ShipInternalGateway(
    private val shipApi: ShipRemote,
    private val shipDao: ShipLocal
): ShipInternalUseCases {

    override suspend fun syncEngines(): SyncResult =
        when (val result = shipApi.getEngines()) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> shipDao.rewriteEngines(engines = result.list).let { SyncResult.Success }
        }

    override suspend fun prepopulateEngines() {
        if (shipDao.isEngineEmpty()) {
            val engines: List<Engine> = loadFromJson(path = "files/engines.json")
            shipDao.rewriteEngines(engines = engines)
        }
    }

    override suspend fun upsertShip(ship: Ship) =
        shipDao.upsertShip(ship = ship)
}
