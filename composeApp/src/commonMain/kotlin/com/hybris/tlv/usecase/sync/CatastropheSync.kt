package com.hybris.tlv.usecase.sync

import com.hybris.tlv.database.CatastropheSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.CATASTROPHES_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.sync.model.SyncResult
import database.AppDatabase
import io.ktor.client.HttpClient

internal class CatastropheSync(
    private val httpClient: HttpClient,
    database: AppDatabase
) {

    private val catastropheDao = database.catastropheQueries

    suspend fun syncCatastrophes(): SyncResult =
        when (val result = httpClient.getStream<Catastrophe>(path = CATASTROPHES_URL)) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> rewriteCatastrophes(catastrophes = result.list).let { SyncResult.Success }
        }

    suspend fun prepopulateCatastrophes() {
        if (catastropheDao.isCatastropheEmpty().executeAsList().isEmpty()) {
            val catastrophes: List<Catastrophe> = loadFromJson(path = "files/catastrophes.json")
            rewriteCatastrophes(catastrophes = catastrophes)
        }
    }

    private fun rewriteCatastrophes(catastrophes: List<Catastrophe>) = catastropheDao.transaction {
        catastropheDao.truncateCatastrophe()
        catastrophes.forEach { catastropheDao.upsertCatastrophe(Catastrophe = it.toCatastropheSchema()) }
    }

    private fun Catastrophe.toCatastropheSchema(): CatastropheSchema =
        com.hybris.tlv.database.CatastropheSchema(
            id = id,
            description = description,
        )
}