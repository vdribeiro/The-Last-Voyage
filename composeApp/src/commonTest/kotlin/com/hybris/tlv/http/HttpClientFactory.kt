package com.hybris.tlv.http

import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
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
        val mockEngine = MockEngine.Companion { request ->
            when {
                request.method == HttpMethod.Companion.Get -> {
                    val path = request.url.encodedPath
                    val parameters = request.url.parameters.toString()
                    when (path) {
                        EXOPLANET_ARCHIVE_URL -> when {
                            parameters.contains(other = "from stellarhosts") -> {
                                respond(
                                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
                                    content = json.encodeToString(value = stellarHosts.map { it.toStellarHostJson() }),
                                )
                            }

                            parameters.contains(other = "from pscomppars") || parameters.contains(other = "from k2pandc") -> {
                                val stellarHostsMap = stellarHosts.associateBy { it.id }
                                val exoplanets = planets.mapNotNull {
                                    val stellarHost = stellarHostsMap[it.stellarHostId] ?: return@mapNotNull null
                                    it.toExoplanetJson(stellarHost = stellarHost)
                                }
                                respond(
                                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
                                    content = json.encodeToString(value = exoplanets),
                                )
                            }

                            else -> respondError(
                                status = HttpStatusCode.Companion.BadRequest,
                                content = "Resource query incorrect: ${request.url.encodedPath}"
                            )
                        }

                        else -> respondError(
                            status = HttpStatusCode.Companion.NotFound,
                            content = "Resource not found for path: ${request.url.encodedPath}"
                        )
                    }
                }

                else -> respondError(status = HttpStatusCode.Companion.BadRequest, content = "Method not found: ${request.method}")
            }
        }

        return HttpClient(engine = mockEngine) {
            setContentValidator()
        }
    }
}