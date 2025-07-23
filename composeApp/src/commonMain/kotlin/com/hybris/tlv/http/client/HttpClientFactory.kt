package com.hybris.tlv.http.client

import com.hybris.tlv.logger.Logger
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.usecase.space.mapper.toExoplanetJson
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object HttpClientFactory {

    private const val TAG = "Ktor"

    private val mockEngine = MockEngine { request ->
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

    fun getExoplanetMockHttpClient() = HttpClient(engine = mockEngine) {
        setRequestUrl(url = "exoplanetarchive.ipac.caltech.edu/TAP")
        setContentValidator()
    }

    fun getExoplanetHttpClient() = HttpClient {
        setLogging()
        setTimeout(timeout = 60_000L * 5)
        setCache()
        setRequestUrl(url = "exoplanetarchive.ipac.caltech.edu/TAP")
        setContentValidator()
        setContentEncoding(compressionQuality = 0.9F)
    }

    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setLogging() {
        install(plugin = Logging) {
            logger = object: io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    Logger.debug(tag = TAG, message = message)
                }
            }
            level = LogLevel.INFO
        }
    }

    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setRequestUrl(url: String) {
        install(plugin = DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = url
            }
        }
    }

    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setTimeout(timeout: Long) {
        install(plugin = HttpTimeout) { setTimeout(timeout) }
    }

    private fun HttpTimeoutConfig.setTimeout(timeout: Long) {
        requestTimeoutMillis = timeout
        connectTimeoutMillis = timeout
        socketTimeoutMillis = timeout
    }

    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setCache() {
        install(plugin = HttpCache)
    }

    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setContentEncoding(compressionQuality: Float) {
        install(plugin = ContentEncoding) { gzip(quality = compressionQuality) }
    }

    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setContentValidator() {
        install(plugin = ContentNegotiation) { json(json = json) }
    }
}

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}
