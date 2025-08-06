package com.hybris.tlv.usecase.ship.local

import com.hybris.tlv.usecase.ship.mapper.toEngine
import com.hybris.tlv.usecase.ship.mapper.toEngineSchema
import com.hybris.tlv.usecase.ship.model.Engine
import database.AppDatabase

internal class ShipDao(
    database: AppDatabase
): ShipLocal {

    private val engineDao = database.engineQueries

    override fun isEngineEmpty(): Boolean =
        engineDao.isEngineEmpty().executeAsList().isEmpty()

    override fun rewriteEngines(engines: List<Engine>) = engineDao.transaction {
        engineDao.truncateEngine()
        engines.forEach { engineDao.upsertEngine(Engine = it.toEngineSchema()) }
    }

    override fun getEngines(): List<Engine> =
        engineDao.getEngines().executeAsList().map { it.toEngine() }
}
