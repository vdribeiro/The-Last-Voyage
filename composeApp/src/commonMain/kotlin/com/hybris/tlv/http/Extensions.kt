package com.hybris.tlv.http

import com.hybris.tlv.serializer.json
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import io.ktor.utils.io.toByteArray

internal suspend inline fun <reified T> HttpClient.getStream(
    path: String,
    queryMap: Map<String, String> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = runCatching {
    prepareGet(urlString = path.encodeURLPath()) {
        queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
        block()
    }.execute { httpResponse ->
        if (!httpResponse.status.isSuccess()) return@execute Result.Error(error = "Unsuccessful response: ${httpResponse.status}")
        val channel = httpResponse.bodyAsChannel()
        val bytes = channel.toByteArray()
        Result.Success(list = json.decodeFromString<List<T>>(string = bytes.decodeToString()))
    }
}.getOrElse {
    Result.Error(error = it.message.orEmpty())
}

internal fun HttpTimeoutConfig.setTimeout(timeout: Long) {
    requestTimeoutMillis = timeout
    connectTimeoutMillis = timeout
    socketTimeoutMillis = timeout
}
