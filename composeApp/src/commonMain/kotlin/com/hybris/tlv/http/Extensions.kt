package com.hybris.tlv.http

import com.hybris.tlv.serializer.json
import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.isSuccess
import io.ktor.utils.io.toByteArray

internal suspend fun <T> HttpClient.getStream(url: String): Result<T> = runCatching {
    prepareGet(urlString = url).execute { httpResponse ->
        if (!httpResponse.status.isSuccess()) return@execute Result.Error(error = "Unsuccessful response: ${httpResponse.status}")
        val channel = httpResponse.bodyAsChannel()
        val bytes = channel.toByteArray()
        Result.Success(list = json.decodeFromString<List<T>>(string = bytes.decodeToString()))
    }
}.getOrElse {
    Result.Error(error = it.message.orEmpty())
}

internal fun Map<String, Any>.getString(key: String): String? =
    get(key = key)?.toString()

internal fun Map<String, Any>.getDouble(key: String): Double? =
    getString(key = key)?.toDoubleOrNull()

internal fun Map<String, Any>.getBoolean(key: String): Boolean? =
    getString(key = key)?.toBooleanStrictOrNull()

