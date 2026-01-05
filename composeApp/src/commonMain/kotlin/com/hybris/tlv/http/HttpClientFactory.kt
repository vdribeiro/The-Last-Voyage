package com.hybris.tlv.http

import kotlinx.io.IOException
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import com.hybris.tlv.platform.isDebug
import com.hybris.tlv.serializer.json
import com.hybris.tlv.telemetry.Telemetry

/**
 * A factory for creating and configuring [HttpClient] instances with the necessary plugins, given an optional [HttpClientEngine] to use for the client. If null, a default engine is used.
 */
internal class HttpClientFactory(engine: HttpClientEngine?) {

    /**
     * Installs and configures the necessary plugins for the [HttpClient], namely logging, timeouts, caching, content negotiation, and compression.
     */
    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.install() {
        expectSuccess = true
        install(plugin = Logging) {
            logger = object: io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    Telemetry.info(tag = "Ktor", message = message)
                }
            }
            level = if (isDebug) LogLevel.ALL else LogLevel.INFO
        }
        install(plugin = HttpTimeout) {
            connectTimeoutMillis = 15_000L
            socketTimeoutMillis = 30_000L
            requestTimeoutMillis = 120_000L
        }
        install(plugin = HttpCache)
        install(plugin = ContentNegotiation) { json(json = json) }
        install(plugin = ContentEncoding) { gzip(quality = 0.9F) }
        install(plugin = HttpRequestRetry) {
            maxRetries = 3
            exponentialDelay()
            retryIf { _, response -> !response.status.isSuccess() }
            retryOnExceptionIf { _, cause ->
                cause is ConnectTimeoutException ||
                        cause is SocketTimeoutException ||
                        cause is HttpRequestTimeoutException ||
                        cause is IOException
            }
        }
    }

    /**
     * The configured [HttpClient] instance.
     */
    val httpClient: HttpClient = when (engine) {
        null -> HttpClient { install() }
        else -> HttpClient(engine = engine) { install() }
    }
}
