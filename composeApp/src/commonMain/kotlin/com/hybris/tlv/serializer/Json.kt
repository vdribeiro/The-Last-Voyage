package com.hybris.tlv.serializer

import com.hybris.tlv.storage.loadFile
import com.hybris.tlv.storage.saveFile
import com.hybris.tlv.telemetry.Logger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import thelastvoyage.composeapp.generated.resources.Res

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

/**
 * Safely decode a JSON string.
 */
internal inline fun <reified T> decode(value: String?): T? = runCatching {
    value?.let { json.decodeFromString<T>(string = value) }
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to decode value: ${it.stackTraceToString()}")
    null
}

/**
 * Safely encode to JSON string.
 */
internal inline fun <reified T> encode(value: T?): String? = runCatching {
    value?.let { json.encodeToString(value = value) }
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to encode value: ${it.stackTraceToString()}")
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

/**
 * Load a JSON resource.
 */
internal suspend inline fun <reified T> loadFromJsonResource(path: String): List<T> = runCatching {
    loadFromJsonResourceShadowing(path = path, serializer = ListSerializer(elementSerializer = json.serializersModule.serializer<T>()))
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to load resource: ${it.stackTraceToString()}")
    emptyList()
}

// The Json loading must be mocked in tests and 'inline' cannot be shadowed
private suspend fun <T> loadFromJsonResourceShadowing(path: String, serializer: KSerializer<List<T>>): List<T> =
    json.decodeFromString(deserializer = serializer, string = Res.readBytes(path).decodeToString())

private const val TAG = "JSON"

// Resources
const val TRANSLATIONS_JSON = "files/translations.json"
const val LEARNINGS_JSON = "files/learnings.json"
const val CATASTROPHES_JSON = "files/catastrophes.json"
const val ENGINES_JSON = "files/engines.json"
const val STELLAR_HOSTS_JSON = "files/hosts.json"
const val SOLAR_HOSTS_JSON = "files/solarsystem.json"
const val PLANETS_JSON = "files/planets.json"
const val SOLAR_PLANETS_JSON = "files/solarplanets.json"
const val EVENTS_JSON = "files/events.json"
const val ACHIEVEMENTS_JSON = "files/achievements.json"
const val CREDITS_JSON = "files/credits.json"

// Files
const val CONFIGS_JSON = "configs.json"
const val PREFERENCES_JSON = "preferences.json"
