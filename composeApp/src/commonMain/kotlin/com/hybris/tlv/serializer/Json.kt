package com.hybris.tlv.serializer

import com.hybris.tlv.storage.loadFile
import com.hybris.tlv.storage.saveFile
import com.hybris.tlv.telemetry.Telemetry
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import thelastvoyage.composeapp.generated.resources.Res

@OptIn(ExperimentalSerializationApi::class)
val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
    allowTrailingComma = true
}

/**
 * Safely decode a JSON string.
 */
internal inline fun <reified T> decode(value: String?): T? = runCatching {
    value?.let { json.decodeFromString<T>(string = value) }
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to decode value", throwable = it)
    null
}

/**
 * Safely encode to JSON string.
 */
internal inline fun <reified T> encode(value: T?): String? = runCatching {
    value?.let { json.encodeToString(value = value) }
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to encode value", throwable = it)
    null
}

/**
 * Load a JSON file.
 */
internal suspend inline fun <reified T> loadJsonFile(path: String): T? =
    loadFile(path = path)?.let { decode<T>(value = it) }

/**
 * Save a JSON file.
 */
internal suspend inline fun <reified T> saveJsonFile(path: String, content: T): Boolean =
    encode<T>(value = content)?.let { saveFile(path = path, content = it) } ?: false

internal suspend inline fun <reified T> loadFromJsonResource(path: String): List<T> = runCatching {
    json.decodeFromString<List<T>>(string = Res.readBytes(path = path).decodeToString())
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to load resource", throwable = it)
    emptyList()
}

private const val TAG = "JSON"

// Resources
const val TRANSLATIONS_JSON = "files/translations.json"
const val STELLAR_HOSTS_JSON = "files/hosts.json"
const val SOLAR_HOSTS_JSON = "files/solarsystem.json"
const val PLANETS_JSON = "files/planets.json"
const val SOLAR_PLANETS_JSON = "files/solarplanets.json"
const val LEARNINGS_JSON = "files/learnings.json"
const val CATASTROPHES_JSON = "files/catastrophes.json"
const val ENGINES_JSON = "files/engines.json"
const val EVENTS_JSON = "files/events.json"
const val ACHIEVEMENTS_JSON = "files/achievements.json"
const val CREDITS_JSON = "files/credits.json"

// Files
const val CONFIGS_JSON = "configs.json"
const val PREFERENCES_JSON = "preferences.json"
