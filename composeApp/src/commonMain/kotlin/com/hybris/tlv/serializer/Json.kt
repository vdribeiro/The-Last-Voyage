package com.hybris.tlv.serializer

import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import io.ktor.http.decodeURLQueryComponent
import io.ktor.http.encodeURLQueryComponent
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.storage.loadFile
import com.hybris.tlv.storage.saveFile
import com.hybris.tlv.telemetry.Telemetry
import thelastvoyage.composeapp.generated.resources.Res

@OptIn(ExperimentalSerializationApi::class)
internal val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    allowTrailingComma = true
}

/**
 * Safely encode to JSON string.
 */
internal inline fun <reified T> encode(value: T?): String? = runCatching {
    value?.let { json.encodeToString(value = value) }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to encode value", throwable = it) }.getOrNull()

/**
 * Safely decode a JSON string.
 */
internal inline fun <reified T> decode(value: String?): T? = runCatching {
    value?.let {
        json.decodeFromString<T>(string = value.ifBlank {
            when (T::class) {
                Collection::class -> "[{}]"
                else -> "{}"
            }
        })
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to decode value", throwable = it) }.getOrNull()

/**
 * Safely encode to URL string.
 */
internal inline fun <reified T> encodeURL(value: T?): String = runCatching {
    encode(value = value)?.encodeURLQueryComponent()
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to encode URL value", throwable = it) }.getOrNull() ?: "null"

/**
 * Safely decode a URL string.
 */
internal inline fun <reified T> decodeURL(value: String?): T? = runCatching {
    if (value == "null") return null
    decode<T>(value = value?.decodeURLQueryComponent())
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to decode URL value", throwable = it) }.getOrNull()

/**
 * Save a JSON file.
 */
internal suspend inline fun <reified T> saveJsonFile(path: String, content: T): Boolean = withContext(context = Dispatcher.IO) {
    encode<T>(value = content)?.let { saveFile(path = path, content = it) } ?: false
}

/**
 * Load a JSON file.
 */
internal suspend inline fun <reified T> loadJsonFile(path: String): T? = withContext(context = Dispatcher.IO) {
    loadFile(path = path)?.let { decode<T>(value = it) }
}

/**
 * Load a JSON resource.
 */
internal suspend inline fun <reified T> loadFromJsonResource(path: String): List<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        decode<List<T>>(value = Res.readBytes(path = path).decodeToString())
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to load resource", throwable = it) }.getOrNull().orEmpty()
}

private const val TAG = "JSON"

// Resources
internal const val TRANSLATIONS_JSON = "files/translations.json"
internal const val STELLAR_HOSTS_JSON = "files/hosts.json"
internal const val PLANETS_JSON = "files/planets.json"
internal const val LEARNINGS_JSON = "files/learnings.json"
internal const val CATASTROPHES_JSON = "files/catastrophes.json"
internal const val ENGINES_JSON = "files/engines.json"
internal const val EVENTS_JSON = "files/events.json"
internal const val ACHIEVEMENTS_JSON = "files/achievements.json"
internal const val CREDITS_JSON = "files/credits.json"

// Configs
internal const val CONFIGS_JSON = "configs.json"
internal const val PREFERENCES_JSON = "preferences.json"

// Archive
internal const val SOLAR_HOSTS_JSON = "files/solarsystem.json"
internal const val SOLAR_PLANETS_JSON = "files/solarplanets.json"
internal const val ARCHIVE_STELLAR_HOSTS_JSON = "hosts.json"
internal const val ARCHIVE_PLANETS_JSON = "planets.json"
