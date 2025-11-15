package com.hybris.tlv.http

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import io.ktor.utils.io.toByteArray
import com.hybris.tlv.serializer.decode

internal suspend inline fun <reified T> HttpClient.getStream(
    path: String,
    queryMap: Map<String, String> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = runCatching {
    if (!isInternetAvailable()) throw Throwable(message = "No internet connection available.")
    prepareGet(urlString = path.encodeURLPath()) {
        queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
        block()
    }.execute { httpResponse ->
        if (!httpResponse.status.isSuccess()) throw Throwable(message = "Unsuccessful response: ${httpResponse.status}")
        val channel = httpResponse.bodyAsChannel()
        val bytes = channel.toByteArray()
        val list = decode<List<T>>(value = bytes.decodeToString())!!
        Result.Success(list = list)
    }
}.getOrElse { Result.Error(error = it) }
