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
    private var remoteCache: Configs = Configs()
    override val remoteConfigs: Configs get() = remoteCache

    override suspend fun fetch() {
        fetchLocal()
        fetchRemote()
    }

    private suspend fun fetchLocal() {
        this@Config.localConfigs = loadJsonFile(path = CONFIGS_JSON) ?: Configs().also { flush(configs = it) }
    }

    private suspend fun fetchRemote() {
        // To prevet unnecessary fetches, wait 1 hour in between
        if (!hasTimePassed(dateTime = getPreferences().syncTime, duration = 1.hours)) {
            remoteCache = localConfigs
            return
        }
        setPreferences { it.copy(syncTime = now()) }

        val remoteConfigs = when (val result = httpClient.getStream<Configs>(path = CONFIGS_URL)) {
            is Result.Error -> null.also { Telemetry.error(tag = TAG, message = "Unable to get configs", throwable = result.error) }
            is Result.Success -> result.list.firstOrNull()
        } ?: Configs()
        remoteCache = remoteConfigs
    }

    override suspend fun flush(configs: Configs) {
        saveJsonFile(path = CONFIGS_JSON, content = configs)
    }

    override suspend fun getPreferences(): Preferences =
        loadJsonFile(path = PREFERENCES_JSON) ?: Preferences().also { savePreferences(preferences = it) }

    override suspend fun setPreferences(preferences: (Preferences) -> Preferences) =
        savePreferences(preferences = preferences(getPreferences()))

    private suspend fun savePreferences(preferences: Preferences) {
        saveJsonFile(path = PREFERENCES_JSON, content = preferences)
    }

    companion object Companion {
        private const val TAG = "Config"
    }
}
