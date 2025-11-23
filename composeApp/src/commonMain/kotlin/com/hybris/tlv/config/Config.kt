package com.hybris.tlv.config

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

    private val _preferences: MutableStateFlow<Preferences> = MutableStateFlow(value = Preferences())
    override val preferences: StateFlow<Preferences> = _preferences.asStateFlow()

    private val _localConfigs: MutableStateFlow<Configs> = MutableStateFlow(value = Configs())
    override val localConfigs: StateFlow<Configs> = _localConfigs.asStateFlow()

    private val _remoteConfigs: MutableStateFlow<Configs> = MutableStateFlow(value = Configs())
    override val remoteConfigs: StateFlow<Configs> = _remoteConfigs.asStateFlow()

    private val cacheTTL: Duration = if (isDebug) ZERO else 1.hours

    override suspend fun setup(): ConfigManager = apply {
        val preferences = mutex.withLock { loadJsonFile(path = PREFERENCES_JSON) ?: Preferences() }
        setPreferences { preferences }
        val localConfigs = mutex.withLock { loadJsonFile(path = CONFIGS_JSON) ?: Configs() }
        _localConfigs.update { localConfigs }
        fetchRemoteConfigs()
        saveConfigs()
    }

    override suspend fun fetchRemoteConfigs(): ConfigManager = apply {
        if (!hasTimePassed(dateTime = _preferences.value.syncTime, duration = cacheTTL)) return@apply
        setPreferences { it.copy(syncTime = now()) }

        when (val result = httpClient.getStream<Configs>(path = CONFIGS_URL)) {
            is Result.Error -> Telemetry.error(tag = TAG, message = "Unable to get remote configs", throwable = result.error)
            is Result.Success -> {
                val configs = result.list.firstOrNull()
                if (configs != null) {
                    Telemetry.info(tag = TAG, message = "Fetched remote configs")
                    _remoteConfigs.update { configs }
                    _localConfigs.update {
                        it.copy(
                            developerCorner = configs.developerCorner,
                            support = configs.support,
                            formula = configs.formula,
                        )
                    }
                } else Telemetry.error(tag = TAG, message = "No remote configs set")
            }
        }
    }

    override suspend fun setPreferences(preferences: (Preferences) -> Preferences): ConfigManager = apply {
        _preferences.update { preferences(it) }
        mutex.withLock { saveJsonFile(path = PREFERENCES_JSON, content = _preferences.value) }
    }

    override suspend fun setConfigs(configs: (Configs) -> Configs): ConfigManager = apply {
        _localConfigs.update { configs(it) }
    }

    override suspend fun saveConfigs(): ConfigManager = apply {
        mutex.withLock { saveJsonFile(path = CONFIGS_JSON, content = _localConfigs.value) }
    }

    companion object Companion {
        private const val TAG = "Config"
    }
}
