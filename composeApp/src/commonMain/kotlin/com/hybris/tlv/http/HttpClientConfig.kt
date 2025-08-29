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
internal const val CONFIGS_URL =
    "https://gist.githubusercontent.com/vdribeiro/eb23013b329c47317622981187df3f23/raw/f2aef92a463df2b1ee13dd005a520a3fe840658c/configs.json"
internal const val TRANSLATIONS_URL =
    "https://gist.githubusercontent.com/vdribeiro/f7506d5deafe08d9bf489c5b3bf7c00a/raw/91c7b906a7905ae877004430975e775d8fba8b76/translations.json"
internal const val CATASTROPHES_URL =
    "https://gist.githubusercontent.com/vdribeiro/27258c022708a20066f793996031d884/raw/551592f492ed0d1726e8e570232e2771e6011fa4/catastrophes.json"
internal const val ENGINES_URL =
    "https://gist.githubusercontent.com/vdribeiro/4168f14c15569dd6dd4a57af4ee37a99/raw/c43fea2cd4bbfcbbd42daa247d4d844671439622/engines.json"
internal const val STELLAR_HOSTS_URL =
    "https://gist.githubusercontent.com/vdribeiro/7e0ccc933aa6826bf1f427aa036f5793/raw/09df3493670cb24bfade6ae4a440afe61f62b68c/hosts.json"
internal const val PLANETS_URL =
    "https://gist.githubusercontent.com/vdribeiro/95146e01cd2b5c322a5e49ee4b9e3261/raw/7b57429663f891d8434197b34f49525e35faf4e5/planets.json"
internal const val EVENTS_URL =
    "https://gist.githubusercontent.com/vdribeiro/c2cf6a30e9be34c512f77ceea583bc71/raw/1df8ea3140695bcd47150739ae0bae47adee60c4/events.json"
internal const val ACHIEVEMENTS_URL =
    "https://gist.githubusercontent.com/vdribeiro/bf676c0c196c64ed40a7a1e7635035ea/raw/10a9fce6f794c7b6f81f1c85be00fdb3e718b8dc/achievements.json"
internal const val CREDITS_URL =
    "https://gist.githubusercontent.com/vdribeiro/a0dd7e6766e8bb40d1028a62d4d8f941/raw/0f0d1b6426d9c62f116b55d0e3d00e984d501fc1/credits.json"

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
