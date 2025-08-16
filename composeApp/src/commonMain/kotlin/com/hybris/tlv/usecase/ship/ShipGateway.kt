package com.hybris.tlv.usecase.ship

import com.hybris.tlv.http.json.loadFromJson
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.remote.ShipRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ShipGateway(
    private val shipApi: ShipRemote,
    private val shipDao: ShipLocal
): ShipUseCases {

    override suspend fun rewrite(): Flow<SyncResult> {
        val engines: List<Engine> = loadFromJson(path = "files/engines.json")
        shipDao.rewriteEngines(engines = engines)
        return shipApi.rewriteEngines(engines = engines)
    }

    override suspend fun syncEngines(): Flow<SyncResult> =
        shipApi.getEngines(queryMap = QueryMap().apply {
            paginate = true
            limit = 1000
        }).map { result ->
            when (result) {
                is Result.Error -> {
                    prepopulateEngines()
                    SyncResult.Error(error = result.error)
                }

                is Result.PartialSuccess -> SyncResult.Loading(
                    progress = result.list.size.toFloat(),
                    total = result.total.toFloat()
                )

                is Result.Success -> {
                    shipDao.rewriteEngines(engines = result.list)
                    SyncResult.Success
                }
            }
        }

    override suspend fun prepopulateEngines() {
        if (shipDao.isEngineEmpty()) {
            val engines: List<Engine> = loadFromJson(path = "files/engines.json")
            shipDao.rewriteEngines(engines = engines)
        }
    }

    override suspend fun getEngines(): List<Engine> =
        shipDao.getEngines().sortedByDescending { it.velocity }
}
