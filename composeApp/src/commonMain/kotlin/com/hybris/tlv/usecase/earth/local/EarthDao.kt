package com.hybris.tlv.usecase.earth.local

import com.hybris.tlv.usecase.earth.mapper.toCatastrophe
import com.hybris.tlv.usecase.earth.mapper.toCatastropheSchema
import com.hybris.tlv.usecase.earth.model.Catastrophe
import database.AppDatabase

internal class EarthDao(
    database: AppDatabase
): EarthLocal {

    private val catastropheDao = database.catastropheQueries

    override fun isCatastropheEmpty(): Boolean =
        catastropheDao.isCatastropheEmpty().executeAsList().isEmpty()

    override fun rewriteCatastrophes(catastrophes: List<Catastrophe>) = catastropheDao.transaction {
        catastropheDao.truncateCatastrophe()
        catastrophes.forEach { catastropheDao.upsertCatastrophe(it.toCatastropheSchema()) }
    }

    override fun getCatastrophes(): List<Catastrophe> =
        catastropheDao.getCatastrophes().executeAsList().map { it.toCatastrophe() }
}
