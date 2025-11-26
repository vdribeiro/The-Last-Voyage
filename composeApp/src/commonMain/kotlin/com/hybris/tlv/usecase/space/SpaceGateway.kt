package com.hybris.tlv.usecase.space

import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.PlanetSchema
import com.hybris.tlv.database.StellarHostSchema
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.HttpClientFactory.Companion.PLANETS_URL
import com.hybris.tlv.http.HttpClientFactory.Companion.STELLAR_HOSTS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.PLANETS_JSON
import com.hybris.tlv.serializer.STELLAR_HOSTS_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.space.model.CartesianPoint
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.model.TravelOutcome
import database.AppDatabase

internal class SpaceGateway(
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    database: AppDatabase
): SpaceUseCases {

    private val stellarHostDao = database.stellarHostQueries
    private val planetDao = database.planetQueries

    override suspend fun syncStellarHosts() = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.value.stellarHostsVersion
        val localVersion = config.localConfigs.value.stellarHostsVersion
        Telemetry.info(tag = TAG, message = "Syncing stellar hosts: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            when (val result = httpClient.getStream<StellarHost>(path = STELLAR_HOSTS_URL)) {
                is Result.Error -> Telemetry.error(tag = TAG, message = "Unable to get stellar hosts", throwable = result.error)
                is Result.Success -> {
                    rewriteStellarHosts(stellarHosts = result.list)
                    config.setConfigs { it.copy(stellarHostsVersion = remoteVersion) }
                    Telemetry.info(tag = TAG, message = "Successful stellar hosts sync")
                    return@withContext
                }
            }
        }
        if (stellarHostDao.isStellarHostEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating stellar hosts")
            val stellarHosts: List<StellarHost> = loadFromJsonResource(path = STELLAR_HOSTS_JSON)
            rewriteStellarHosts(stellarHosts = stellarHosts)
        }
    }

    private fun rewriteStellarHosts(stellarHosts: List<StellarHost>) = stellarHostDao.transaction {
        stellarHostDao.truncateStellarHost()
        stellarHosts.forEach { stellarHostDao.upsertStellarHost(StellarHost = it.toStellarHostSchema()) }
    }

    override suspend fun syncPlanets() = withContext(context = Dispatcher.IO) {
        val remoteVersion = config.remoteConfigs.value.planetsVersion
        val localVersion = config.localConfigs.value.planetsVersion
        Telemetry.info(tag = TAG, message = "Syncing planets: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            when (val result = httpClient.getStream<Planet>(path = PLANETS_URL)) {
                is Result.Error -> Telemetry.error(tag = TAG, message = "Unable to get planets", throwable = result.error)
                is Result.Success -> {
                    rewritePlanets(planets = result.list)
                    config.setConfigs { it.copy(planetsVersion = remoteVersion) }
                    Telemetry.info(tag = TAG, message = "Successful planets sync")
                    return@withContext
                }
            }
        }
        if (planetDao.isPlanetEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating planets")
            val planets: List<Planet> = loadFromJsonResource(path = PLANETS_JSON)
            rewritePlanets(planets = planets)
        }
    }

    private fun rewritePlanets(planets: List<Planet>) = planetDao.transaction {
        planetDao.truncatePlanet()
        planets.forEach { planetDao.upsertPlanet(Planet = it.toPlanetSchema()) }
    }

    override suspend fun getStellarHost(id: String): StellarHost? = withContext(context = Dispatcher.IO) {
        val planets = planetDao.getPlanetsByStellarHost(stellarHostId = id).executeAsList().map { it.toPlanet() }
        stellarHostDao.getStellarHost(id = id).executeAsOneOrNull()?.toStellarHost()?.apply { this.planets.addAll(elements = planets) }
    }

    override suspend fun getExoplanets(): List<StellarHost> = withContext(context = Dispatcher.IO) {
        val planetMap = planetDao.getPlanets().executeAsList().map { it.toPlanet() }.groupBy { it.stellarHostId }
        stellarHostDao.getStellarHosts().executeAsList().map { it.toStellarHost() }.apply {
            forEach { it.planets.addAll(elements = planetMap[it.id].orEmpty()) }
        }
    }

    override suspend fun getNearestStars(
        stellarHost: StellarHost,
        n: Int,
        visited: Set<String>
    ): List<StellarHost> = withContext(context = Dispatcher.Default) {
        if (n <= 0) return@withContext emptyList()
        val stellarHostCP = stellarHost.toCartesian() ?: return@withContext emptyList()
        val nearest = mutableListOf<Pair<StellarHost, Double>>()
        getExoplanets()
            .asSequence()
            .filter { it.id != stellarHost.id && it.id !in visited && it.planets.isNotEmpty() }
            .forEach { otherStellarHost ->
                val otherStellarHostCP = otherStellarHost.toCartesian() ?: return@forEach
                val distanceSquared = stellarHostCP.distanceSquaredBetween(cp = otherStellarHostCP)
                when {
                    nearest.size < n -> {
                        nearest.add(otherStellarHost to distanceSquared)
                        nearest.sortBy { it.second }
                    }

                    else -> {
                        val farthestDistanceSquared = nearest.last().second
                        if (distanceSquared < farthestDistanceSquared) {
                            nearest[n - 1] = otherStellarHost to distanceSquared
                            nearest.sortBy { it.second }
                        }
                    }
                }
            }
        nearest.map { (stellarHost, distanceSquared) ->
            val finalDistance = sqrt(x = distanceSquared)
            stellarHost.copy(distance = finalDistance).apply {
                planets.addAll(elements = stellarHost.planets)
                travelOutcome = TravelOutcome(fuel = ceil(x = finalDistance).toInt())
            }
        }
    }

    private fun StellarHost.toCartesian(): CartesianPoint? {
        if (ra == null || dec == null || distance == null) return null
        val raRad = ra * PI / 180.0
        val decRad = dec * PI / 180.0
        return CartesianPoint(
            x = distance * cos(x = decRad) * cos(x = raRad),
            y = distance * cos(x = decRad) * sin(x = raRad),
            z = distance * sin(x = decRad)
        )
    }

    private fun StellarHost.toStellarHostSchema(): StellarHostSchema =
        StellarHostSchema(
            id = id,
            name = name,
            systemName = systemName,
            spectralType = spectralType,
            effectiveTemperature = effectiveTemperature,
            radius = radius,
            mass = mass,
            metallicity = metallicity,
            luminosity = luminosity,
            gravity = gravity,
            age = age,
            density = density,
            rotationalVelocity = rotationalVelocity,
            rotationalPeriod = rotationalPeriod,
            distance = distance,
            ra = ra,
            dec = dec
        )

    private fun StellarHostSchema.toStellarHost(): StellarHost =
        StellarHost(
            id = id,
            name = name,
            systemName = systemName,
            spectralType = spectralType,
            effectiveTemperature = effectiveTemperature,
            radius = radius,
            mass = mass,
            metallicity = metallicity,
            luminosity = luminosity,
            gravity = gravity,
            age = age,
            density = density,
            rotationalVelocity = rotationalVelocity,
            rotationalPeriod = rotationalPeriod,
            distance = distance,
            ra = ra,
            dec = dec
        )

    private fun Planet.toPlanetSchema(): PlanetSchema =
        PlanetSchema(
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

    private fun PlanetSchema.toPlanet(): Planet =
        Planet(
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

    companion object Companion {
        private const val TAG = "Space"
    }
}
