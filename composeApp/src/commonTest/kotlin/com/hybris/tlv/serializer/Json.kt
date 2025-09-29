@file:Suppress("unused", "UNCHECKED_CAST", "RedundantSuspendModifier")

package com.hybris.tlv.serializer

import com.hybris.tlv.achievements
import com.hybris.tlv.catastrophes
import com.hybris.tlv.credits
import com.hybris.tlv.engines
import com.hybris.tlv.events
import com.hybris.tlv.learnings
import com.hybris.tlv.planets
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.translations
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
}

private suspend fun <T> loadFromJsonResourceShadowing(path: String, serializer: KSerializer<List<T>>): List<T> =
    when (path) {
        TRANSLATIONS_JSON -> translations
        LEARNINGS_JSON -> learnings
        CATASTROPHES_JSON -> catastrophes
        ENGINES_JSON -> engines
        STELLAR_HOSTS_JSON, SOLAR_HOSTS_JSON -> stellarHosts
        PLANETS_JSON, SOLAR_PLANETS_JSON -> planets
        EVENTS_JSON -> events
        ACHIEVEMENTS_JSON -> achievements
        CREDITS_JSON -> credits
        else -> emptyList()
    } as List<T>
