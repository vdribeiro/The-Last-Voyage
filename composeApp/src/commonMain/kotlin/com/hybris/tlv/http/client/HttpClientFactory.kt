package com.hybris.tlv.http.client

import com.hybris.tlv.logger.Logger
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object HttpClientFactory {

    private const val TAG = "Ktor"

    fun buildExoplanetHttpClient(): HttpClient = HttpClient {
        setLogging()
        setTimeout(timeout = 60_000L * 5)
        setCache()
        setRequestUrl(url = "exoplanetarchive.ipac.caltech.edu/TAP")
        setContentValidator()
        setContentEncoding(compressionQuality = 0.9F)
    }

    fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setLogging() {
        install(plugin = Logging) {
            logger = object: io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    Logger.debug(tag = TAG, message = message)
                }
            }
            level = LogLevel.INFO
        }
    }

    fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setRequestUrl(url: String) {
        install(plugin = DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = url
            }
        }
    }

    fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setTimeout(timeout: Long) {
        install(plugin = HttpTimeout) { setTimeout(timeout) }
    }

    fun HttpTimeoutConfig.setTimeout(timeout: Long) {
        requestTimeoutMillis = timeout
        connectTimeoutMillis = timeout
        socketTimeoutMillis = timeout
    }

    fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setCache() {
        install(plugin = HttpCache)
    }

    fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setContentEncoding(compressionQuality: Float) {
        install(plugin = ContentEncoding) { gzip(quality = compressionQuality) }
    }

    fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setContentValidator() {
        install(plugin = ContentNegotiation) { json(json = json) }
    }
}

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}
