package com.hybris.tlv.serializer

import com.hybris.tlv.serializer.JsonSerializer.json
import kotlinx.serialization.json.Json
import thelastvoyage.composeapp.generated.resources.Res

internal object JsonSerializer {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
}

internal suspend fun <T> loadFromJson(path: String): List<T> = runCatching {
    json.decodeFromString<List<T>>(string = Res.readBytes(path = path).decodeToString())
}.getOrDefault(defaultValue = emptyList())
