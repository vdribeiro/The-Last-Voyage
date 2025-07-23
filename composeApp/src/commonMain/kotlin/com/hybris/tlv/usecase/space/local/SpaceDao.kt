package com.hybris.tlv.usecase.space.local

import com.hybris.tlv.usecase.space.mapper.toPlanet
import com.hybris.tlv.usecase.space.mapper.toPlanetSchema
import com.hybris.tlv.usecase.space.mapper.toStellarHost
import com.hybris.tlv.usecase.space.mapper.toStellarHostSchema
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import database.AppDatabase

internal class SpaceDao(
    database: AppDatabase
): SpaceLocal {

    private val stellarHostDao = database.stellarHostQueries
    private val planetDao = database.planetQueries

    override fun isStellarHostEmpty(): Boolean =
        stellarHostDao.isStellarHostEmpty().executeAsList().isEmpty()

    override fun isPlanetEmpty(): Boolean =
        planetDao.isPlanetEmpty().executeAsList().isEmpty()

    override fun rewriteStellarHosts(stellarHosts: List<StellarHost>) = stellarHostDao.transaction {
        stellarHostDao.truncateStellarHost()
        stellarHosts.forEach { stellarHostDao.upsertStellarHost(StellarHost = it.toStellarHostSchema()) }
    }

    override fun rewritePlanets(planets: List<Planet>) = planetDao.transaction {
        planetDao.truncatePlanet()
        planets.forEach { planetDao.upsertPlanet(Planet = it.toPlanetSchema()) }
    }

    override fun getStellarHosts(): List<StellarHost> =
        stellarHostDao.getStellarHosts().executeAsList().map { it.toStellarHost() }

    override fun getPlanets(): List<Planet> =
        planetDao.getPlanets().executeAsList().map { it.toPlanet() }
}
