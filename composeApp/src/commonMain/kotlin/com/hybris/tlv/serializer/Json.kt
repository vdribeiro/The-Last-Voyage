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

internal suspend inline fun <reified T> loadFromJson(path: String): List<T> {
    val serializer = ListSerializer(elementSerializer = json.serializersModule.serializer<T>())
    return loadFromJsonShadowing(path, serializer)
}

// The Json loading must be mocked in tests and 'inline' breaks shadowing, hence the shadowing function
private suspend fun <T> loadFromJsonShadowing(path: String, serializer: KSerializer<List<T>>): List<T> {
    return runCatching {
        val stringContent = Res.readBytes(path).decodeToString()
        json.decodeFromString(serializer, stringContent)
    }.getOrDefault(emptyList())
}
