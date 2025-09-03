package com.hybris.tlv.http

import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.json
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

internal const val EXOPLANET_ARCHIVE_URL = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync"
internal const val CONFIGS_URL = "https://gist.githubusercontent.com/vdribeiro/eb23013b329c47317622981187df3f23"
internal const val TRANSLATIONS_URL = "https://gist.githubusercontent.com/vdribeiro/90daf9ebde2b8e37ce893e49e8d7f7c7"
internal const val LEARNINGS_URL = "https://gist.githubusercontent.com/vdribeiro/f9ef3c647be7e4aaaa6b9c7af43db270"
internal const val CATASTROPHES_URL = "https://gist.githubusercontent.com/vdribeiro/27258c022708a20066f793996031d884"
internal const val ENGINES_URL = "https://gist.githubusercontent.com/vdribeiro/4168f14c15569dd6dd4a57af4ee37a99"
internal const val STELLAR_HOSTS_URL = "https://gist.githubusercontent.com/vdribeiro/7e0ccc933aa6826bf1f427aa036f5793"
internal const val PLANETS_URL = "https://gist.githubusercontent.com/vdribeiro/95146e01cd2b5c322a5e49ee4b9e3261"
internal const val EVENTS_URL = "https://gist.githubusercontent.com/vdribeiro/c2cf6a30e9be34c512f77ceea583bc71"
internal const val ACHIEVEMENTS_URL = "https://gist.githubusercontent.com/vdribeiro/bf676c0c196c64ed40a7a1e7635035ea"
internal const val CREDITS_URL = "https://gist.githubusercontent.com/vdribeiro/a0dd7e6766e8bb40d1028a62d4d8f941"

internal fun <T: HttpClientEngineConfig> HttpClientConfig<T>.setLogging() {
    install(plugin = Logging) {
        logger = object: io.ktor.client.plugins.logging.Logger {
            override fun log(message: String) {
                Logger.info(tag = "Ktor", message = message)
            }
        }
        level = LogLevel.INFO
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
