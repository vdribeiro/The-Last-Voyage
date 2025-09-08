package com.hybris.tlv.usecase.sync

import com.hybris.tlv.http.EXOPLANET_ARCHIVE_URL
import com.hybris.tlv.http.QueryMap
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.json
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.storage.saveFile
import com.hybris.tlv.usecase.space.formula.DerivedData
import com.hybris.tlv.usecase.space.formula.parsecsToLightYears
import com.hybris.tlv.usecase.space.formula.stellarHostGravityToSunGravity
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.sync.model.ExoplanetJson
import com.hybris.tlv.usecase.sync.model.ExoplanetsResult
import com.hybris.tlv.usecase.sync.model.StellarHostJson
import com.hybris.tlv.usecase.sync.model.SyncResult
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class SpaceSync(
    private val httpClient: HttpClient
) {

    fun getArchive(): Flow<SyncResult> = flow {
        val totalOperations = 6f
        emit(value = SyncResult.Loading(progress = 0f, total = totalOperations))
        val stellarHosts = loadFromJson<StellarHost>(path = "files/solarsystem.json").toMutableList()
        val planets = loadFromJson<Planet>(path = "files/solarplanets.json").toMutableList()

        emit(value = SyncResult.Loading(progress = 1f, total = totalOperations))
        when (val stellarHostsArchiveResult = getArchive { getStellarHostsArchive(queryMap = it) }) {
            is ExoplanetsResult.Error -> emit(value = SyncResult.Error(error = stellarHostsArchiveResult.error))
            is ExoplanetsResult.Success -> stellarHosts.addAll(elements = stellarHostsArchiveResult.stellarHosts)
        }

        emit(value = SyncResult.Loading(progress = 2f, total = totalOperations))
        when (val exoplanetsArchiveResult = getArchive { getExoplanetsArchive(queryMap = it) }) {
            is ExoplanetsResult.Error -> emit(value = SyncResult.Error(error = exoplanetsArchiveResult.error))
            is ExoplanetsResult.Success -> {
                val stellarHostIds = stellarHosts.map { it.id }
                val filteredStellarHosts = exoplanetsArchiveResult.stellarHosts.filter { it.id !in stellarHostIds }
                stellarHosts.addAll(elements = filteredStellarHosts)
                planets.addAll(elements = exoplanetsArchiveResult.planets)
            }
        }

        emit(value = SyncResult.Loading(progress = 3f, total = totalOperations))
        when (val k2ExoplanetsArchiveResult = getArchive { getK2ExoplanetsArchive(queryMap = it) }) {
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

    private suspend fun getStellarHostsArchive(queryMap: QueryMap): ExoplanetsResult {
        val offset = queryMap.offset ?: 0
        val limit = queryMap.limit ?: Long.MAX_VALUE
        val query = "select+*+from+(+select+t.*,rownum+as+rn+from+(+select+" +
                "${StellarHostJson.Companion.STELLAR_HOST_NAME}," +
                "${StellarHostJson.Companion.STELLAR_HOST_SYSTEM_NAME}," +
                "${StellarHostJson.Companion.STELLAR_HOST_SPECTRAL_TYPE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_TEMPERATURE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_RADIUS}," +
                "${StellarHostJson.Companion.STELLAR_HOST_MASS}," +
                "${StellarHostJson.Companion.STELLAR_HOST_METALLICITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_LUMINOSITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_GRAVITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_AGE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_DENSITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_ROTATIONAL_VELOCITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_ROTATIONAL_PERIOD}," +
                "${StellarHostJson.Companion.STELLAR_HOST_DISTANCE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_RA}," +
                StellarHostJson.Companion.STELLAR_HOST_DEC +
                "+from+stellarhosts" +
                "+order+by+${StellarHostJson.Companion.STELLAR_HOST_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val queryMap = QueryMap().apply {
            set(key = "query", value = query)
            set(key = "format", value = "json")
        }
        return when (val response = httpClient.getStream<StellarHostJson>(path = EXOPLANET_ARCHIVE_URL, queryMap = queryMap)) {
            is Result.Error<StellarHostJson> -> ExoplanetsResult.Error(error = response.error)
            is Result.Success<StellarHostJson> -> ExoplanetsResult.Success(
                stellarHosts = response.list.map { it.toStellarHost() },
                planets = emptyList()
            )
        }
    }

    private suspend fun getExoplanetsArchive(queryMap: QueryMap): ExoplanetsResult {
        val offset = queryMap.offset ?: 0
        val limit = queryMap.limit ?: Long.MAX_VALUE
        val query = "select+*+from+(+select+t.*,rownum+as+rn+from+(+select+" +
                "${StellarHostJson.Companion.STELLAR_HOST_NAME}," +
                "${StellarHostJson.Companion.STELLAR_HOST_SPECTRAL_TYPE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_TEMPERATURE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_RADIUS}," +
                "${StellarHostJson.Companion.STELLAR_HOST_MASS}," +
                "${StellarHostJson.Companion.STELLAR_HOST_METALLICITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_LUMINOSITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_GRAVITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_AGE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_DENSITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_ROTATIONAL_VELOCITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_ROTATIONAL_PERIOD}," +
                "${StellarHostJson.Companion.STELLAR_HOST_DISTANCE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_RA}," +
                "${StellarHostJson.Companion.STELLAR_HOST_DEC}," +
                "${ExoplanetJson.Companion.PLANET_NAME}," +
                "${ExoplanetJson.Companion.PLANET_ORBITAL_PERIOD}," +
                "${ExoplanetJson.Companion.PLANET_ORBIT_AXIS}," +
                "${ExoplanetJson.Companion.PLANET_RADIUS}," +
                "${ExoplanetJson.Companion.PLANET_MASS}," +
                "${ExoplanetJson.Companion.PLANET_DENSITY}," +
                "${ExoplanetJson.Companion.PLANET_ECCENTRICITY}," +
                "${ExoplanetJson.Companion.PLANET_INSOLATION_FLUX}," +
                "${ExoplanetJson.Companion.PLANET_EQUILIBRIUM_TEMPERATURE}," +
                "${ExoplanetJson.Companion.PLANET_OCCULTATION_DEPTH}," +
                "${ExoplanetJson.Companion.PLANET_INCLINATION}," +
                "${ExoplanetJson.Companion.PLANET_OBLIQUITY}," +
                ExoplanetJson.Companion.PLANET_PROJECTED_OBLIQUITY +
                "+from+pscomppars" +
                "+order+by+${ExoplanetJson.Companion.PLANET_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val queryMap = QueryMap().apply {
            set(key = "query", value = query)
            set(key = "format", value = "json")
        }
        return when (val response = httpClient.getStream<ExoplanetJson>(path = EXOPLANET_ARCHIVE_URL, queryMap = queryMap)) {
            is Result.Error<ExoplanetJson> -> ExoplanetsResult.Error(error = response.error)
            is Result.Success<ExoplanetJson> -> ExoplanetsResult.Success(
                stellarHosts = response.list.map { it.toStellarHost() },
                planets = response.list.map { it.toPlanet() }
            )
        }
    }

    private suspend fun getK2ExoplanetsArchive(queryMap: QueryMap): ExoplanetsResult {
        val offset = queryMap.offset ?: 0
        val limit = queryMap.limit ?: Long.MAX_VALUE
        val query = "select+*+from+(+select+t.*,rownum+as+rn+from+(+select+" +
                "${StellarHostJson.Companion.STELLAR_HOST_NAME}," +
                "${StellarHostJson.Companion.STELLAR_HOST_SPECTRAL_TYPE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_TEMPERATURE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_RADIUS}," +
                "${StellarHostJson.Companion.STELLAR_HOST_MASS}," +
                "${StellarHostJson.Companion.STELLAR_HOST_METALLICITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_LUMINOSITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_GRAVITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_AGE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_DENSITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_ROTATIONAL_VELOCITY}," +
                "${StellarHostJson.Companion.STELLAR_HOST_ROTATIONAL_PERIOD}," +
                "${StellarHostJson.Companion.STELLAR_HOST_DISTANCE}," +
                "${StellarHostJson.Companion.STELLAR_HOST_RA}," +
                "${StellarHostJson.Companion.STELLAR_HOST_DEC}," +
                "${ExoplanetJson.Companion.PLANET_NAME}," +
                "${ExoplanetJson.Companion.PLANET_STATUS}," +
                "${ExoplanetJson.Companion.PLANET_ORBITAL_PERIOD}," +
                "${ExoplanetJson.Companion.PLANET_ORBIT_AXIS}," +
                "${ExoplanetJson.Companion.PLANET_RADIUS}," +
                "${ExoplanetJson.Companion.PLANET_MASS}," +
                "${ExoplanetJson.Companion.PLANET_DENSITY}," +
                "${ExoplanetJson.Companion.PLANET_ECCENTRICITY}," +
                "${ExoplanetJson.Companion.PLANET_INSOLATION_FLUX}," +
                "${ExoplanetJson.Companion.PLANET_EQUILIBRIUM_TEMPERATURE}," +
                "${ExoplanetJson.Companion.PLANET_OCCULTATION_DEPTH}," +
                "${ExoplanetJson.Companion.PLANET_INCLINATION}," +
                "${ExoplanetJson.Companion.PLANET_OBLIQUITY}," +
                ExoplanetJson.Companion.PLANET_PROJECTED_OBLIQUITY +
                "+from+k2pandc" +
                "+order+by+${ExoplanetJson.Companion.PLANET_NAME}+asc" +
                "+)+t+where+rownum+<=+${offset + limit}+)+where+rn+>+${offset}"
        val queryMap = QueryMap().apply {
            set(key = "query", value = query)
            set(key = "format", value = "json")
        }
        return when (val response = httpClient.getStream<ExoplanetJson>(path = EXOPLANET_ARCHIVE_URL, queryMap = queryMap)) {
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
}