package com.hybris.tlv.data.http

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import com.hybris.tlv.achievements
import com.hybris.tlv.catastrophes
import com.hybris.tlv.configs
import com.hybris.tlv.credits
import com.hybris.tlv.data.serializer.encode
import com.hybris.tlv.engines
import com.hybris.tlv.events
import com.hybris.tlv.planets
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.translations

internal object TestEngine {

    val mock = MockEngine { request ->
        val path = request.url.toString()
        when (request.method) {
            HttpMethod.Head -> when {
                path.startsWith(prefix = URL.Probe.path) -> respondMock(status = HttpStatusCode.NoContent)
                else -> respondMock(status = HttpStatusCode.NotFound, content = "Not found for path: ${request.url.encodedPath}")
            }

            HttpMethod.Get -> when {
                path.startsWith(prefix = URL.ExoplanetArchive.path) -> respondArchive(request = request)
                path.startsWith(prefix = URL.Configs.path) -> respondMock(content = encode(value = listOf(configs)))
                path.startsWith(prefix = URL.Translations.path) -> respondMock(content = encode(value = translations))
                path.startsWith(prefix = URL.Catastrophes.path) -> respondMock(content = encode(value = catastrophes))
                path.startsWith(prefix = URL.Engines.path) -> respondMock(content = encode(value = engines))
                path.startsWith(prefix = URL.StellarHosts.path) -> respondMock(content = encode(value = stellarHosts))
                path.startsWith(prefix = URL.Planets.path) -> respondMock(content = encode(value = planets))
                path.startsWith(prefix = URL.Events.path) -> respondMock(content = encode(value = events))
                path.startsWith(prefix = URL.Achievements.path) -> respondMock(content = encode(value = achievements))
                path.startsWith(prefix = URL.Credits.path) -> respondMock(content = encode(value = credits))
                else -> respondMock(status = HttpStatusCode.NotFound, content = "Resource not found for path: ${request.url.encodedPath}")
            }

            else -> respondMock(status = HttpStatusCode.BadRequest, content = "Method not found: ${request.method}")
        }
    }

    private fun MockRequestHandleScope.respondMock(
        status: HttpStatusCode = HttpStatusCode.OK,
        content: String? = ""
    ): HttpResponseData = respond(
        content = content.orEmpty(),
        status = status,
        headers = headersOf(name = HttpHeaders.ContentType, value = ContentType.Application.Json.toString())
    )

    private fun MockRequestHandleScope.respondArchive(request: HttpRequestData): HttpResponseData {
        val parameters = request.url.parameters.toString()
        return when {
            parameters.contains(other = "from stellarhosts") -> respondMock(content = encode(value = stellarHosts.map { it.toStellarHostJson() }))
            parameters.contains(other = "from pscomppars") || parameters.contains(other = "from k2pandc") -> {
                val stellarHostsMap = stellarHosts.associateBy { it.id }
                val exoplanets = planets.mapNotNull {
                    val stellarHost = stellarHostsMap[it.stellarHostId] ?: return@mapNotNull null
                    it.toExoplanetJson(stellarHost = stellarHost)
                }
                respondMock(content = encode(value = exoplanets))
            }

            else -> respondError(status = HttpStatusCode.BadRequest, content = "Resource query incorrect: ${request.url.encodedPath}")
        }
    }
}
