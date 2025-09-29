package com.hybris.tlv.usecase.space

import com.hybris.tlv.http.HttpClientFactory.Companion.EXOPLANET_ARCHIVE_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.http.setTimeout
import com.hybris.tlv.serializer.PLANETS_JSON
import com.hybris.tlv.serializer.SOLAR_HOSTS_JSON
import com.hybris.tlv.serializer.SOLAR_PLANETS_JSON
import com.hybris.tlv.serializer.STELLAR_HOSTS_JSON
import com.hybris.tlv.serializer.json
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.storage.saveFile
import com.hybris.tlv.telemetry.Logger
import com.hybris.tlv.usecase.space.formula.DerivedData
import com.hybris.tlv.usecase.space.formula.parsecsToLightYears
import com.hybris.tlv.usecase.space.formula.stellarHostGravityToSunGravity
import com.hybris.tlv.usecase.space.model.ExoplanetJson
import com.hybris.tlv.usecase.space.model.ExoplanetsResult
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
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.model.StellarHostJson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout

internal class ArchiveGateway(
    private val httpClient: HttpClient,
): ArchiveUseCases {

    /**
     * Request timeout in milliseconds.
     */
    private val timeout = 1_800_000L // 30 minutes (It's a very slow API)
    /**
     * Request page size.
     */
    private val pageSize = 5000

    override suspend fun getArchive() {
        val stellarHosts = loadFromJsonResource<StellarHost>(path = SOLAR_HOSTS_JSON).toMutableList()
        val planets = loadFromJsonResource<Planet>(path = SOLAR_PLANETS_JSON).toMutableList()

        when (val stellarHostsArchiveResult = getArchive { offset, limit -> getStellarHostsArchive(offset = offset, limit = limit) }) {
            is ExoplanetsResult.Error -> Logger.error(tag = TAG, message = stellarHostsArchiveResult.error)
            is ExoplanetsResult.Success -> stellarHosts.addAll(elements = stellarHostsArchiveResult.stellarHosts)
        }

        when (val exoplanetsArchiveResult = getArchive { offset, limit -> getExoplanetsArchive(offset = offset, limit = limit) }) {
            is ExoplanetsResult.Error -> Logger.error(tag = TAG, message = exoplanetsArchiveResult.error)
            is ExoplanetsResult.Success -> {
                val stellarHostIds = stellarHosts.map { it.id }
                val filteredStellarHosts = exoplanetsArchiveResult.stellarHosts.filter { it.id !in stellarHostIds }
                stellarHosts.addAll(elements = filteredStellarHosts)
                planets.addAll(elements = exoplanetsArchiveResult.planets)
            }
        }

        when (val k2ExoplanetsArchiveResult = getArchive { offset, limit -> getK2ExoplanetsArchive(offset = offset, limit = limit) }) {
            is ExoplanetsResult.Error -> Logger.error(tag = TAG, message = k2ExoplanetsArchiveResult.error)
            is ExoplanetsResult.Success -> {
                val stellarHostIds = stellarHosts.map { it.id }
                val filteredStellarHosts = k2ExoplanetsArchiveResult.stellarHosts.filter { it.id !in stellarHostIds }
                stellarHosts.addAll(elements = filteredStellarHosts)

                val planetIds = planets.map { it.id }
                val filteredPlanets = k2ExoplanetsArchiveResult.planets.filter { it.id !in planetIds }
                planets.addAll(elements = filteredPlanets)
            }
        }

        val planetMap = planets.mergePlanets().groupBy { it.stellarHostId }
        val mergedStellarHosts = stellarHosts.mergeStellarHosts().apply {
            forEach { it.planets.addAll(elements = planetMap[it.id].orEmpty()) }
        }
        val derivedStellarHosts = DerivedData.derive(stellarHosts = mergedStellarHosts)
        val derivedPlanets = derivedStellarHosts.map { it.planets }.flatten()

        val hostsFile = runCatching { saveFile(path = STELLAR_HOSTS_JSON, content = json.encodeToString(value = derivedStellarHosts.map { it.copy() })) }.getOrElse {
            Logger.error(tag = TAG, message = it.stackTraceToString())
            false
        }
        val planetsFile = runCatching { saveFile(path = PLANETS_JSON, content = json.encodeToString(value = derivedPlanets.map { it.copy() })) }.getOrElse {
            Logger.error(tag = TAG, message = it.stackTraceToString())
            false
        }
        Logger.info(tag = TAG, message = "Hosts file saved: $hostsFile\nPlanets file saved: $planetsFile")
    }

    /**
     *
     */
    private suspend fun getArchive(limit: Int = pageSize, apiCall: suspend (Int, Int) -> ExoplanetsResult): ExoplanetsResult {
        val stellarHosts = mutableListOf<StellarHost>()
        val planets = mutableListOf<Planet>()
        var offset = 0
        do {
            val hasMore = when (val result = apiCall(offset, limit)) {
                is ExoplanetsResult.Success -> {
                    stellarHosts.addAll(elements = result.stellarHosts)
                    planets.addAll(elements = result.planets)
                    offset += limit
                    result.stellarHosts.size >= limit || result.planets.size >= limit
                }

                is ExoplanetsResult.Error -> return result
            }
        } while (hasMore)
        return ExoplanetsResult.Success(stellarHosts = stellarHosts, planets = planets)
    }

    /**
     * Get data from DOI 10.26133/NEA40.
     */
    private suspend fun getStellarHostsArchive(offset: Int, limit: Int): ExoplanetsResult {
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

        return when (val response = httpClient.getStream<StellarHostJson>(path = EXOPLANET_ARCHIVE_URL, queryMap = queryMap) { timeout { setTimeout(timeout = timeout) } }) {
            is Result.Error<StellarHostJson> -> ExoplanetsResult.Error(error = response.error)
            is Result.Success<StellarHostJson> -> ExoplanetsResult.Success(
                stellarHosts = response.list.map { it.toStellarHost() },
                planets = emptyList()
            )
        }
    }

    /**
     * Get data from DOI 10.26133/NEA13.
     */
    private suspend fun getExoplanetsArchive(offset: Int, limit: Int): ExoplanetsResult {
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
        return when (val response = httpClient.getStream<ExoplanetJson>(path = EXOPLANET_ARCHIVE_URL, queryMap = queryMap) { timeout { setTimeout(timeout = timeout) } }) {
            is Result.Error<ExoplanetJson> -> ExoplanetsResult.Error(error = response.error)
            is Result.Success<ExoplanetJson> -> ExoplanetsResult.Success(
                stellarHosts = response.list.map { it.toStellarHost() },
                planets = response.list.map { it.toPlanet() }
            )
        }
    }

    /**
     * Get data from DOI 10.26133/NEA19.
     */
    private suspend fun getK2ExoplanetsArchive(offset: Int, limit: Int): ExoplanetsResult {
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
        return when (val response = httpClient.getStream<ExoplanetJson>(path = EXOPLANET_ARCHIVE_URL, queryMap = queryMap) { timeout { setTimeout(timeout = timeout) } }) {
            is Result.Error<ExoplanetJson> -> ExoplanetsResult.Error(error = response.error)
            is Result.Success<ExoplanetJson> -> ExoplanetsResult.Success(
                stellarHosts = response.list.map { it.toStellarHost() },
                planets = response.list.map { it.toPlanet() }
            )
        }
    }

    private fun StellarHostJson.toStellarHost(): StellarHost =
        StellarHost(
            id = stellarHostName.toSnakeCase(),
            name = stellarHostName.toExpandedName(),
            systemName = stellarHostSystemName?.toExpandedName(),
            spectralType = stellarHostSpectralType
                ?.replace(regex = "\\s".toRegex(), replacement = "")
                ?.uppercase(),
            effectiveTemperature = stellarHostEffectiveTemperature,
            radius = stellarHostRadius,
            mass = stellarHostMass,
            metallicity = stellarHostMetallicity,
            luminosity = stellarHostLuminosity,
            gravity = stellarHostGravity?.stellarHostGravityToSunGravity(),
            age = stellarHostAge,
            density = stellarHostDensity,
            rotationalVelocity = stellarHostRotationalVelocity,
            rotationalPeriod = stellarHostRotationalPeriod,
            distance = stellarHostDistance?.parsecsToLightYears(),
            ra = stellarHostRa,
            dec = stellarHostDec
        )

    private fun ExoplanetJson.toStellarHost(): StellarHost =
        StellarHost(
            id = stellarHostName.toSnakeCase(),
            name = stellarHostName.toExpandedName(),
            systemName = null, // Should be fetched from Stellar Hosts
            spectralType = stellarHostSpectralType
                ?.replace(regex = "\\s".toRegex(), replacement = "")
                ?.uppercase(),
            effectiveTemperature = stellarHostEffectiveTemperature,
            radius = stellarHostRadius,
            mass = stellarHostMass,
            metallicity = stellarHostMetallicity,
            luminosity = stellarHostLuminosity,
            gravity = stellarHostGravity?.stellarHostGravityToSunGravity(),
            age = stellarHostAge,
            density = stellarHostDensity,
            rotationalVelocity = stellarHostRotationalVelocity,
            rotationalPeriod = stellarHostRotationalPeriod,
            distance = stellarHostDistance?.parsecsToLightYears(),
            ra = stellarHostRa,
            dec = stellarHostDec
        )

    private fun ExoplanetJson.toPlanet(): Planet =
        Planet(
            id = planetName.toSnakeCase(),
            name = planetName.toExpandedName(),
            stellarHostId = stellarHostName.toSnakeCase(),
            status = when (planetStatus?.lowercase()) {
                null, PlanetStatus.CONFIRMED.name.lowercase() -> PlanetStatus.CONFIRMED
                PlanetStatus.CANDIDATE.name.lowercase() -> PlanetStatus.CANDIDATE
                else -> PlanetStatus.FALSE
            },
            orbitalPeriod = planetOrbitalPeriod,
            orbitAxis = planetOrbitAxis,
            radius = planetRadius,
            mass = planetMass,
            density = planetDensity,
            eccentricity = planetEccentricity,
            insolationFlux = planetInsolationFlux,
            equilibriumTemperature = planetEquilibriumTemperature,
            occultationDepth = planetOccultationDepth,
            inclination = planetInclination,
            obliquity = planetObliquity ?: planetProjectedObliquity,
        )

    private fun List<StellarHost>.mergeStellarHosts(): List<StellarHost> =
        groupBy { it.id }.mapNotNull { (id, group) ->
            StellarHost(
                id = id,
                name = group.map { it.name }.firstOrNull().orEmpty(),
                systemName = group.mapNotNull { it.systemName }.ifEmpty { null }?.firstOrNull(),
                spectralType = group.mapNotNull { it.spectralType }.ifEmpty { null }?.firstOrNull(),
                effectiveTemperature = group.mapNotNull { it.effectiveTemperature }.ifEmpty { null }?.average(),
                radius = group.mapNotNull { it.radius }.ifEmpty { null }?.average(),
                mass = group.mapNotNull { it.mass }.ifEmpty { null }?.average(),
                metallicity = group.mapNotNull { it.metallicity }.ifEmpty { null }?.average(),
                luminosity = group.mapNotNull { it.luminosity }.ifEmpty { null }?.average(),
                gravity = group.mapNotNull { it.gravity }.ifEmpty { null }?.average(),
                age = group.mapNotNull { it.age }.ifEmpty { null }?.average(),
                density = group.mapNotNull { it.density }.ifEmpty { null }?.average(),
                rotationalVelocity = group.mapNotNull { it.rotationalVelocity }.ifEmpty { null }?.average(),
                rotationalPeriod = group.mapNotNull { it.rotationalPeriod }.ifEmpty { null }?.average(),
                distance = group.mapNotNull { it.distance }.ifEmpty { null }?.average(),
                ra = group.mapNotNull { it.ra }.ifEmpty { null }?.average(),
                dec = group.mapNotNull { it.dec }.ifEmpty { null }?.average()
            )
        }

    private fun List<Planet>.mergePlanets(): List<Planet> =
        groupBy { it.id }.mapNotNull { (id, group) ->
            Planet(
                id = id,
                name = group.map { it.name }.firstOrNull().orEmpty(),
                stellarHostId = group.firstNotNullOf { it.stellarHostId },
                status = (group.find { it.status == PlanetStatus.CONFIRMED }
                    ?: group.find { it.status == PlanetStatus.CANDIDATE })?.status
                    ?: PlanetStatus.FALSE,
                orbitalPeriod = group.mapNotNull { it.orbitalPeriod }.ifEmpty { null }?.average(),
                orbitAxis = group.mapNotNull { it.orbitAxis }.ifEmpty { null }?.average(),
                radius = group.mapNotNull { it.radius }.ifEmpty { null }?.average(),
                mass = group.mapNotNull { it.mass }.ifEmpty { null }?.average(),
                density = group.mapNotNull { it.density }.ifEmpty { null }?.average(),
                eccentricity = group.mapNotNull { it.eccentricity }.ifEmpty { null }?.average(),
                insolationFlux = group.mapNotNull { it.insolationFlux }.ifEmpty { null }?.average(),
                equilibriumTemperature = group.mapNotNull { it.equilibriumTemperature }.ifEmpty { null }?.average(),
                occultationDepth = group.mapNotNull { it.occultationDepth }.ifEmpty { null }?.average(),
                inclination = group.mapNotNull { it.inclination }.ifEmpty { null }?.average(),
                obliquity = group.mapNotNull { it.obliquity }.ifEmpty { null }?.average(),
            )
        }

    private val greekAbbreviations = mapOf(
        "Alf" to "Alpha",
        "Bet" to "Beta",
        "Gam" to "Gamma",
        "Del" to "Delta",
        "Eps" to "Epsilon",
        "Zet" to "Zeta",
        "Eta" to "Eta",
        "The" to "Theta",
        "Iot" to "Iota",
        "Kap" to "Kappa",
        "Lam" to "Lambda",
        "Mu" to "Mu",
        "Nu" to "Nu",
        "Xi" to "Xi",
        "Omi" to "Omicron",
        "Pi" to "Pi",
        "Rho" to "Rho",
        "Sig" to "Sigma",
        "Tau" to "Tau",
        "Ups" to "Upsilon",
        "Phi" to "Phi",
        "Chi" to "Chi",
        "Psi" to "Psi",
        "Ome" to "Omega"
    )
    private val latinAbbreviations = mapOf(
        "And" to "Andromedae",
        "Ant" to "Antliae",
        "Aps" to "Apodis",
        "Aqr" to "Aquarii",
        "Aql" to "Aquilae",
        "Ara" to "Arae",
        "Ari" to "Arietis",
        "Aur" to "Aurigae",
        "Boo" to "Bootis",
        "Cae" to "Caeli",
        "Cam" to "Camelopardalis",
        "Cnc" to "Cancri",
        "CVn" to "Canum Venaticorum",
        "CMa" to "Canis Majoris",
        "CMi" to "Canis Minoris",
        "Cap" to "Capricorni",
        "Car" to "Carinae",
        "Cas" to "Cassiopeiae",
        "Cen" to "Centauri",
        "Cep" to "Cephei",
        "Cet" to "Ceti",
        "Cha" to "Chamaeleontis",
        "Cir" to "Circini",
        "Col" to "Columbae",
        "Com" to "Comae Berenices",
        "CrA" to "Coronae Australis",
        "CrB" to "Coronae Borealis",
        "Crv" to "Corvi",
        "Crt" to "Crateris",
        "Cru" to "Crucis",
        "Cyg" to "Cygni",
        "Del" to "Delphini",
        "Dor" to "Doradus",
        "Dra" to "Draconis",
        "Equ" to "Equulei",
        "Eri" to "Eridani",
        "For" to "Fornacis",
        "Gem" to "Geminorum",
        "Gru" to "Gruis",
        "Her" to "Herculis",
        "Hor" to "Horologii",
        "Hya" to "Hydrae",
        "Hyi" to "Hydri",
        "Ind" to "Indi",
        "Lac" to "Lacertae",
        "Leo" to "Leonis",
        "LMi" to "Leonis Minoris",
        "Lep" to "Leporis",
        "Lib" to "Librae",
        "Lup" to "Lupi",
        "Lyn" to "Lyncis",
        "Lyr" to "Lyrae",
        "Men" to "Mensae",
        "Mic" to "Microscopii",
        "Mon" to "Monocerotis",
        "Mus" to "Muscae",
        "Nor" to "Normae",
        "Oct" to "Octantis",
        "Oph" to "Ophiuchi",
        "Ori" to "Orionis",
        "Pav" to "Pavonis",
        "Peg" to "Pegasi",
        "Per" to "Persei",
        "Phe" to "Phoenicis",
        "Pic" to "Pictoris",
        "Psc" to "Piscium",
        "PsA" to "Piscis Austrini",
        "Pup" to "Puppis",
        "Pyx" to "Pyxidis",
        "Ret" to "Reticuli",
        "Sge" to "Sagittae",
        "Sgr" to "Sagittarii",
        "Sco" to "Scorpii",
        "Scl" to "Sculptoris",
        "Sct" to "Scuti",
        "Ser" to "Serpentis",
        "Sex" to "Sextantis",
        "Tau" to "Tauri",
        "Tel" to "Telescopii",
        "Tri" to "Trianguli",
        "TrA" to "Trianguli Australis",
        "Tuc" to "Tucanae",
        "UMa" to "Ursae Majoris",
        "UMi" to "Ursae Minoris",
        "Vel" to "Velorum",
        "Vir" to "Virginis",
        "Vol" to "Volantis",
        "Vul" to "Vulpeculae"
    )
    private val allAbbreviations = (greekAbbreviations + latinAbbreviations).mapKeys { it.key.lowercase() }
    private val allAbbreviationsPattern =
        "\\b(${allAbbreviations.keys.joinToString(separator = "|")})\\b".toRegex(option = RegexOption.IGNORE_CASE)

    private fun String.toExpandedName() = allAbbreviationsPattern.replace(input = replace(oldValue = "_", newValue = " ")) { matchResult ->
        val key = matchResult.value.lowercase()
        allAbbreviations[key] ?: matchResult.value
    }

    private fun String.toSnakeCase(): String =
        lowercase().replace(oldValue = " ", newValue = "_").replace(oldValue = "-", newValue = "_")

    companion object {
        private const val TAG = "Archive"
    }
}
