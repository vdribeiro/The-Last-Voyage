package com.hybris.tlv.serializer

import com.hybris.tlv.achievements
import com.hybris.tlv.catastrophes
import com.hybris.tlv.configs
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

@Suppress("UNCHECKED_CAST", "RedundantSuspendModifier", "unused")
private suspend fun <T> loadFromJsonShadowing(path: String, serializer: KSerializer<List<T>>): List<T> =
    when (path) {
        "files/configs.json" -> configs
        "files/translations.json" -> translations
        "files/learnings.json" -> learnings
        "files/catastrophes.json" -> catastrophes
        "files/engines.json" -> engines
        "files/hosts.json" -> stellarHosts
        "files/planets.json" -> planets
        "files/events.json" -> events
        "files/achievements.json" -> achievements
        "files/credits.json" -> credits
        else -> emptyList()
    } as List<T>
