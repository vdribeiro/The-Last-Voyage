package com.hybris.tlv.usecase.earth

import com.hybris.tlv.http.Result
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.earth.local.EarthLocal
import com.hybris.tlv.usecase.earth.model.Catastrophe
import com.hybris.tlv.usecase.earth.remote.EarthRemote
import com.hybris.tlv.usecase.sync.model.SyncResult

internal class EarthInternalGateway(
    private val earthApi: EarthRemote,
    private val earthDao: EarthLocal
): EarthInternalUseCases {

    override suspend fun syncCatastrophes(): SyncResult =
        when (val result = earthApi.getCatastrophes()) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> earthDao.rewriteCatastrophes(catastrophes = result.list).let { SyncResult.Success }
        }

    override suspend fun prepopulateCatastrophes() {
        if (earthDao.isCatastropheEmpty()) {
            val catastrophes: List<Catastrophe> = loadFromJson(path = "files/catastrophes.json")
            earthDao.rewriteCatastrophes(catastrophes = catastrophes)
        }
    }
}
