package com.hybris.tlv.usecase.catastrophe

import com.hybris.tlv.database.CatastropheSchema
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import database.AppDatabase

internal class CatastropheGateway(
    database: AppDatabase
): CatastropheUseCases {

    private val catastropheDao = database.catastropheQueries

    override suspend fun getRandomCatastrophe(): Catastrophe? =
        catastropheDao.getRandomCatastrophe().executeAsOneOrNull()?.toCatastrophe()

    private fun CatastropheSchema.toCatastrophe(): Catastrophe =
        Catastrophe(
            id = id,
            description = description,
        )
}
