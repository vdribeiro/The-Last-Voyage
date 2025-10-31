package com.hybris.tlv.config

import kotlin.concurrent.Volatile
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import io.ktor.client.HttpClient
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

internal class Config(private val httpClient: HttpClient): ConfigManager {

    private val mutex = Mutex()

    @Volatile
    private var _preferences: Preferences = Preferences()
    override val preferences: Preferences get() = _preferences

    @Volatile
    private var _localConfigs: Configs = Configs()
    override val localConfigs: Configs get() = _localConfigs

    @Volatile
    private var _remoteConfigs: Configs = Configs()
    override val remoteConfigs: Configs get() = _remoteConfigs
    private val remoteInterval = 1.hours

    private suspend fun setPreferences(preferences: Preferences) = mutex.withLock { _preferences = preferences }
    private suspend fun setLocalConfigs(configs: Configs) = mutex.withLock { _localConfigs = configs }
    private suspend fun setRemoteConfigs(configs: Configs) = mutex.withLock { _remoteConfigs = configs }

    override suspend fun refresh() {
        loadPreferences()
        loadLocalConfigs()
        fetchRemoteConfigs()
    }

    private suspend fun loadPreferences() {
        val preferences = loadJsonFile(path = PREFERENCES_JSON) ?: Preferences().also { savePreferences(preferences = it) }
        setPreferences(preferences = preferences)
        Telemetry.info(tag = TAG, message = "Loaded preferences")
    }

    private suspend fun loadLocalConfigs() {
        val configs = loadJsonFile(path = CONFIGS_JSON) ?: Configs().also { saveConfigs(configs = it) }
        setLocalConfigs(configs = configs)
        Telemetry.info(tag = TAG, message = "Loaded local configs")
    }

    private suspend fun fetchRemoteConfigs() {
        // To prevet unnecessary fetches, wait 1 hour in between
        val remoteConfigs = if (!hasTimePassed(dateTime = _preferences.syncTime, duration = remoteInterval)) {
            _localConfigs.also { Telemetry.info(tag = TAG, message = "Fetched remote configs from local cache") }
        } else {
            setPreferences { it.copy(syncTime = now()) }
            when (val result = httpClient.getStream<Configs>(path = CONFIGS_URL)) {
                is Result.Error -> null.also { Telemetry.error(tag = TAG, message = "Unable to get configs", throwable = result.error) }
                is Result.Success -> result.list.firstOrNull().also { Telemetry.info(tag = TAG, message = "Fetched remote configs") }
            } ?: _localConfigs
        }
        setRemoteConfigs(configs = remoteConfigs)
    }

    override suspend fun setPreferences(preferences: (Preferences) -> Preferences) =
        setPreferences(preferences = preferences(_preferences))

    override suspend fun setConfigs(configs: (Configs) -> Configs) =
        setLocalConfigs(configs = configs(_localConfigs))

    override suspend fun savePreferences(preferences: Preferences) = mutex.withLock {
        val file = saveJsonFile(path = PREFERENCES_JSON, content = preferences)
        Telemetry.info(tag = TAG, message = "Flushed preferences: $file")
    }

    override suspend fun saveConfigs(configs: Configs) = mutex.withLock {
        val file = saveJsonFile(path = CONFIGS_JSON, content = configs)
        Telemetry.info(tag = TAG, message = "Flushed local configs: $file")
    }

    companion object Companion {
        private const val TAG = "Config"
    }
}
