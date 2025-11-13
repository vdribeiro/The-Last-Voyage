package com.hybris.tlv.config

import kotlin.concurrent.Volatile
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import io.ktor.client.HttpClient
import com.hybris.tlv.http.HttpClientFactory.Companion.CONFIGS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.locale.hasTimePassed
import com.hybris.tlv.locale.now
import com.hybris.tlv.platform.isDebug
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
    private val remoteInterval: Duration = if (isDebug) ZERO else 1.hours

    override suspend fun refresh(): ConfigManager = apply {
        _preferences = loadJsonFile(path = PREFERENCES_JSON) ?: Preferences()
        _localConfigs = loadJsonFile(path = CONFIGS_JSON) ?: Configs()
        _remoteConfigs = if (!hasTimePassed(dateTime = _preferences.syncTime, duration = remoteInterval)) {
            null.also { Telemetry.info(tag = TAG, message = "Fetched remote configs from local cache") }
        } else {
            _preferences = _preferences.copy(syncTime = now())
            when (val result = httpClient.getStream<Configs>(path = CONFIGS_URL)) {
                is Result.Error -> null.also { Telemetry.error(tag = TAG, message = "Unable to get remote configs", throwable = result.error) }
                is Result.Success -> result.list.firstOrNull().also {
                    Telemetry.info(tag = TAG, message = "Fetched remote configs")
                    if (it != null) setNonVersioning(configs = it)
                }
            }
        } ?: _localConfigs
    }

    private fun setNonVersioning(configs: Configs) {
        _localConfigs = _localConfigs.copy(
            developerCorner = configs.developerCorner,
            support = configs.support,
            formula = configs.formula,
        )
    }

    override suspend fun setPreferences(preferences: (Preferences) -> Preferences): ConfigManager = apply {
        mutex.withLock { _preferences = preferences(_preferences) }
    }

    override suspend fun setConfigs(configs: (Configs) -> Configs): ConfigManager = apply {
        mutex.withLock { _localConfigs = configs(_localConfigs) }
    }

    override suspend fun savePreferences(): ConfigManager = apply {
        mutex.withLock { saveJsonFile(path = PREFERENCES_JSON, content = _preferences) }
    }

    override suspend fun saveConfigs(): ConfigManager = apply {
        mutex.withLock { saveJsonFile(path = CONFIGS_JSON, content = _localConfigs) }
    }

    companion object Companion {
        private const val TAG = "Config"
    }
}
