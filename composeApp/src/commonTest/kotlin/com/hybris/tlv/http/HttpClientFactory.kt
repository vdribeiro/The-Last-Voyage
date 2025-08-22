package com.hybris.tlv.http

import com.hybris.tlv.mock.achievements
import com.hybris.tlv.mock.catastrophes
import com.hybris.tlv.mock.credits
import com.hybris.tlv.mock.engines
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.mock.translations
import com.hybris.tlv.serializer.json
import com.hybris.tlv.usecase.space.mapper.toExoplanetJson
import com.hybris.tlv.usecase.space.mapper.toStellarHostJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

internal object HttpClientFactory {

    fun buildHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            when {
                request.method == HttpMethod.Get -> {
                    val path = request.url.encodedPath.drop(n = 1)
                    val parameters = request.url.parameters.toString()
                    when {
                        path.startsWith(prefix = EXOPLANET_ARCHIVE_URL) -> when {
                            parameters.contains(other = "from stellarhosts") -> {
                                respond(content = json.encodeToString(value = stellarHosts.map { it.toStellarHostJson() }),)
                            }

                            parameters.contains(other = "from pscomppars") || parameters.contains(other = "from k2pandc") -> {
                                val stellarHostsMap = stellarHosts.associateBy { it.id }
                                val exoplanets = planets.mapNotNull {
                                    val stellarHost = stellarHostsMap[it.stellarHostId] ?: return@mapNotNull null
                                    it.toExoplanetJson(stellarHost = stellarHost)
                                }
                                respond(content = json.encodeToString(value = exoplanets),)
                            }

                            else -> respondError(
                                status = HttpStatusCode.BadRequest,
                                content = "Resource query incorrect: ${request.url.encodedPath}"
                            )
                        }
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
}