package com.hybris.tlv.usecase.ship

import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.remote.ShipRemote

internal class ShipInternalGateway(
    private val shipApi: ShipRemote,
    private val shipDao: ShipLocal
): ShipInternalUseCases {

    override suspend fun syncEngines(): SyncResult =
        when (val result = shipApi.getEngines()) {
            is Result.Error -> {
                prepopulateEngines()
                SyncResult.Error(error = result.error)
            }

            is Result.Success -> {
                shipDao.rewriteEngines(engines = result.list)
                SyncResult.Success
            }
        }

    override suspend fun prepopulateEngines() {
        if (shipDao.isEngineEmpty()) {
            val engines: List<Engine> = loadFromJson(path = "files/engines.json")
            shipDao.rewriteEngines(engines = engines)
        }
    }
}
