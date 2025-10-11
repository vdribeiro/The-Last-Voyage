package com.hybris.tlv.config

import com.hybris.tlv.http.HttpClientFactory.Companion.CONFIGS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.locale.hasTimePassed
import com.hybris.tlv.locale.now
import com.hybris.tlv.serializer.CONFIGS_JSON
import com.hybris.tlv.serializer.PREFERENCES_JSON
import com.hybris.tlv.serializer.loadJsonFile
import com.hybris.tlv.serializer.saveJsonFile
import com.hybris.tlv.telemetry.Telemetry
import io.ktor.client.HttpClient
import kotlin.time.Duration.Companion.hours

internal class Config(private val httpClient: HttpClient): ConfigManager {

    override var localConfigs: Configs = Configs()
    private var remoteConfigsCache: Configs = Configs()
    override val remoteConfigs: Configs get() = remoteConfigsCache

    override suspend fun fetch() {
        fetchLocal()
        fetchRemote()
    }

    private suspend fun fetchLocal() {
        this@Config.localConfigs = loadJsonFile(path = CONFIGS_JSON) ?: Configs().also { flush(configs = it) }
        Telemetry.info(tag = TAG, message = "Fetched local configs")
    }

    private suspend fun fetchRemote() {
        // To prevet unnecessary fetches, wait 1 hour in between
        if (!hasTimePassed(dateTime = getPreferences().syncTime, duration = 1.hours)) {
            remoteConfigsCache = localConfigs
            Telemetry.info(tag = TAG, message = "Fetched remote configs from local cache")
            return
        }
        setPreferences { it.copy(syncTime = now()) }

        val remoteConfigs = when (val result = httpClient.getStream<Configs>(path = CONFIGS_URL)) {
            is Result.Error -> null.also { Telemetry.error(tag = TAG, message = "Unable to get configs", throwable = result.error) }
            is Result.Success -> result.list.firstOrNull()
        } ?: Configs()
        remoteConfigsCache = remoteConfigs
        Telemetry.info(tag = TAG, message = "Fetched remote configs")
    }

    override suspend fun flush(configs: Configs) {
        saveJsonFile(path = CONFIGS_JSON, content = configs)
        Telemetry.info(tag = TAG, message = "Flushed local configs")
    }

    override suspend fun getPreferences(): Preferences {
        val preferences = loadJsonFile(path = PREFERENCES_JSON) ?: Preferences().also { savePreferences(preferences = it) }
        Telemetry.info(tag = TAG, message = "Fetched preferences")
        return preferences
    }

    override suspend fun setPreferences(preferences: (Preferences) -> Preferences) =
        savePreferences(preferences = preferences(getPreferences()))

    private suspend fun savePreferences(preferences: Preferences) {
        saveJsonFile(path = PREFERENCES_JSON, content = preferences)
        Telemetry.info(tag = TAG, message = "Flushed preferences")
    }

    companion object Companion {
        private const val TAG = "Config"
    }
}
