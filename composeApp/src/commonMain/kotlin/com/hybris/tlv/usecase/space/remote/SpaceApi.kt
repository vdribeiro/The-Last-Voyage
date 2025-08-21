package com.hybris.tlv.usecase.space.remote

import com.hybris.tlv.http.EXOPLANET_ARCHIVE_URL
import com.hybris.tlv.http.QueryMap
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.STELLAR_HOSTS_URL
import com.hybris.tlv.http.getStream
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.json
import com.hybris.tlv.usecase.space.mapper.toPlanet
import com.hybris.tlv.usecase.space.mapper.toStellarHost
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.remote.json.ExoplanetJson
import com.hybris.tlv.usecase.space.remote.json.StellarHostJson
import com.hybris.tlv.usecase.space.remote.result.ExoplanetsResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.encodeURLPath

internal class SpaceApi(
    private val httpClient: HttpClient,
): SpaceRemote {

    override suspend fun getStellarHostsArchive(queryMap: QueryMap): ExoplanetsResult = runCatching {
        val offset = queryMap.offset ?: 0
        val limit = queryMap.limit ?: Long.MAX_VALUE
        val query = "select+*+from+(+select+t.*,rownum+as+rn+from+(+select+" +
                "${StellarHostJson.STELLAR_HOST_NAME}," +
                "${StellarHostJson.STELLAR_HOST_SYSTEM_NAME}," +
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
        val queryMap = QueryMap().apply {
            set(key = "query", value = query)
            set(key = "format", value = "json")
        }
        val response = httpClient.get {
            url(path = EXOPLANET_ARCHIVE_URL.encodeURLPath())
            queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
        }.call.body<String>()

        val json = json.decodeFromString<List<StellarHostJson>>(string = response)
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
                "${ExoplanetJson.PLANET_OCCULTATION_DEPTH}," +
                "${ExoplanetJson.PLANET_INCLINATION}," +
                "${ExoplanetJson.PLANET_OBLIQUITY}," +
                ExoplanetJson.PLANET_PROJECTED_OBLIQUITY +
                "+from+pscomppars" +
                "+order+by+${ExoplanetJson.PLANET_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val queryMap = QueryMap().apply {
            set(key = "query", value = query)
            set(key = "format", value = "json")
        }
        val response = httpClient.get {
            url(path = EXOPLANET_ARCHIVE_URL.encodeURLPath())
            queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
        }.call.body<String>()
        val json = json.decodeFromString<List<ExoplanetJson>>(string = response)
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
                "${ExoplanetJson.PLANET_OCCULTATION_DEPTH}," +
                "${ExoplanetJson.PLANET_INCLINATION}," +
                "${ExoplanetJson.PLANET_OBLIQUITY}," +
                ExoplanetJson.PLANET_PROJECTED_OBLIQUITY +
                "+from+k2pandc" +
                "+order+by+${ExoplanetJson.PLANET_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val queryMap = QueryMap().apply {
            set(key = "query", value = query)
            set(key = "format", value = "json")
        }
        val response = httpClient.get {
            url(path = EXOPLANET_ARCHIVE_URL.encodeURLPath())
            queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
        }.call.body<String>()
        val json = json.decodeFromString<List<ExoplanetJson>>(string = response)
        ExoplanetsResult.Success(stellarHosts = json.map { it.toStellarHost() }, planets = json.map { it.toPlanet() })
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        ExoplanetsResult.Error(error = it.message.orEmpty())
    }

    override suspend fun getStellarHosts(): Result<StellarHost> =
        httpClient.getStream(url = STELLAR_HOSTS_URL)

    override suspend fun getPlanets(): Result<Planet> =
        httpClient.getStream(url = STELLAR_HOSTS_URL)

    companion object {
        private const val TAG = "SpaceApi"
    }
}
