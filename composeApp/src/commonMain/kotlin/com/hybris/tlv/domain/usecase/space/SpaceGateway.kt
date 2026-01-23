package com.hybris.tlv.domain.usecase.space

import kotlin.math.ceil
import kotlin.math.sqrt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.serializer.loadFromJsonResource
import com.hybris.tlv.domain.usecase.space.model.Planet
import com.hybris.tlv.domain.usecase.space.model.StellarHost
import com.hybris.tlv.domain.usecase.space.model.TravelOutcome
import com.hybris.tlv.infrastructure.resource.JsonResource
import database.AppDatabase

internal class SpaceGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): SpaceUseCases {

    private val stellarHostDao = database.stellarHostQueries
    private val planetDao = database.planetQueries

    override suspend fun syncStellarHosts(): Boolean = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.get<StellarHost>(path = URL.StellarHosts)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get stellar hosts", throwable = result.error)
                false
            }

            is Result.Success -> {
                rewriteStellarHosts(stellarHosts = result.list)
                Telemetry.info(tag = TAG, message = "Successful stellar hosts sync")
                true
            }
        }
    }

    override suspend fun prepopulateStellarHosts(): Boolean = withContext(context = Dispatcher.IO) {
        if (stellarHostDao.isStellarHostEmpty().awaitAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating stellar hosts")
            val stellarHosts: List<StellarHost> = loadFromJsonResource(json = JsonResource.StellarHosts)
            rewriteStellarHosts(stellarHosts = stellarHosts)
            true
        } else false
    }

    private suspend fun rewriteStellarHosts(stellarHosts: List<StellarHost>) = stellarHostDao.transactionWithResult {
        stellarHostDao.truncateStellarHost()
        stellarHosts.forEach { stellarHostDao.upsertStellarHost(StellarHost = it.toStellarHostSchema()) }
    }

    override suspend fun syncPlanets(): Boolean = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.get<Planet>(path = URL.Planets)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get planets", throwable = result.error)
                false
            }

            is Result.Success -> {
                rewritePlanets(planets = result.list)
                Telemetry.info(tag = TAG, message = "Successful planets sync")
                true
            }
        }

    }

    override suspend fun prepopulatePlanets(): Boolean = withContext(context = Dispatcher.IO) {
        if (planetDao.isPlanetEmpty().awaitAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating planets")
            val planets: List<Planet> = loadFromJsonResource(json = JsonResource.Planets)
            rewritePlanets(planets = planets)
            true
        } else false
    }

    private suspend fun rewritePlanets(planets: List<Planet>) = planetDao.transactionWithResult {
        planetDao.truncatePlanet()
        planets.forEach { planetDao.upsertPlanet(Planet = it.toPlanetSchema()) }
    }

    override suspend fun getStellarHost(id: String): StellarHost? = withContext(context = Dispatcher.IO) {
        val planets = planetDao.getPlanetsByStellarHost(stellarHostId = id).awaitAsList().map { it.toPlanet() }
        stellarHostDao.getStellarHost(id = id).awaitAsOneOrNull()?.toStellarHost()?.apply { this.planets.addAll(elements = planets) }
    }

    override fun observeExoplanets(): Flow<List<StellarHost>> {
        val planetsFlow = planetDao.getPlanets()
            .asFlow()
            .mapToList(context = Dispatcher.IO)
        val stellarHostsFlow = stellarHostDao.getStellarHosts()
            .asFlow()
            .mapToList(context = Dispatcher.IO)
        return combine(flow = stellarHostsFlow, flow2 = planetsFlow) { stellarHosts, planets ->
            stellarHosts.map { it.toStellarHost() }.addPlanets(planets = planets.map { it.toPlanet() })
        }.flowOn(context = Dispatcher.Default)
    }

    override suspend fun getNearestStars(
        stellarHost: StellarHost,
        n: Int,
        visited: Set<String>
    ): List<StellarHost> = withContext(context = Dispatcher.Default) {
        if (n <= 0) return@withContext emptyList()
        val stellarHostCP = stellarHost.toCartesian() ?: return@withContext emptyList()
        val nearest = mutableListOf<Pair<StellarHost, Double>>()
        stellarHostDao.getStellarHosts().awaitAsList().map { it.toStellarHost() }
            .addPlanets(planets = planetDao.getPlanets().awaitAsList().map { it.toPlanet() })
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

    companion object Companion {
        private const val TAG = "Space"
    }
}
