package com.hybris.tlv.config

import com.hybris.tlv.http.HttpClientFactory.Companion.CONFIGS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.locale.hasTimePassed
import com.hybris.tlv.locale.now
import com.hybris.tlv.telemetry.Logger
import com.hybris.tlv.serializer.CONFIGS_JSON
import com.hybris.tlv.serializer.PREFERENCES_JSON
import com.hybris.tlv.serializer.json
import com.hybris.tlv.storage.loadFile
import com.hybris.tlv.storage.saveFile
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
        this@Config.localConfigs = runCatching {
            json.decodeFromString<Configs>(string = loadFile(path = CONFIGS_JSON).orEmpty())
        }.getOrElse {
            Logger.error(tag = TAG, message = "Unable to load config file\n${it.stackTraceToString()}")
            null
        } ?: Configs().also { flush(configs = it) }
    }

    private suspend fun fetchRemote() {
        // To prevet unnecessary fetches, wait 1 hour in between
        if (!hasTimePassed(dateTime = getPreferences().syncTime, duration = 1.hours)) {
            remoteCache = localConfigs
            return
        }
        setPreferences { it.copy(syncTime = now()) }

        val remoteConfigs = when (val result = httpClient.getStream<Configs>(path = CONFIGS_URL)) {
            is Result.Error -> null.also { Logger.error(tag = TAG, message = result.error) }
            is Result.Success -> result.list.firstOrNull()
        } ?: Configs()
        remoteCache = remoteConfigs
    }

    override suspend fun flush(configs: Configs) {
        runCatching {
            saveFile(path = CONFIGS_JSON, content = json.encodeToString(value = configs))
        }.getOrElse {
            Logger.error(tag = TAG, message = "Unable to flush config file\n${it.stackTraceToString()}")
        }
    }

    override suspend fun getPreferences(): Preferences = runCatching {
        json.decodeFromString<Preferences>(string = loadFile(path = PREFERENCES_JSON).orEmpty())
    }.getOrElse {
        Logger.error(tag = TAG, message = "Unable to load preferences file\n${it.stackTraceToString()}")
        null
    } ?: Preferences().also { savePreferences(preferences = it) }

    override suspend fun setPreferences(preferences: (Preferences) -> Preferences) =
        savePreferences(preferences = preferences(getPreferences()))

    private fun savePreferences(preferences: Preferences) {
        runCatching {
            saveFile(path = PREFERENCES_JSON, content = json.encodeToString(value = preferences))
        }.getOrElse {
            Logger.error(tag = TAG, message = "Unable to save preferences file\n${it.stackTraceToString()}")
        }
    }

    companion object Companion {
        private const val TAG = "Config"
    }
}
