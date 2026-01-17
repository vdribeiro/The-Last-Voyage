package com.hybris.tlv.usecase.space

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.serializer.JsonFile
import com.hybris.tlv.serializer.JsonResource
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.serializer.saveJsonFile
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.space.formula.DerivedData
import com.hybris.tlv.usecase.space.model.ExoplanetJson
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_DENSITY
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_ECCENTRICITY
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_EQUILIBRIUM_TEMPERATURE
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_INCLINATION
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_INSOLATION_FLUX
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_MASS
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_NAME
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_OBLIQUITY
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_OCCULTATION_DEPTH
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_ORBITAL_PERIOD
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_ORBIT_AXIS
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_PROJECTED_OBLIQUITY
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_RADIUS
import com.hybris.tlv.usecase.space.model.JsonConstants.PLANET_STATUS
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_AGE
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_DEC
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_DENSITY
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_DISTANCE
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_GRAVITY
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_LUMINOSITY
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_MASS
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_METALLICITY
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_NAME
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_RA
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_RADIUS
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_ROTATIONAL_PERIOD
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_ROTATIONAL_VELOCITY
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_SPECTRAL_TYPE
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_SYSTEM_NAME
import com.hybris.tlv.usecase.space.model.JsonConstants.STELLAR_HOST_TEMPERATURE
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.model.StellarHostJson

