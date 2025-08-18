package com.hybris.tlv.http.client

import com.hybris.tlv.http.json.json
import com.hybris.tlv.logger.Logger
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

internal const val EXOPLANET_ARCHIVE_URL = "exoplanetarchive.ipac.caltech.edu"
internal fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setLogging() {
    install(plugin = Logging) {
        logger = object: io.ktor.client.plugins.logging.Logger {
            override fun log(message: String) {
                Logger.debug(tag = "Ktor", message = message)
            }
        }
        level = LogLevel.INFO
    }
}

internal fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setRequestUrl(url: String) {
    install(plugin = DefaultRequest) {
        url {
            protocol = URLProtocol.HTTPS
            host = url
        }
    }
}

internal fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setTimeout(timeout: Long) {
    install(plugin = HttpTimeout) { setTimeout(timeout) }
}

internal fun HttpTimeoutConfig.setTimeout(timeout: Long) {
    requestTimeoutMillis = timeout
    connectTimeoutMillis = timeout
    socketTimeoutMillis = timeout
}

internal fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setCache() {
    install(plugin = HttpCache)
}

internal fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setContentEncoding(compressionQuality: Float) {
    install(plugin = ContentEncoding) { gzip(quality = compressionQuality) }
}

internal fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setContentValidator() {
    install(plugin = ContentNegotiation) { json(json = json) }
}
