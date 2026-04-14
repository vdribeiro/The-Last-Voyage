package com.hybris.tlv.data.config

import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.hours
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.locale.hasTimePassed
import com.hybris.tlv.core.locale.now
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.storage.FilePath
import com.hybris.tlv.data.storage.deleteJsonFile
import com.hybris.tlv.data.storage.loadJsonFile
import com.hybris.tlv.data.storage.saveJsonFile
import com.hybris.tlv.domain.flag.FeatureFlags.flags

/**
 * This class implements [ConfigManager] acting as the "Source of Truth" for settings, coordinating between local disk storage, in-memory caches and remote network updates.
 * It ensures thread-safety across platforms using a [Mutex] for disk I/O and utilizes [MutableStateFlow] to provide reactive updates and atomic access.
 *
 * @property httpClient The [HttpClient] instance used for remote API communication.
 */
internal class Config(private val httpClient: HttpClient): ConfigManager {

    /**
     * Synchronizes access to local storage files to prevent concurrent write corruption.
     */
    private val mutex = Mutex()

    private val _preferences: MutableStateFlow<Preferences> = MutableStateFlow(value = Preferences())
    override val preferences: Preferences get() = _preferences.value

    private val _localConfigs: MutableStateFlow<Configs> = MutableStateFlow(value = Configs())
    override val localConfigs: Configs get() = _localConfigs.value

    private val _remoteConfigs: MutableStateFlow<Configs> = MutableStateFlow(value = Configs())
    override val remoteConfigs: Configs get() = _remoteConfigs.value

    /**
     * The interval required between network syncs.
     * - Returns [ZERO] in dev mode to facilitate rapid testing.
     * - Returns 1 hour in production to conserve battery and data.
     */
    private val cacheTTL: Duration get() = if (flags.devMode) ZERO else 1.hours

    override suspend fun reset(): ConfigManager = apply {
        withContext(context = Dispatcher.IO) {
            mutex.withLock {
                deleteJsonFile(path = FilePath.Configs)
                deleteJsonFile(path = FilePath.Preferences)
            }
            val preferences = Preferences()
            val configs = Configs()
            _preferences.update { preferences }
            _localConfigs.update { configs }
            _remoteConfigs.update { configs }
        }
    }

    override suspend fun setup(): ConfigManager = apply {
        withContext(context = Dispatcher.IO) {
            val preferences = mutex.withLock { loadJsonFile(path = FilePath.Preferences) ?: Preferences() }
            setPreferences { preferences }
            val localConfigs = mutex.withLock { loadJsonFile(path = FilePath.Configs) ?: Configs() }
            _localConfigs.update { localConfigs }
            fetchRemoteConfigs()
            saveConfigs()
        }
    }

    override suspend fun fetchRemoteConfigs(): ConfigManager = apply {
        withContext(context = Dispatcher.IO) {
            if (!hasTimePassed(dateTime = _preferences.value.syncTime, duration = cacheTTL)) return@withContext
            setPreferences { it.copy(syncTime = now()) }

            when (val result = httpClient.get<Configs>(path = URL.Configs)) {
                is Result.Error -> Telemetry.error(tag = TAG, message = "Unable to get remote configs", throwable = result.error)
                is Result.Success -> {
                    val configs = result.list.firstOrNull()
                    if (configs != null) {
                        Telemetry.info(tag = TAG, message = "Fetched remote configs")
                        _remoteConfigs.update { configs }
                        _localConfigs.update {
                            it.copy(
                                developerCorner = configs.developerCorner,
                                formula = configs.formula,
                            )
                        }
                    } else Telemetry.error(tag = TAG, message = "No remote configs set")
                }
            }
        }
    }

    override suspend fun setPreferences(preferences: (Preferences) -> Preferences): ConfigManager = apply {
        withContext(context = Dispatcher.IO) {
            _preferences.update(function = preferences)
            mutex.withLock { saveJsonFile(path = FilePath.Preferences, content = _preferences.value) }
        }
    }

    override fun setConfigs(configs: (Configs) -> Configs): ConfigManager = apply {
        _localConfigs.update(function = configs)
    }

    override suspend fun saveConfigs(): ConfigManager = apply {
        withContext(context = Dispatcher.IO) {
            mutex.withLock { saveJsonFile(path = FilePath.Configs, content = _localConfigs.value) }
        }
    }

    companion object Companion {
        private const val TAG = "Config"
    }
}
