package com.hybris.tlv.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingConfig
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
        followRedirects = false
        install(plugin = Logging) { configure() }
        install(plugin = HttpTimeout) { configure() }
        install(plugin = HttpCache)
        install(plugin = ContentNegotiation) { json(json = json) }
        install(plugin = ContentEncoding) { gzip(quality = 0.9F) }
    }

    private fun LoggingConfig.configure() {
        logger = object: Logger {
            override fun log(message: String) {
                Telemetry.info(tag = TAG, message = message)
            }
        }
        level = if (isDebug) LogLevel.ALL else LogLevel.INFO
    }

    private fun HttpTimeoutConfig.configure() {
        connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS
        socketTimeoutMillis = SOCKET_TIMEOUT_MILLIS
        requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
    }

    /**
     * The configured [HttpClient] instance.
     */
    val httpClient: HttpClient = when (engine) {
        null -> HttpClient { install() }
        else -> HttpClient(engine = engine) { install() }
    }

    companion object {
        private const val TAG = "HttpClient"
        private const val CONNECT_TIMEOUT_MILLIS = 10_000L
        private const val SOCKET_TIMEOUT_MILLIS = 20_000L
        private const val REQUEST_TIMEOUT_MILLIS = 60_000L
    }
}
