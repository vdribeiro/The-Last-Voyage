package com.hybris.tlv.usecase.space

import com.hybris.tlv.http.QueryMap
import com.hybris.tlv.serializer.json
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.storage.saveFile
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.space.formula.DerivedData
import com.hybris.tlv.usecase.space.local.SpaceLocal
import com.hybris.tlv.usecase.space.mapper.mergePlanets
import com.hybris.tlv.usecase.space.mapper.mergeStellarHosts
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.remote.SpaceRemote
import com.hybris.tlv.usecase.space.remote.result.ExoplanetsResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class SpaceInternalGateway(
    private val spaceApi: SpaceRemote,
    private val spaceDao: SpaceLocal,
): SpaceInternalUseCases {

    override suspend fun getArchive(): Flow<SyncResult> = flow {
        val totalOperations = 6f
        emit(value = SyncResult.Loading(progress = 0f, total = totalOperations))
        val stellarHosts = loadFromJson<StellarHost>(path = "files/solarsystem.json").toMutableList()
        val planets = loadFromJson<Planet>(path = "files/solarplanets.json").toMutableList()

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

    override suspend fun rewriteStellarHosts(): Flow<SyncResult> {
        val stellarHosts: List<StellarHost> = loadFromJson(path = "files/hosts.json")
        spaceDao.rewriteStellarHosts(stellarHosts = stellarHosts)
        return spaceApi.rewriteStellarHosts(stellarHosts = stellarHosts)
    }

    override suspend fun rewritePlanets(): Flow<SyncResult> {
        val planets: List<Planet> = loadFromJson(path = "files/planets.json")
        spaceDao.rewritePlanets(planets = planets)
        return spaceApi.rewritePlanets(planets = planets)
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
            val stellarHosts: List<StellarHost> = loadFromJson(path = "files/hosts.json")
            spaceDao.rewriteStellarHosts(stellarHosts = stellarHosts)
        }
    }

    override suspend fun prepopulatePlanets() {
        if (spaceDao.isPlanetEmpty()) {
            val planets: List<Planet> = loadFromJson(path = "files/planets.json")
            spaceDao.rewritePlanets(planets = planets)
        }
    }
}
