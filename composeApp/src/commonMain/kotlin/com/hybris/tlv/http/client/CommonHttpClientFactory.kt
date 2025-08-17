package com.hybris.tlv.http.client

import com.hybris.tlv.http.json.json
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.usecase.space.mapper.toExoplanetJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

internal class CommonHttpClientFactory: HttpClientFactory {

    override fun buildExoplanetHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            if (request.method == HttpMethod.Get && request.url.encodedPath.startsWith(prefix = "/sync")) {
                val stellarHostsMap = stellarHosts.associateBy { it.id }
                respond(
                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
                    content = json.encodeToString(value = planets.mapNotNull {
                        val stellarHost = stellarHostsMap[it.stellarHostId] ?: return@mapNotNull null
                        it.toExoplanetJson(stellarHost = stellarHost)
                    }),
                )
            } else respondError(status = HttpStatusCode.NotFound, content = "Resource not found for path: ${request.url.encodedPath}")
        }

        return HttpClient(engine = mockEngine) {
            setRequestUrl(url = EXOPLANET_ARCHIVE_URL)
            setContentValidator()
        }
    }
}
