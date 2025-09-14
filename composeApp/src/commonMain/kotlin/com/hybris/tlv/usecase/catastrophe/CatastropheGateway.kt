package com.hybris.tlv.usecase.catastrophe

import com.hybris.tlv.database.CatastropheSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.CATASTROPHES_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import database.AppDatabase
import io.ktor.client.HttpClient

internal class CatastropheGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): CatastropheUseCases {

    private val catastropheDao = database.catastropheQueries

    override suspend fun syncCatastrophes(): Result<Catastrophe> =
        httpClient.getStream<Catastrophe>(path = CATASTROPHES_URL)

    override suspend fun prepopulateCatastrophes() {
        if (catastropheDao.isCatastropheEmpty().executeAsList().isEmpty()) {
            val catastrophes: List<Catastrophe> = loadFromJson(path = "files/catastrophes.json")
            rewriteCatastrophes(catastrophes = catastrophes)
        }
    }

    private fun rewriteCatastrophes(catastrophes: List<Catastrophe>) = catastropheDao.transaction {
        catastropheDao.truncateCatastrophe()
        catastrophes.forEach { catastropheDao.upsertCatastrophe(Catastrophe = it.toCatastropheSchema()) }
    }

    override suspend fun getRandomCatastrophe(): Catastrophe? =
        catastropheDao.getRandomCatastrophe().executeAsOneOrNull()?.toCatastrophe()

    private fun Catastrophe.toCatastropheSchema(): CatastropheSchema =
        CatastropheSchema(
            id = id,
            description = description,
        )

    private fun CatastropheSchema.toCatastrophe(): Catastrophe =
        Catastrophe(
            id = id,
            description = description,
        )
}
