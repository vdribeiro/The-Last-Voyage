package com.hybris.tlv.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import thelastvoyage.composeapp.generated.resources.Res

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

internal suspend inline fun <reified T> loadFromJsonResource(path: String): List<T> {
    val serializer = ListSerializer(elementSerializer = json.serializersModule.serializer<T>())
    return loadFromJsonShadowing(path = path, serializer = serializer)
}

// The Json loading must be mocked in tests and 'inline' cannot be shadowed
private suspend fun <T> loadFromJsonShadowing(path: String, serializer: KSerializer<List<T>>): List<T> {
    return runCatching {
        val stringContent = Res.readBytes(path).decodeToString()
        json.decodeFromString(deserializer = serializer, string = stringContent)
    }.getOrDefault(defaultValue = emptyList())
}

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
