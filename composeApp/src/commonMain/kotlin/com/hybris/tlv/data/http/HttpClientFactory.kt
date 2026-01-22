package com.hybris.tlv.data.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.compression.ContentEncodingConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiationConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingConfig
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.serializer.json
import com.hybris.tlv.domain.flag.FeatureFlags.flags

/**
 * A factory for creating and configuring the [HttpClient] instance with the necessary plugins,
 * given an optional [HttpClientEngine] to use for the client. If null, a default engine is used.
 */
internal class HttpClientFactory(engine: HttpClientEngine?) {

    /**
     * The configured [HttpClient] instance.
     */
    val httpClient: HttpClient = when (engine) {
        null -> HttpClient { install() }
        else -> HttpClient(engine = engine) { install() }
    }

    /**
     * Installs and configures the necessary plugins for the [HttpClient].
     */
    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.install() {
        install(plugin = Logging) { configure() }
        install(plugin = HttpTimeout) { configure() }
        install(plugin = HttpCache) { configure() }
        install(plugin = ContentNegotiation) { configure() }
        install(plugin = ContentEncoding) { configure() }
        defaultRequest { configure() }
    }

    private fun LoggingConfig.configure() {
        logger = object: Logger {
            override fun log(message: String) {
                Telemetry.info(tag = TAG, message = message)
            }
        }
        level = if (flags.devMode) LogLevel.ALL else LogLevel.INFO
    }

    private fun HttpTimeoutConfig.configure() {
        connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS
        socketTimeoutMillis = SOCKET_TIMEOUT_MILLIS
        requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
    }

    private fun HttpCache.Config.configure() {
        publicStorage(storage = CacheStorage.Unlimited())
        privateStorage(storage = CacheStorage.Unlimited())
        isShared = false
    }

    private fun ContentNegotiationConfig.configure() {
        json(json = json, contentType = ContentType.Application.Json)
        register(contentType = ContentType.Text.Plain, converter = KotlinxSerializationConverter(format = json))
    }

    private fun ContentEncodingConfig.configure() {
        gzip()
    }

    private fun DefaultRequest.DefaultRequestBuilder.configure() {
        header(key = HttpHeaders.Accept, value = ContentType.Application.Json)
    }

    companion object {
        private const val TAG = "HttpClient"
        const val CONNECT_TIMEOUT_MILLIS = 10_000L
        const val SOCKET_TIMEOUT_MILLIS = 20_000L
        const val REQUEST_TIMEOUT_MILLIS = 60_000L
    }
}
