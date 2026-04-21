package com.hybris.tlv.data.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.api.createClientPlugin
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
import com.hybris.tlv.data.http.HttpClientFactory.Companion.CONNECT_TIMEOUT_MILLIS
import com.hybris.tlv.data.http.HttpClientFactory.Companion.REQUEST_TIMEOUT_MILLIS
import com.hybris.tlv.data.http.HttpClientFactory.Companion.SOCKET_TIMEOUT_MILLIS
import com.hybris.tlv.data.serializer.json
import com.hybris.tlv.domain.flag.FeatureFlags.flags

/**
 * A centralized factory for creating and configuring the [HttpClient].
 * This factory ensures that all network requests follow a consistent set of rules for logging, timeouts, caching, and serialization.
 *
 * @property engine The underlying networking engine used to execute requests.
 */
internal class HttpClientFactory(engine: HttpClientEngine) {

    /**
     * The configured [HttpClient] instance.
     * This instance should be treated as a singleton and shared across the application to maximize the efficiency of connection pooling and caching.
     */
    val httpClient: HttpClient = HttpClient(engine = engine) { install() }

    /**
     * Installs and configures the necessary plugins for the [HttpClient].
     */
    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.install() {
        install(plugin = NetworkValidator)
        install(plugin = Logging) { configure() }
        install(plugin = HttpTimeout) { configure() }
        install(plugin = HttpCache) { configure() }
        install(plugin = ContentNegotiation) { configure() }
        install(plugin = ContentEncoding) { configure() }
        defaultRequest { configure() }
    }

    /**
     * - **Feature Gating:** Checks a feature flag to see if networking is globally disabled.
     * - **Connectivity Check:** Verifies network availability via [isInternetAvailable].
     */
    @Suppress("PrivatePropertyName")
    private val NetworkValidator = createClientPlugin(name = "NetworkValidator") {
        onRequest { _, _ ->
            if (!flags.http) throw Throwable(message = "Network disabled")
            if (!isInternetAvailable()) throw Throwable(message = "No internet connection available")
        }
    }

    /**
     * Configures the [Logging] plugin to route Ktor network logs to the [Telemetry] system.
     */
    private fun LoggingConfig.configure() {
        logger = object: Logger {
            override fun log(message: String) {
                Telemetry.info(tag = TAG, message = message)
            }
        }
        level = LogLevel.INFO
    }

    /**
     * Configures the [HttpTimeout] settings.
     * Ensures that the client doesn't hang indefinitely on poor connections by enforcing strict [CONNECT_TIMEOUT_MILLIS], [SOCKET_TIMEOUT_MILLIS] and [REQUEST_TIMEOUT_MILLIS].
     */
    private fun HttpTimeoutConfig.configure() {
        connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS
        socketTimeoutMillis = SOCKET_TIMEOUT_MILLIS
        requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
    }

    /**
     * Configures the [HttpCache] with unlimited persistence for both public and private storage.
     */
    private fun HttpCache.Config.configure() {
        publicStorage(storage = CacheStorage.Unlimited())
        privateStorage(storage = CacheStorage.Unlimited())
        isShared = false
    }

    /**
     * Configures [ContentNegotiation] using Kotlinx Serialization.
     * Supports standard `application/json` and fallback `text/plain` responses for increased API compatibility across platforms.
     */
    private fun ContentNegotiationConfig.configure() {
        json(json = json, contentType = ContentType.Application.Json)
        register(contentType = ContentType.Text.Plain, converter = KotlinxSerializationConverter(format = json))
    }

    /**
     * Enables [ContentEncoding] to support compressed Gzip responses, significantly reducing payload sizes.
     */
    private fun ContentEncodingConfig.configure() {
        gzip()
    }

    /**
     * Injects default headers into every request.
     * Enforces that all requests accept [ContentType.Application.Json].
     */
    private fun DefaultRequest.DefaultRequestBuilder.configure() {
        header(key = HttpHeaders.Accept, value = ContentType.Application.Json)
    }

    companion object {
        private const val TAG = "HttpClient"
        private const val CONNECT_TIMEOUT_MILLIS = 20_000L
        private const val SOCKET_TIMEOUT_MILLIS = 50_000L
        private const val REQUEST_TIMEOUT_MILLIS = 120_000L
    }
}
