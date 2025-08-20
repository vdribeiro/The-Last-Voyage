package com.hybris.tlv.serializer

import com.hybris.tlv.mock.achievements
import com.hybris.tlv.mock.catastrophes
import com.hybris.tlv.mock.credits
import com.hybris.tlv.mock.engines
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.mock.translations
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
}

@Suppress("UNCHECKED_CAST")
internal suspend inline fun <reified T> loadFromJson(path: String): List<T> =
    when (path) {
        "files/translations.json" -> translations
        "files/catastrophes.json" -> catastrophes
        "files/engines.json" -> engines
        "files/hosts.json" -> stellarHosts
        "files/planets.json" -> planets
        "files/events.json" -> events
        "files/achievements.json" -> achievements
        "files/credits.json" -> credits
        else -> emptyList()
    } as List<T>
