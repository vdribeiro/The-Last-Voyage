package com.hybris.tlv.http

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import com.hybris.tlv.achievements
import com.hybris.tlv.catastrophes
import com.hybris.tlv.configs
import com.hybris.tlv.credits
import com.hybris.tlv.engines
import com.hybris.tlv.events
import com.hybris.tlv.planets
import com.hybris.tlv.serializer.encode
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.translations

internal object TestEngine {

    val mock = MockEngine { request ->
        when (request.method) {
            HttpMethod.Get -> {
                val path = request.url.toString()
                when {
                    path.startsWith(prefix = URL.Configs.path) -> respond(content = encode(value = listOf(configs)).orEmpty())
                    path.startsWith(prefix = URL.Translations.path) -> respond(content = encode(value = translations).orEmpty())
                    path.startsWith(prefix = URL.Catastrophes.path) -> respond(content = encode(value = catastrophes).orEmpty())
                    path.startsWith(prefix = URL.Engines.path) -> respond(content = encode(value = engines).orEmpty())
                    path.startsWith(prefix = URL.StellarHosts.path) -> respond(content = encode(value = stellarHosts).orEmpty())
                    path.startsWith(prefix = URL.Planets.path) -> respond(content = encode(value = planets).orEmpty())
                    path.startsWith(prefix = URL.Events.path) -> respond(content = encode(value = events).orEmpty())
                    path.startsWith(prefix = URL.Achievements.path) -> respond(content = encode(value = achievements).orEmpty())
                    path.startsWith(prefix = URL.Credits.path) -> respond(content = encode(value = credits).orEmpty())
                    path.startsWith(prefix = URL.ExoplanetArchive.path) -> respondArchive(request = request)
                    else -> respondError(status = HttpStatusCode.NotFound, content = "Resource not found for path: ${request.url.encodedPath}")
                }
            }

            else -> respondError(status = HttpStatusCode.BadRequest, content = "Method not found: ${request.method}")
        }
    }

    private fun MockRequestHandleScope.respondArchive(request: HttpRequestData): HttpResponseData {
        val parameters = request.url.parameters.toString()
        return when {
            parameters.contains(other = "from stellarhosts") -> respond(content = encode(value = stellarHosts.map { it.toStellarHostJson() }).orEmpty())
            parameters.contains(other = "from pscomppars") || parameters.contains(other = "from k2pandc") -> {
                val stellarHostsMap = stellarHosts.associateBy { it.id }
                val exoplanets = planets.mapNotNull {
                    val stellarHost = stellarHostsMap[it.stellarHostId] ?: return@mapNotNull null
                    it.toExoplanetJson(stellarHost = stellarHost)
                }
                respond(content = encode(value = exoplanets).orEmpty())
            }

            else -> respondError(status = HttpStatusCode.BadRequest, content = "Resource query incorrect: ${request.url.encodedPath}")
        }
    }
}
