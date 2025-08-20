package com.hybris.tlv.serializer

import kotlinx.serialization.json.Json
import thelastvoyage.composeapp.generated.resources.Res

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

internal suspend inline fun <reified T> loadFromJson(path: String): List<T> = runCatching {
    json.decodeFromString<List<T>>(string = Res.readBytes(path = path).decodeToString())
}.getOrDefault(defaultValue = emptyList())
