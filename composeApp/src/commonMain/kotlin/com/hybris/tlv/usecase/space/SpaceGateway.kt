package com.hybris.tlv.usecase.space

import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.logger.benchmark
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.combine
import com.hybris.tlv.usecase.space.local.SpaceLocal
import com.hybris.tlv.usecase.space.mapper.mergePlanets
import com.hybris.tlv.usecase.space.mapper.mergeStellarHosts
import com.hybris.tlv.usecase.space.mapper.toCartesian
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.model.TravelOutcome
import com.hybris.tlv.usecase.space.remote.SpaceRemote
import com.hybris.tlv.usecase.space.remote.result.ExoplanetsResult
import kotlin.math.ceil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import thelastvoyage.composeapp.generated.resources.Res

internal class SpaceGateway(
    private val spaceApi: SpaceRemote,
    private val spaceDao: SpaceLocal,
): SpaceUseCases {

    private suspend fun loadHostsFromJson(): List<StellarHost> = runCatching {
        val jsonString = Res.readBytes(path = "files/hosts.json").decodeToString()
        json.decodeFromString<List<StellarHost>>(string = jsonString)
    }.getOrDefault(defaultValue = emptyList())

    private suspend fun loadPlanetsFromJson(): List<Planet> = runCatching {
        val jsonString = Res.readBytes(path = "files/planets.json").decodeToString()
        json.decodeFromString<List<Planet>>(string = jsonString)
    }.getOrDefault(defaultValue = emptyList())

    override suspend fun setup(): Flow<SyncResult> {
        val result = when (val result = getArchive()) {
            is ExoplanetsResult.Error -> return flowOf(value = SyncResult.Error(error = result.error))
            is ExoplanetsResult.Success -> result
        }

        spaceDao.rewriteStellarHosts(stellarHosts = result.stellarHosts)
        spaceDao.rewritePlanets(planets = result.planets)

        val stellarHostsFlow = spaceApi.rewriteStellarHosts(stellarHosts = result.stellarHosts)
        val planetsFlow = spaceApi.rewritePlanets(planets = result.planets)
        return combine(flows = listOf(stellarHostsFlow, planetsFlow)) { it.combine() }
    }

    private suspend fun getArchive(): ExoplanetsResult {
        val stellarHosts = loadHostsFromJson().toMutableList()
        val planets = loadPlanetsFromJson().toMutableList()

        benchmark(message = "stellarHostsArchiveTime") {
            when (val stellarHostsArchiveResult = getArchive { spaceApi.getStellarHostsArchive(queryMap = it) }) {
                is ExoplanetsResult.Error -> return stellarHostsArchiveResult
                is ExoplanetsResult.Success -> {
                    stellarHosts.addAll(elements = stellarHostsArchiveResult.stellarHosts)
                }
            }
        }

        benchmark(message = "exoplanetsArchiveTime") {
            when (val exoplanetsArchiveResult = getArchive { spaceApi.getExoplanetsArchive(queryMap = it) }) {
                is ExoplanetsResult.Error -> return exoplanetsArchiveResult
                is ExoplanetsResult.Success -> {
                    val stellarHostIds = stellarHosts.map { it.id }
                    val filteredStellarHosts = exoplanetsArchiveResult.stellarHosts.filter { it.id !in stellarHostIds }
                    stellarHosts.addAll(elements = filteredStellarHosts)

                    planets.addAll(elements = exoplanetsArchiveResult.planets)
                }
            }
        }

        benchmark(message = "k2ExoplanetsArchiveTime") {
            when (val k2ExoplanetsArchiveResult = getArchive { spaceApi.getK2ExoplanetsArchive(queryMap = it) }) {
                is ExoplanetsResult.Error -> return k2ExoplanetsArchiveResult
                is ExoplanetsResult.Success -> {
                    val stellarHostIds = stellarHosts.map { it.id }
                    val filteredStellarHosts = k2ExoplanetsArchiveResult.stellarHosts.filter { it.id !in stellarHostIds }
                    stellarHosts.addAll(elements = filteredStellarHosts)

                    val planetIds = planets.map { it.id }
                    val filteredPlanets = k2ExoplanetsArchiveResult.planets.filter { it.id !in planetIds }
                    planets.addAll(elements = filteredPlanets)
                }
            }
        }

        return ExoplanetsResult.Success(stellarHosts = stellarHosts.mergeStellarHosts(), planets = planets.mergePlanets())
    }

    private suspend fun getArchive(apiCall: suspend (QueryMap) -> ExoplanetsResult): ExoplanetsResult {
        val stellarHosts = mutableListOf<StellarHost>()
        val planets = mutableListOf<Planet>()
        val queryMap = QueryMap().apply {
            this.limit = 1000
            this.offset = 0
        }
        do {
            val hasMore = when (val result = apiCall(queryMap)) {
                is ExoplanetsResult.Success -> {
                    stellarHosts.addAll(elements = result.stellarHosts)
                    planets.addAll(elements = result.planets)
                    queryMap.nextPage()
                    result.stellarHosts.size >= queryMap.limit!! || result.planets.size >= queryMap.limit!!
                }

                is ExoplanetsResult.Error -> return result
            }
        } while (hasMore)
        return ExoplanetsResult.Success(stellarHosts = stellarHosts, planets = planets)
    }

    override suspend fun syncStellarHosts(): Flow<SyncResult> =
        spaceApi.getStellarHosts(queryMap = QueryMap().apply {
            paginate = true
            limit = 1000
        }).map { result ->
            when (result) {
                is Result.Error -> {
                    prepopulateStellarHosts()
                    SyncResult.Error(error = result.error)
                }

                is Result.PartialSuccess -> SyncResult.Loading(
                    progress = result.list.size.toFloat(),
                    total = result.total.toFloat()
                )

                is Result.Success -> {
                    spaceDao.rewriteStellarHosts(stellarHosts = result.list)
                    SyncResult.Success
                }
            }
        }

    override suspend fun syncPlanets(): Flow<SyncResult> =
        spaceApi.getPlanets(queryMap = QueryMap().apply {
            paginate = true
            limit = 1000
        }).map { result ->
            when (result) {
                is Result.Error -> {
                    prepopulatePlanets()
                    SyncResult.Error(error = result.error)
                }

                is Result.PartialSuccess -> SyncResult.Loading(
                    progress = result.list.size.toFloat(),
                    total = result.total.toFloat()
                )

                is Result.Success -> {
                    spaceDao.rewritePlanets(planets = result.list)
                    SyncResult.Success
                }
            }
        }

    override suspend fun prepopulateStellarHosts() {
        if (spaceDao.isStellarHostEmpty()) {
            val stellarHosts = loadHostsFromJson()
            spaceDao.rewriteStellarHosts(stellarHosts = stellarHosts)
        }
    }

    override suspend fun prepopulatePlanets() {
        if (spaceDao.isPlanetEmpty()) {
            val planets = loadPlanetsFromJson()
            spaceDao.rewritePlanets(planets = planets)
        }
    }

    override suspend fun getExoplanets(): List<StellarHost> {
        val planets = spaceDao.getPlanets().groupBy { it.stellarHostId }
        return spaceDao.getStellarHosts().apply {
            forEach { it.planets.addAll(elements = planets[it.id].orEmpty()) }
        }.sortedWith(comparator = compareBy<StellarHost, Double?>(comparator = nullsLast()) { it.distance }.thenBy { it.id })
    }

    override suspend fun getNearestStars(
        stellarHost: StellarHost,
        stellarHosts: List<StellarHost>,
        n: Int,
        visited: Set<String>
    ): List<StellarHost> {
        val stellarHostCP = stellarHost.toCartesian() ?: return emptyList()
        return stellarHosts
            .asSequence()
            .filter { it.id != stellarHost.id && it.id !in visited }
            .mapNotNull { other ->
                val otherCP = other.toCartesian() ?: return@mapNotNull null
                val distance = stellarHostCP.distanceBetween(cp = otherCP)
                Pair(first = other, second = distance)
            }
            .sortedBy { it.second }
            .take(n = n)
            .map {
                it.first.copy(distance = it.second).apply {
                    planets.addAll(elements = it.first.planets)
                    travelOutcome = TravelOutcome(fuel = ceil(x = it.second).toInt())
                }
            }
            .toList()
    }
}
