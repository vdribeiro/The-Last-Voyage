package com.hybris.tlv.http

import com.hybris.tlv.config.Configs
import com.hybris.tlv.mock.achievements
import com.hybris.tlv.mock.catastrophes
import com.hybris.tlv.mock.credits
import com.hybris.tlv.mock.engines
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.mock.translations
import com.hybris.tlv.serializer.json
import com.hybris.tlv.usecase.space.formula.Constants.PARSEC
import com.hybris.tlv.usecase.space.formula.Constants.SUN_SURFACE_GRAVITY
import com.hybris.tlv.usecase.space.mapper.roundTo
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.remote.json.ExoplanetJson
import com.hybris.tlv.usecase.space.remote.json.StellarHostJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.math.log10

internal object HttpClientFactory {

    fun buildHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            when {
                request.method == HttpMethod.Get -> {
                    val path = request.url.toString()
                    val parameters = request.url.parameters.toString()
                    when {
                        path.startsWith(prefix = EXOPLANET_ARCHIVE_URL) -> when {
                            parameters.contains(other = "from stellarhosts") -> {
                                respond(content = json.encodeToString(value = stellarHosts.map { it.toStellarHostJson() }))
                            }

                            parameters.contains(other = "from pscomppars") || parameters.contains(other = "from k2pandc") -> {
                                val stellarHostsMap = stellarHosts.associateBy { it.id }
                                val exoplanets = planets.mapNotNull {
                                    val stellarHost = stellarHostsMap[it.stellarHostId] ?: return@mapNotNull null
                                    it.toExoplanetJson(stellarHost = stellarHost)
                                }
                                respond(content = json.encodeToString(value = exoplanets))
                            }

                            else -> respondError(
                                status = HttpStatusCode.BadRequest,
                                content = "Resource query incorrect: ${request.url.encodedPath}"
                            )
                        }

                        path.startsWith(prefix = CONFIGS_URL) -> respond(content = json.encodeToString(value = listOf(Configs())))
                        path.startsWith(prefix = TRANSLATIONS_URL) -> respond(content = json.encodeToString(value = translations))
                        path.startsWith(prefix = CATASTROPHES_URL) -> respond(content = json.encodeToString(value = catastrophes))
                        path.startsWith(prefix = ENGINES_URL) -> respond(content = json.encodeToString(value = engines))
                        path.startsWith(prefix = STELLAR_HOSTS_URL) -> respond(content = json.encodeToString(value = stellarHosts))
                        path.startsWith(prefix = PLANETS_URL) -> respond(content = json.encodeToString(value = planets))
                        path.startsWith(prefix = EVENTS_URL) -> respond(content = json.encodeToString(value = events))
                        path.startsWith(prefix = ACHIEVEMENTS_URL) -> respond(content = json.encodeToString(value = achievements))
                        path.startsWith(prefix = CREDITS_URL) -> respond(content = json.encodeToString(value = credits))

                        else -> respondError(
                            status = HttpStatusCode.NotFound,
                            content = "Resource not found for path: ${request.url.encodedPath}"
                        )
                    }
                }

                else -> respondError(status = HttpStatusCode.BadRequest, content = "Method not found: ${request.method}")
            }
        }

        return HttpClient(engine = mockEngine) {
            setContentValidator()
        }
    }

    fun buildErrorHttpClient(): HttpClient = HttpClient(engine = MockEngine {
        respondError(status = HttpStatusCode.InternalServerError)
    })

    private fun Double.sunGravityToStellarHostGravity(): Double = (log10(x = this) + SUN_SURFACE_GRAVITY).roundTo(decimalPlaces = 7)
    private fun Double.lightYearsToParsecs(): Double = this / PARSEC

    private fun StellarHost.toStellarHostJson(): StellarHostJson =
        StellarHostJson(
            stellarHostName = name,
            stellarHostSystemName = systemName,
            stellarHostSpectralType = spectralType,
            stellarHostEffectiveTemperature = effectiveTemperature,
            stellarHostRadius = radius,
            stellarHostMass = mass,
            stellarHostMetallicity = metallicity,
            stellarHostLuminosity = luminosity,
            stellarHostGravity = gravity?.sunGravityToStellarHostGravity(),
            stellarHostAge = age,
            stellarHostDensity = density,
            stellarHostRotationalVelocity = rotationalVelocity,
            stellarHostRotationalPeriod = rotationalPeriod,
            stellarHostDistance = distance?.lightYearsToParsecs(),
            stellarHostRa = ra,
            stellarHostDec = dec
        )

    private fun Planet.toExoplanetJson(stellarHost: StellarHost): ExoplanetJson? =
        ExoplanetJson(
            stellarHostName = stellarHost.name,
            stellarHostSpectralType = stellarHost.spectralType,
            stellarHostEffectiveTemperature = stellarHost.effectiveTemperature,
            stellarHostRadius = stellarHost.radius,
            stellarHostMass = stellarHost.mass,
            stellarHostMetallicity = stellarHost.metallicity,
            stellarHostLuminosity = stellarHost.luminosity,
            stellarHostGravity = stellarHost.gravity?.sunGravityToStellarHostGravity(),
            stellarHostAge = stellarHost.age,
            stellarHostDensity = stellarHost.density,
            stellarHostRotationalVelocity = stellarHost.rotationalVelocity,
            stellarHostRotationalPeriod = stellarHost.rotationalPeriod,
            stellarHostDistance = stellarHost.distance?.lightYearsToParsecs(),
            stellarHostRa = stellarHost.ra,
            stellarHostDec = stellarHost.dec,
            planetName = name,
            planetStatus = status.name,
            planetOrbitalPeriod = orbitalPeriod,
            planetOrbitAxis = orbitAxis,
            planetRadius = radius,
            planetMass = mass,
            planetDensity = density,
            planetEccentricity = eccentricity,
            planetInsolationFlux = insolationFlux,
            planetEquilibriumTemperature = equilibriumTemperature,
            planetOccultationDepth = occultationDepth,
            planetInclination = inclination,
            planetObliquity = obliquity,
            planetProjectedObliquity = obliquity
        )
}
