package com.hybris.tlv.data.http

import io.ktor.client.engine.HttpClientEngine
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
import com.hybris.tlv.data.serializer.encode
import com.hybris.tlv.domain.flag.FeatureFlags.flags
import com.hybris.tlv.domain.usecase.space.toExoplanetJson
import com.hybris.tlv.domain.usecase.space.toStellarHostJson
import com.hybris.tlv.test.FakeData

internal fun createHttpEngine(): HttpClientEngine = MockEngine { request ->
    val path = request.url.toString()
    when (request.method) {
        HttpMethod.Head -> when {
            path.startsWith(prefix = URL.Probe.path) -> respondMock(status = HttpStatusCode.NoContent)
            else -> respondMock(status = HttpStatusCode.NotFound, content = "Not found for path: ${request.url.encodedPath}")
        }

        HttpMethod.Get -> when {
            path.startsWith(prefix = URL.ExoplanetArchive.path) -> respondArchive(request = request)
            path.startsWith(prefix = URL.Configs.path) -> respondMock(content = encode(value = listOf(FakeData.configs)))
            path.startsWith(prefix = URL.Translations.path) -> respondMock(content = encode(value = FakeData.translations.get()))
            path.startsWith(prefix = URL.Catastrophes.path) -> respondMock(content = encode(value = FakeData.catastrophes.get()))
            path.startsWith(prefix = URL.Engines.path) -> respondMock(content = encode(value = FakeData.engines.get()))
            path.startsWith(prefix = URL.StellarHosts.path) -> respondMock(content = encode(value = FakeData.stellarHosts.get()))
            path.startsWith(prefix = URL.Planets.path) -> respondMock(content = encode(value = FakeData.planets.get()))
            path.startsWith(prefix = URL.Events.path) -> respondMock(content = encode(value = FakeData.events.get()))
            path.startsWith(prefix = URL.Achievements.path) -> respondMock(content = encode(value = FakeData.achievements.get()))
            path.startsWith(prefix = URL.Credits.path) -> respondMock(content = encode(value = FakeData.credits.get()))
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

private suspend fun MockRequestHandleScope.respondArchive(request: HttpRequestData): HttpResponseData {
    val parameters = request.url.parameters.toString()
    return when {
        parameters.contains(other = "FROM stellarhosts", ignoreCase = true) -> {
            val stellarHosts = FakeData.stellarHosts.get().map { it.toStellarHostJson() }
            respondMock(content = encode(value = stellarHosts))
        }

        parameters.contains(other = "FROM pscomppars", ignoreCase = true) || parameters.contains(other = "FROM k2pandc", ignoreCase = true) -> {
            val stellarHostsMap = FakeData.stellarHosts.get().associateBy { it.id }
            val exoplanets = FakeData.planets.get().mapNotNull {
                val stellarHost = stellarHostsMap[it.stellarHostId] ?: return@mapNotNull null
                it.toExoplanetJson(stellarHost = stellarHost)
            }
            respondMock(content = encode(value = exoplanets))
        }

        else -> respondError(status = HttpStatusCode.BadRequest, content = "Resource query incorrect: ${request.url.encodedPath}")
    }
}

internal fun isInternetAvailable(): Boolean = flags.http
