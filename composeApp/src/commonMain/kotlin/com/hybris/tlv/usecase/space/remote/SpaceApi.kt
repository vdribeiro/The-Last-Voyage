package com.hybris.tlv.usecase.space.remote

import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.client.HttpClient
import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.http.request.Request
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.space.mapper.toPlanet
import com.hybris.tlv.usecase.space.mapper.toPlanetMap
import com.hybris.tlv.usecase.space.mapper.toStellarHost
import com.hybris.tlv.usecase.space.mapper.toStellarHostMap
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.remote.json.ExoplanetJson
import com.hybris.tlv.usecase.space.remote.json.StellarHostJson
import com.hybris.tlv.usecase.space.remote.result.ExoplanetsResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

internal class SpaceApi(
    private val exoplanetHttpClient: HttpClient,
    private val firestore: Firestore
): SpaceRemote {

    override suspend fun getStellarHostsArchive(queryMap: QueryMap): ExoplanetsResult = runCatching {
        val offset = queryMap.offset ?: 0
        val limit = queryMap.limit ?: Long.MAX_VALUE
        val query = "select+*+from+(+select+t.*,rownum+as+rn+from+(+select+" +
                "${StellarHostJson.STELLAR_HOST_SYSTEM_NAME}," +
                "${StellarHostJson.STELLAR_HOST_NAME}," +
                "${StellarHostJson.STELLAR_HOST_SPECTRAL_TYPE}," +
                "${StellarHostJson.STELLAR_HOST_TEMPERATURE}," +
                "${StellarHostJson.STELLAR_HOST_RADIUS}," +
                "${StellarHostJson.STELLAR_HOST_MASS}," +
                "${StellarHostJson.STELLAR_HOST_METALLICITY}," +
                "${StellarHostJson.STELLAR_HOST_LUMINOSITY}," +
                "${StellarHostJson.STELLAR_HOST_GRAVITY}," +
                "${StellarHostJson.STELLAR_HOST_AGE}," +
                "${StellarHostJson.STELLAR_HOST_DENSITY}," +
                "${StellarHostJson.STELLAR_HOST_ROTATIONAL_VELOCITY}," +
                "${StellarHostJson.STELLAR_HOST_ROTATIONAL_PERIOD}," +
                "${StellarHostJson.STELLAR_HOST_DISTANCE}," +
                "${StellarHostJson.STELLAR_HOST_RA}," +
                StellarHostJson.STELLAR_HOST_DEC +
                "+from+stellarhosts" +
                "+order+by+${StellarHostJson.STELLAR_HOST_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val request = Request(
            path = "sync",
            queryMap = QueryMap().apply {
                set(key = "query", value = query)
                set(key = "format", value = "json")
            }
        )
        val response = exoplanetHttpClient.get<String>(request = request)
        val json = json.decodeFromString<List<StellarHostJson>>(string = response.body)
        ExoplanetsResult.Success(stellarHosts = json.map { it.toStellarHost() }, planets = emptyList())
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        ExoplanetsResult.Error(error = it.message.orEmpty())
    }

    override suspend fun getExoplanetsArchive(queryMap: QueryMap): ExoplanetsResult = runCatching {
        val offset = queryMap.offset ?: 0
        val limit = queryMap.limit ?: Long.MAX_VALUE
        val query = "select+*+from+(+select+t.*,rownum+as+rn+from+(+select+" +
                "${StellarHostJson.STELLAR_HOST_NAME}," +
                "${StellarHostJson.STELLAR_HOST_SPECTRAL_TYPE}," +
                "${StellarHostJson.STELLAR_HOST_TEMPERATURE}," +
                "${StellarHostJson.STELLAR_HOST_RADIUS}," +
                "${StellarHostJson.STELLAR_HOST_MASS}," +
                "${StellarHostJson.STELLAR_HOST_METALLICITY}," +
                "${StellarHostJson.STELLAR_HOST_LUMINOSITY}," +
                "${StellarHostJson.STELLAR_HOST_GRAVITY}," +
                "${StellarHostJson.STELLAR_HOST_AGE}," +
                "${StellarHostJson.STELLAR_HOST_DENSITY}," +
                "${StellarHostJson.STELLAR_HOST_ROTATIONAL_VELOCITY}," +
                "${StellarHostJson.STELLAR_HOST_ROTATIONAL_PERIOD}," +
                "${StellarHostJson.STELLAR_HOST_DISTANCE}," +
                "${StellarHostJson.STELLAR_HOST_RA}," +
                "${StellarHostJson.STELLAR_HOST_DEC}," +
                "${ExoplanetJson.PLANET_NAME}," +
                "${ExoplanetJson.PLANET_ORBITAL_PERIOD}," +
                "${ExoplanetJson.PLANET_ORBIT_AXIS}," +
                "${ExoplanetJson.PLANET_RADIUS}," +
                "${ExoplanetJson.PLANET_MASS}," +
                "${ExoplanetJson.PLANET_DENSITY}," +
                "${ExoplanetJson.PLANET_ECCENTRICITY}," +
                "${ExoplanetJson.PLANET_INSOLATION_FLUX}," +
                "${ExoplanetJson.PLANET_EQUILIBRIUM_TEMPERATURE}," +
                "${ExoplanetJson.PLANET_INCLINATION}," +
                "${ExoplanetJson.PLANET_OBLIQUITY}," +
                ExoplanetJson.PLANET_PROJECTED_OBLIQUITY +
                "+from+pscomppars" +
                "+order+by+${ExoplanetJson.PLANET_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val request = Request(
            path = "sync",
            queryMap = QueryMap().apply {
                set(key = "query", value = query)
                set(key = "format", value = "json")
            }
        )
        val response = exoplanetHttpClient.get<String>(request = request)
        val json = json.decodeFromString<List<ExoplanetJson>>(string = response.body)
        ExoplanetsResult.Success(stellarHosts = json.map { it.toStellarHost() }, planets = json.map { it.toPlanet() })
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        ExoplanetsResult.Error(error = it.message.orEmpty())
    }

    override suspend fun getK2ExoplanetsArchive(queryMap: QueryMap): ExoplanetsResult = runCatching {
        val offset = queryMap.offset ?: 0
        val limit = queryMap.limit ?: Long.MAX_VALUE
        val query = "select+*+from+(+select+t.*,rownum+as+rn+from+(+select+" +
                "${StellarHostJson.STELLAR_HOST_NAME}," +
                "${StellarHostJson.STELLAR_HOST_SPECTRAL_TYPE}," +
                "${StellarHostJson.STELLAR_HOST_TEMPERATURE}," +
                "${StellarHostJson.STELLAR_HOST_RADIUS}," +
                "${StellarHostJson.STELLAR_HOST_MASS}," +
                "${StellarHostJson.STELLAR_HOST_METALLICITY}," +
                "${StellarHostJson.STELLAR_HOST_LUMINOSITY}," +
                "${StellarHostJson.STELLAR_HOST_GRAVITY}," +
                "${StellarHostJson.STELLAR_HOST_AGE}," +
                "${StellarHostJson.STELLAR_HOST_DENSITY}," +
                "${StellarHostJson.STELLAR_HOST_ROTATIONAL_VELOCITY}," +
                "${StellarHostJson.STELLAR_HOST_ROTATIONAL_PERIOD}," +
                "${StellarHostJson.STELLAR_HOST_DISTANCE}," +
                "${StellarHostJson.STELLAR_HOST_RA}," +
                "${StellarHostJson.STELLAR_HOST_DEC}," +
                "${ExoplanetJson.PLANET_NAME}," +
                "${ExoplanetJson.PLANET_STATUS}," +
                "${ExoplanetJson.PLANET_ORBITAL_PERIOD}," +
                "${ExoplanetJson.PLANET_ORBIT_AXIS}," +
                "${ExoplanetJson.PLANET_RADIUS}," +
                "${ExoplanetJson.PLANET_MASS}," +
                "${ExoplanetJson.PLANET_DENSITY}," +
                "${ExoplanetJson.PLANET_ECCENTRICITY}," +
                "${ExoplanetJson.PLANET_INSOLATION_FLUX}," +
                "${ExoplanetJson.PLANET_EQUILIBRIUM_TEMPERATURE}," +
                "${ExoplanetJson.PLANET_INCLINATION}," +
                "${ExoplanetJson.PLANET_OBLIQUITY}," +
                ExoplanetJson.PLANET_PROJECTED_OBLIQUITY +
                "+from+k2pandc" +
                "+order+by+${ExoplanetJson.PLANET_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val request = Request(
            path = "sync",
            queryMap = QueryMap().apply {
                set(key = "query", value = query)
                set(key = "format", value = "json")
            }
        )
        val response = exoplanetHttpClient.get<String>(request = request)
        val json = json.decodeFromString<List<ExoplanetJson>>(string = response.body)
        ExoplanetsResult.Success(stellarHosts = json.map { it.toStellarHost() }, planets = json.map { it.toPlanet() })
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        ExoplanetsResult.Error(error = it.message.orEmpty())
    }

    override suspend fun rewriteStellarHosts(stellarHosts: List<StellarHost>): Flow<SyncResult> = runCatching {
        firestore.removeCollection(collection = HOSTS).last()
        firestore.setCollection(collection = HOSTS, documents = stellarHosts.map { it.toStellarHostMap() }).map { result ->
            when (result) {
                is FirestoreWriteResult.Error -> SyncResult.Error(error = result.error)
                is FirestoreWriteResult.PartialSuccess -> SyncResult.Loading(
                    progress = result.documents.size.toFloat(),
                    total = result.totalDocuments.toFloat()
                )

                is FirestoreWriteResult.Success -> SyncResult.Success
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = SyncResult.Error(error = it.message.orEmpty()))
    }

    override suspend fun rewritePlanets(planets: List<Planet>): Flow<SyncResult> = runCatching {
        firestore.removeCollection(collection = PLANETS).last()
        firestore.setCollection(collection = PLANETS, documents = planets.map { it.toPlanetMap() }).map { result ->
            when (result) {
                is FirestoreWriteResult.Error -> SyncResult.Error(error = result.error)
                is FirestoreWriteResult.PartialSuccess -> SyncResult.Loading(
                    progress = result.documents.size.toFloat(),
                    total = result.totalDocuments.toFloat()
                )

                is FirestoreWriteResult.Success -> SyncResult.Success
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = SyncResult.Error(error = it.message.orEmpty()))
    }

    override suspend fun getStellarHosts(queryMap: QueryMap): Flow<Result<StellarHost>> = runCatching {
        firestore.getCollection(collection = HOSTS, queryMap = queryMap).map { result ->
            when (result) {
                is FirestoreReadResult.Error -> Result.Error(error = result.error)
                is FirestoreReadResult.PartialSuccess -> Result.PartialSuccess(
                    list = result.documents.map { it.toStellarHost() },
                    total = result.totalDocuments
                )

                is FirestoreReadResult.Success -> Result.Success(list = result.documents.map { it.toStellarHost() })
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = Result.Error(error = it.message.orEmpty()))
    }

    override suspend fun getPlanets(queryMap: QueryMap): Flow<Result<Planet>> = runCatching {
        firestore.getCollection(collection = PLANETS, queryMap = queryMap).map { result ->
            when (result) {
                is FirestoreReadResult.Error -> Result.Error(error = result.error)
                is FirestoreReadResult.PartialSuccess -> Result.PartialSuccess(
                    list = result.documents.map { it.toPlanet() },
                    total = result.totalDocuments
                )

                is FirestoreReadResult.Success -> Result.Success(list = result.documents.map { it.toPlanet() })
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = Result.Error(error = it.message.orEmpty()))
    }

    companion object {
        private const val TAG = "SpaceApi"

        private const val HOSTS = "hosts"
        const val STELLAR_HOST_HOST_ID = "id"
        const val STELLAR_HOST_SYSTEM_NAME = "system_name"
        const val STELLAR_HOST_HOST_NAME = "host_name"
        const val STELLAR_HOST_SPECTRAL_TYPE = "spectral_type"
        const val STELLAR_HOST_TEMPERATURE = "effective_temperature"
        const val STELLAR_HOST_RADIUS = "radius"
        const val STELLAR_HOST_MASS = "mass"
        const val STELLAR_HOST_METALLICITY = "metallicity"
        const val STELLAR_HOST_LUMINOSITY = "luminosity"
        const val STELLAR_HOST_GRAVITY = "gravity"
        const val STELLAR_HOST_AGE = "age"
        const val STELLAR_HOST_DENSITY = "density"
        const val STELLAR_HOST_ROTATIONAL_VELOCITY = "rotational_velocity"
        const val STELLAR_HOST_ROTATIONAL_PERIOD = "rotational_period"
        const val STELLAR_HOST_DISTANCE = "distance"
        const val STELLAR_HOST_RA = "ra"
        const val STELLAR_HOST_DEC = "dec"

        private const val PLANETS = "planets"
        const val PLANET_ID = "id"
        const val PLANET_NAME = "name"
        const val PLANET_HOST_ID = "stellar_host_id"
        const val PLANET_STATUS = "status"
        const val PLANET_ORBITAL_PERIOD = "orbital_period"
        const val PLANET_ORBIT_AXIS = "orbit_axis"
        const val PLANET_RADIUS = "radius"
        const val PLANET_MASS = "mass"
        const val PLANET_DENSITY = "density"
        const val PLANET_ECCENTRICITY = "eccentricity"
        const val PLANET_INSOLATION_FLUX = "insolation_flux"
        const val PLANET_EQUILIBRIUM_TEMPERATURE = "equilibrium_temperature"
        const val PLANET_INCLINATION = "inclination"
        const val PLANET_OBLIQUITY = "obliquity"
    }
}
