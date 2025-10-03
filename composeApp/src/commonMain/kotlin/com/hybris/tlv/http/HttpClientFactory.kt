package com.hybris.tlv.http

import com.hybris.tlv.platform.isDebug
import com.hybris.tlv.serializer.json
import com.hybris.tlv.telemetry.Telemetry
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

internal class HttpClientFactory(engine: HttpClientEngine?) {

    val httpClient: HttpClient = when (engine) {
        null -> HttpClient { install() }
        else -> HttpClient(engine = engine) { install() }
    }

    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.install() {
        install(plugin = Logging) {
            logger = object: io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    Telemetry.info(tag = "Ktor", message = message)
                }
            }
            level = if (isDebug) LogLevel.ALL else LogLevel.INFO
        }
        install(plugin = HttpTimeout) {
            connectTimeoutMillis = 10_000L
            socketTimeoutMillis = 20_000L
            requestTimeoutMillis = 60_000L
        }
        install(plugin = HttpCache)
        install(plugin = ContentNegotiation) { json(json = json) }
        install(plugin = ContentEncoding) { gzip(quality = 0.9F) }
    }

    companion object {
        const val EXOPLANET_ARCHIVE_URL = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync"
        const val CONFIGS_URL = "https://gist.githubusercontent.com/vdribeiro/eb23013b329c47317622981187df3f23/raw/configs.json"
        const val TRANSLATIONS_URL = "https://gist.githubusercontent.com/vdribeiro/90daf9ebde2b8e37ce893e49e8d7f7c7/raw/translations.json"
        const val LEARNINGS_URL = "https://gist.githubusercontent.com/vdribeiro/f9ef3c647be7e4aaaa6b9c7af43db270/raw/learnings.json"
        const val CATASTROPHES_URL = "https://gist.githubusercontent.com/vdribeiro/27258c022708a20066f793996031d884/raw/catastrophes.json"
        const val ENGINES_URL = "https://gist.githubusercontent.com/vdribeiro/4168f14c15569dd6dd4a57af4ee37a99/raw/engines.json"
        const val STELLAR_HOSTS_URL = "https://gist.githubusercontent.com/vdribeiro/7e0ccc933aa6826bf1f427aa036f5793/raw/hosts.json"
        const val PLANETS_URL = "https://gist.githubusercontent.com/vdribeiro/95146e01cd2b5c322a5e49ee4b9e3261/raw/planets.json"
        const val EVENTS_URL = "https://gist.githubusercontent.com/vdribeiro/c2cf6a30e9be34c512f77ceea583bc71/raw/events.json"
        const val ACHIEVEMENTS_URL = "https://gist.githubusercontent.com/vdribeiro/bf676c0c196c64ed40a7a1e7635035ea/raw/achievements.json"
        const val CREDITS_URL = "https://gist.githubusercontent.com/vdribeiro/a0dd7e6766e8bb40d1028a62d4d8f941/raw/credits.json"
    }
}
