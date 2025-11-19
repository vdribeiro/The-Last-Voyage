package com.hybris.tlv.config

import kotlin.concurrent.Volatile
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private var _preferences: MutableStateFlow<Preferences> = MutableStateFlow(value = Preferences())
    override val preferences: StateFlow<Preferences> get() = _preferences.asStateFlow()

    @Volatile
    private var _localConfigs: Configs = Configs()
    override val localConfigs: Configs get() = _localConfigs

    @Volatile
    private var _remoteConfigs: Configs = Configs()
    override val remoteConfigs: Configs get() = _remoteConfigs
    private val remoteInterval: Duration = if (isDebug) ZERO else 1.hours

    override suspend fun refresh(): ConfigManager = apply {
        mutex.withLock {
            _preferences.value = loadJsonFile(path = PREFERENCES_JSON) ?: Preferences()
            _localConfigs = loadJsonFile(path = CONFIGS_JSON) ?: Configs()
        }

        _remoteConfigs = if (!hasTimePassed(dateTime = _preferences.value.syncTime, duration = remoteInterval)) {
            null.also { Telemetry.info(tag = TAG, message = "Fetched remote configs from local cache") }
        } else {
            _preferences.update { it.copy(syncTime = now()) }
            when (val result = httpClient.getStream<Configs>(path = CONFIGS_URL)) {
                is Result.Error -> null.also { Telemetry.error(tag = TAG, message = "Unable to get remote configs", throwable = result.error) }
                is Result.Success -> result.list.firstOrNull().also { configs ->
                    Telemetry.info(tag = TAG, message = "Fetched remote configs")
                    if (configs != null) {
                        _localConfigs = _localConfigs.copy(
                            developerCorner = configs.developerCorner,
                            support = configs.support,
                            formula = configs.formula,
                        )
                    }
                }
            }
        } ?: _localConfigs

    }

    override suspend fun setPreferences(preferences: (Preferences) -> Preferences): ConfigManager = apply {
        _preferences.update { preferences(it) }
    }

    override suspend fun setConfigs(configs: (Configs) -> Configs): ConfigManager = apply {
        mutex.withLock { _localConfigs = configs(_localConfigs) }
    }

    override suspend fun savePreferences(): ConfigManager = apply {
        mutex.withLock { saveJsonFile(path = PREFERENCES_JSON, content = _preferences.value) }
    }

    override suspend fun saveConfigs(): ConfigManager = apply {
        mutex.withLock { saveJsonFile(path = CONFIGS_JSON, content = _localConfigs) }
    }

    companion object Companion {
        private const val TAG = "Config"
    }
}
