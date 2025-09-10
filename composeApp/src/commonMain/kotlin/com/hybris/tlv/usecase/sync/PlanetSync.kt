package com.hybris.tlv.usecase.sync

import com.hybris.tlv.database.PlanetSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.PLANETS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.sync.model.SyncResult
import database.AppDatabase
import io.ktor.client.HttpClient

internal class PlanetSync(
    private val httpClient: HttpClient,
    database: AppDatabase
) {

    private val planetDao = database.planetQueries

    suspend fun syncPlanets(): SyncResult =
        when (val result = httpClient.getStream<Planet>(path = PLANETS_URL)) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> rewritePlanets(planets = result.list).let { SyncResult.Success }
        }

    suspend fun prepopulatePlanets() {
        if (planetDao.isPlanetEmpty().executeAsList().isEmpty()) {
            val planets: List<Planet> = loadFromJson(path = "files/planets.json")
            rewritePlanets(planets = planets)
        }
    }

    private fun rewritePlanets(planets: List<Planet>) = planetDao.transaction {
        planetDao.truncatePlanet()
        planets.forEach { planetDao.upsertPlanet(Planet = it.toPlanetSchema()) }
    }

    private fun Planet.toPlanetSchema(): PlanetSchema =
        com.hybris.tlv.database.PlanetSchema(
            id = id,
            name = name,
            stellarHostId = stellarHostId,
            status = status,
            orbitalPeriod = orbitalPeriod,
            orbitAxis = orbitAxis,
            radius = radius,
            mass = mass,
            density = density,
            eccentricity = eccentricity,
            insolationFlux = insolationFlux,
            equilibriumTemperature = equilibriumTemperature,
            occultationDepth = occultationDepth,
            inclination = inclination,
            obliquity = obliquity,
        )
}