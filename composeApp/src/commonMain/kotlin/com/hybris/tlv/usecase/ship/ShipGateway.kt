package com.hybris.tlv.usecase.ship

import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.remote.ShipRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import thelastvoyage.composeapp.generated.resources.Res

internal class ShipGateway(
    private val shipApi: ShipRemote,
    private val shipDao: ShipLocal
): ShipUseCases {

    private suspend fun loadEnginesFromJson(): List<Engine> = runCatching {
        val jsonString = Res.readBytes(path = "files/engines.json").decodeToString()
        json.decodeFromString<List<Engine>>(string = jsonString)
    }.getOrDefault(defaultValue = emptyList())

    override suspend fun setup(): Flow<SyncResult> {
        val engines = loadEnginesFromJson()
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
            val engines = loadEnginesFromJson()
            shipDao.rewriteEngines(engines = engines)
        }
    }

    override suspend fun getEngines(): List<Engine> =
        shipDao.getEngines().sortedByDescending { it.velocity }
}
