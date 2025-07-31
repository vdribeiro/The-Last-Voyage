package com.hybris.tlv.usecase.earth

import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.earth.local.EarthLocal
import com.hybris.tlv.usecase.earth.model.Catastrophe
import com.hybris.tlv.usecase.earth.remote.EarthRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import thelastvoyage.composeapp.generated.resources.Res

internal class EarthGateway(
    private val earthApi: EarthRemote,
    private val earthDao: EarthLocal
): EarthUseCases {

    private suspend fun loadCatastrophesFromJson(): List<Catastrophe> = runCatching {
        val jsonString = Res.readBytes(path = "files/catastrophes.json").decodeToString()
        json.decodeFromString<List<Catastrophe>>(string = jsonString)
    }.getOrDefault(defaultValue = emptyList())

    override suspend fun setup(): Flow<SyncResult> {
        val catastrophes = loadCatastrophesFromJson()
        earthDao.rewriteCatastrophes(catastrophes = catastrophes)
        return earthApi.rewriteCatastrophes(catastrophes = catastrophes)
    }

    override suspend fun syncCatastrophes(): Flow<SyncResult> =
        earthApi.getCatastrophes(queryMap = QueryMap().apply {
            paginate = true
            limit = 1000
        }).map { result ->
            when (result) {
                is Result.Error -> {
                    prepopulateCatastrophes()
                    SyncResult.Error(error = result.error)
                }

                is Result.PartialSuccess -> SyncResult.Loading(
                    progress = result.list.size.toFloat(),
                    total = result.total.toFloat()
                )

                is Result.Success -> {
                    earthDao.rewriteCatastrophes(catastrophes = result.list)
                    SyncResult.Success
                }
            }
        }

    override suspend fun prepopulateCatastrophes() {
        if (earthDao.isCatastropheEmpty()) {
            val catastrophes = loadCatastrophesFromJson()
            earthDao.rewriteCatastrophes(catastrophes = catastrophes)
            true
        }
    }

    override suspend fun getCatastrophes(): List<Catastrophe> =
        earthDao.getCatastrophes()
}
