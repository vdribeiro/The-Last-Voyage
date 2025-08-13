package com.hybris.tlv.usecase.space

import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.storage.saveFile
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.combine
import com.hybris.tlv.usecase.space.formula.DerivedData
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
import kotlinx.coroutines.flow.flow
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

    override suspend fun rewrite(): Flow<SyncResult> {
        val stellarHosts = loadHostsFromJson()
        val planets = loadPlanetsFromJson()
        spaceDao.rewriteStellarHosts(stellarHosts = stellarHosts)
        spaceDao.rewritePlanets(planets = planets)
        val stellarHostsFlow = spaceApi.rewriteStellarHosts(stellarHosts = stellarHosts)
        val planetsFlow = spaceApi.rewritePlanets(planets = planets)
        return combine(flows = listOf(stellarHostsFlow, planetsFlow)) { it.combine() }
    }

    override suspend fun getArchive(): Flow<SyncResult> = flow {
        val totalOperations = 6f
        emit(value = SyncResult.Loading(progress = 0f, total = totalOperations))
        val stellarHosts = runCatching {
            val jsonString = Res.readBytes(path = "files/solarsystem.json").decodeToString()
            json.decodeFromString<List<StellarHost>>(string = jsonString)
        }.getOrDefault(defaultValue = emptyList()).toMutableList()
        val planets = runCatching {
            val jsonString = Res.readBytes(path = "files/solarplanets.json").decodeToString()
            json.decodeFromString<List<Planet>>(string = jsonString)
        }.getOrDefault(defaultValue = emptyList()).toMutableList()

        emit(value = SyncResult.Loading(progress = 1f, total = totalOperations))
        when (val stellarHostsArchiveResult = getArchive { spaceApi.getStellarHostsArchive(queryMap = it) }) {
            is ExoplanetsResult.Error -> emit(value = SyncResult.Error(error = stellarHostsArchiveResult.error))
            is ExoplanetsResult.Success -> stellarHosts.addAll(elements = stellarHostsArchiveResult.stellarHosts)
        }

        emit(value = SyncResult.Loading(progress = 2f, total = totalOperations))
        when (val exoplanetsArchiveResult = getArchive { spaceApi.getExoplanetsArchive(queryMap = it) }) {
            is ExoplanetsResult.Error -> emit(value = SyncResult.Error(error = exoplanetsArchiveResult.error))
            is ExoplanetsResult.Success -> {
                val stellarHostIds = stellarHosts.map { it.id }
                val filteredStellarHosts = exoplanetsArchiveResult.stellarHosts.filter { it.id !in stellarHostIds }
                stellarHosts.addAll(elements = filteredStellarHosts)
                planets.addAll(elements = exoplanetsArchiveResult.planets)
            }
        }

        emit(value = SyncResult.Loading(progress = 3f, total = totalOperations))
        when (val k2ExoplanetsArchiveResult = getArchive { spaceApi.getK2ExoplanetsArchive(queryMap = it) }) {
            is ExoplanetsResult.Error -> emit(value = SyncResult.Error(error = k2ExoplanetsArchiveResult.error))
            is ExoplanetsResult.Success -> {
                val stellarHostIds = stellarHosts.map { it.id }
                val filteredStellarHosts = k2ExoplanetsArchiveResult.stellarHosts.filter { it.id !in stellarHostIds }
                stellarHosts.addAll(elements = filteredStellarHosts)

                val planetIds = planets.map { it.id }
                val filteredPlanets = k2ExoplanetsArchiveResult.planets.filter { it.id !in planetIds }
                planets.addAll(elements = filteredPlanets)
            }
        }

        emit(value = SyncResult.Loading(progress = 4f, total = totalOperations))
        val planetMap = planets.mergePlanets().groupBy { it.stellarHostId }
        val mergedStellarHosts = stellarHosts.mergeStellarHosts().apply {
            forEach { it.planets.addAll(elements = planetMap[it.id].orEmpty()) }
        }
        val derivedStellarHosts = DerivedData.derive(stellarHosts = mergedStellarHosts)
        val derivedPlanets = derivedStellarHosts.map { it.planets }.flatten()

        emit(value = SyncResult.Loading(progress = 5f, total = totalOperations))
        saveFile(fileName = "hosts.json", content = json.encodeToString(value = derivedStellarHosts.map { it.copy() }))
        saveFile(fileName = "planets.json", content = json.encodeToString(value = derivedPlanets.map { it.copy() }))

        emit(value = SyncResult.Success)
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