internal class ArchiveGateway(
    private val httpClient: HttpClient,
): ArchiveUseCases {

    private data class Exoplanets(val stellarHosts: List<StellarHost>, val planets: List<Planet>)

    override suspend fun getArchive(): Boolean = withContext(context = Dispatcher.IO) {
        runCatching {
            coroutineScope {
                // Get archive
                val stellarHostsJob = async { getArchive { offset, limit -> getStellarHostsArchive(offset, limit) } }
                val planetarySystemsCompositeJob = async { getArchive { offset, limit -> getPlanetarySystemsCompositeArchive(offset, limit) } }
                val k2PlanetsJob = async { getArchive { offset, limit -> getK2PlanetsArchive(offset, limit) } }
                val stellarHostsResult = stellarHostsJob.await()
                val planetarySystemsCompositeResult = planetarySystemsCompositeJob.await()
                val k2PlanetsResult = k2PlanetsJob.await()

                // Data enrichment
                val stellarHosts = (loadFromJsonResource<StellarHost>(json = JsonResource.SolarHosts) +
                        stellarHostsResult.stellarHosts +
                        planetarySystemsCompositeResult.stellarHosts +
                        k2PlanetsResult.stellarHosts).mergeStellarHosts()
                val planets = (loadFromJsonResource<Planet>(json = JsonResource.SolarPlanets) +
                        stellarHostsResult.planets +
                        planetarySystemsCompositeResult.planets +
                        k2PlanetsResult.planets).mergePlanets()

                // Derive missing data
                val derivedStellarHosts = DerivedData.derive(stellarHosts = stellarHosts.addPlanets(planets = planets))
                val derivedPlanets = derivedStellarHosts.map { it.planets }.flatten()

                // Strip member properties so only constructor properties are serialized
                val stellarHostsJson = derivedStellarHosts.map { it.copy() }
                val planetsJson = derivedPlanets.map { it.copy() }

                // Save to file
                val hostsFile = saveJsonFile(json = JsonFile.ArchiveStellarHosts, content = stellarHostsJson)
                val planetsFile = saveJsonFile(json = JsonFile.ArchivePlanets, content = planetsJson)
                Telemetry.info(tag = TAG, message = "Hosts file saved: $hostsFile\nPlanets file saved: $planetsFile")
                hostsFile && planetsFile
            }
        }.onFailure { Telemetry.error(tag = TAG, message = "Unable to get archive", throwable = it) }.getOrDefault(defaultValue = false)
    }

    private suspend fun getArchive(limit: Int = PAGE_SIZE, apiCall: suspend (Int, Int) -> Exoplanets): Exoplanets {
        val stellarHosts = mutableListOf<StellarHost>()
        val planets = mutableListOf<Planet>()
        var offset = 0
        do {
            val result = apiCall(offset, limit)
            stellarHosts.addAll(elements = result.stellarHosts)
            planets.addAll(elements = result.planets)
            offset += limit
        } while (result.stellarHosts.size >= limit || result.planets.size >= limit)
        return Exoplanets(stellarHosts = stellarHosts, planets = planets)
    }

    /**
     * Get data from DOI 10.26133/NEA40.
     */
    private suspend fun getStellarHostsArchive(offset: Int, limit: Int): Exoplanets {
        val query = "select+*+from+(+select+t.*,rownum+as+rn+from+(+select+" +
                "${STELLAR_HOST_NAME}," +
                "${STELLAR_HOST_SYSTEM_NAME}," +
                "${STELLAR_HOST_SPECTRAL_TYPE}," +
                "${STELLAR_HOST_TEMPERATURE}," +
                "${STELLAR_HOST_RADIUS}," +
                "${STELLAR_HOST_MASS}," +
                "${STELLAR_HOST_METALLICITY}," +
                "${STELLAR_HOST_LUMINOSITY}," +
                "${STELLAR_HOST_GRAVITY}," +
                "${STELLAR_HOST_AGE}," +
                "${STELLAR_HOST_DENSITY}," +
                "${STELLAR_HOST_ROTATIONAL_VELOCITY}," +
                "${STELLAR_HOST_ROTATIONAL_PERIOD}," +
                "${STELLAR_HOST_DISTANCE}," +
                "${STELLAR_HOST_RA}," +
                STELLAR_HOST_DEC +
                "+from+stellarhosts" +
                "+order+by+${STELLAR_HOST_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val queryMap = mutableMapOf<String, String>().apply {
            set(key = "query", value = query)
            set(key = "format", value = "json")
        }

        return when (val response = httpClient.get<StellarHostJson>(
            path = URL.ExoplanetArchive,
            queryMap = queryMap
        ) { timeout { requestTimeoutMillis = TIMEOUT } }) {
            is Result.Error<StellarHostJson> -> throw Throwable(message = "Unable to get stellar hosts archive", cause = response.error)
            is Result.Success<StellarHostJson> -> Exoplanets(
                stellarHosts = response.list.map { it.toStellarHost() },
                planets = emptyList()
            )
        }
    }

    /**
     * Get data from DOI 10.26133/NEA13.
     */
    private suspend fun getPlanetarySystemsCompositeArchive(offset: Int, limit: Int): Exoplanets {
        val query = "select+*+from+(+select+t.*,rownum+as+rn+from+(+select+" +
                "${STELLAR_HOST_NAME}," +
                "${STELLAR_HOST_SPECTRAL_TYPE}," +
                "${STELLAR_HOST_TEMPERATURE}," +
                "${STELLAR_HOST_RADIUS}," +
                "${STELLAR_HOST_MASS}," +
                "${STELLAR_HOST_METALLICITY}," +
                "${STELLAR_HOST_LUMINOSITY}," +
                "${STELLAR_HOST_GRAVITY}," +
                "${STELLAR_HOST_AGE}," +
                "${STELLAR_HOST_DENSITY}," +
                "${STELLAR_HOST_ROTATIONAL_VELOCITY}," +
                "${STELLAR_HOST_ROTATIONAL_PERIOD}," +
                "${STELLAR_HOST_DISTANCE}," +
                "${STELLAR_HOST_RA}," +
                "${STELLAR_HOST_DEC}," +
                "${PLANET_NAME}," +
                "${PLANET_ORBITAL_PERIOD}," +
                "${PLANET_ORBIT_AXIS}," +
                "${PLANET_RADIUS}," +
                "${PLANET_MASS}," +
                "${PLANET_DENSITY}," +
                "${PLANET_ECCENTRICITY}," +
                "${PLANET_INSOLATION_FLUX}," +
                "${PLANET_EQUILIBRIUM_TEMPERATURE}," +
                "${PLANET_OCCULTATION_DEPTH}," +
                "${PLANET_INCLINATION}," +
                "${PLANET_OBLIQUITY}," +
                PLANET_PROJECTED_OBLIQUITY +
                "+from+pscomppars" +
                "+order+by+${PLANET_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val queryMap = mutableMapOf<String, String>().apply {
            set(key = "query", value = query)
            set(key = "format", value = "json")
        }
        return when (val response = httpClient.get<ExoplanetJson>(
            path = URL.ExoplanetArchive,
            queryMap = queryMap
        ) { timeout { requestTimeoutMillis = TIMEOUT } }) {
            is Result.Error<ExoplanetJson> -> throw Throwable(message = "Unable to get planetary systems composite archive", cause = response.error)
            is Result.Success<ExoplanetJson> -> Exoplanets(
                stellarHosts = response.list.map { it.toStellarHost(systemName = null) },
                planets = response.list.map { it.toPlanet() }
            )
        }
    }

    /**
     * Get data from DOI 10.26133/NEA19.
     */
    private suspend fun getK2PlanetsArchive(offset: Int, limit: Int): Exoplanets {
        val query = "select+*+from+(+select+t.*,rownum+as+rn+from+(+select+" +
                "${STELLAR_HOST_NAME}," +
                "${STELLAR_HOST_SPECTRAL_TYPE}," +
                "${STELLAR_HOST_TEMPERATURE}," +
                "${STELLAR_HOST_RADIUS}," +
                "${STELLAR_HOST_MASS}," +
                "${STELLAR_HOST_METALLICITY}," +
                "${STELLAR_HOST_LUMINOSITY}," +
                "${STELLAR_HOST_GRAVITY}," +
                "${STELLAR_HOST_AGE}," +
                "${STELLAR_HOST_DENSITY}," +
                "${STELLAR_HOST_ROTATIONAL_VELOCITY}," +
                "${STELLAR_HOST_ROTATIONAL_PERIOD}," +
                "${STELLAR_HOST_DISTANCE}," +
                "${STELLAR_HOST_RA}," +
                "${STELLAR_HOST_DEC}," +
                "${PLANET_NAME}," +
                "${PLANET_STATUS}," +
                "${PLANET_ORBITAL_PERIOD}," +
                "${PLANET_ORBIT_AXIS}," +
                "${PLANET_RADIUS}," +
                "${PLANET_MASS}," +
                "${PLANET_DENSITY}," +
                "${PLANET_ECCENTRICITY}," +
                "${PLANET_INSOLATION_FLUX}," +
                "${PLANET_EQUILIBRIUM_TEMPERATURE}," +
                "${PLANET_OCCULTATION_DEPTH}," +
                "${PLANET_INCLINATION}," +
                "${PLANET_OBLIQUITY}," +
                PLANET_PROJECTED_OBLIQUITY +
                "+from+k2pandc" +
                "+order+by+${PLANET_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val queryMap = mutableMapOf<String, String>().apply {
            set(key = "query", value = query)
            set(key = "format", value = "json")
        }
        return when (val response = httpClient.get<ExoplanetJson>(
            path = URL.ExoplanetArchive,
            queryMap = queryMap
        ) { timeout { requestTimeoutMillis = TIMEOUT } }) {
            is Result.Error<ExoplanetJson> -> throw Throwable(message = "Unable to get K2 Planets archive", cause = response.error)
            is Result.Success<ExoplanetJson> -> Exoplanets(
                stellarHosts = response.list.map { it.toStellarHost(systemName = null) },
                planets = response.list.map { it.toPlanet() }
            )
        }
    }

    companion object {
        private const val TAG = "Archive"
        private const val TIMEOUT = 300_000L // 5 minutes (It's a slow API)
        private const val PAGE_SIZE = 10000
    }
}
