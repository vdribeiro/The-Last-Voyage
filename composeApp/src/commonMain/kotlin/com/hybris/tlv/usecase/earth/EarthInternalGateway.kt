package com.hybris.tlv.usecase.earth

import com.hybris.tlv.http.QueryMap
import com.hybris.tlv.http.Result
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.earth.local.EarthLocal
import com.hybris.tlv.usecase.earth.model.Catastrophe
import com.hybris.tlv.usecase.earth.remote.EarthRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class EarthInternalGateway(
    private val earthApi: EarthRemote,
    private val earthDao: EarthLocal
): EarthInternalUseCases {

    override suspend fun rewriteCatastrophes(): Flow<SyncResult> {
        val catastrophes: List<Catastrophe> = loadFromJson(path = "files/catastrophes.json")
        earthDao.rewriteCatastrophes(catastrophes = catastrophes)
        return earthApi.rewriteCatastrophes(catastrophes = catastrophes)
    }

    override suspend fun syncCatastrophes(): Flow<SyncResult> =
        earthApi.getCatastrophes(queryMap = QueryMap().apply {
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
            val catastrophes: List<Catastrophe> = loadFromJson(path = "files/catastrophes.json")
            earthDao.rewriteCatastrophes(catastrophes = catastrophes)
            true
        }
    }
}
