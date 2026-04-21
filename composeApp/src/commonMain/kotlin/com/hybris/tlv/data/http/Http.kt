package com.hybris.tlv.data.http

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import com.hybris.tlv.core.flow.Dispatcher

/**
 * Executes a type-safe GET request and decodes the response into a [Result].
 *
 * - **URL Preparation:** Encodes the [path] and appends [queryMap] parameters safely.
 * - **Resource Management:** Executes on [Dispatcher.IO] to prevent blocking the calling thread.
 * - **Error Handling:** Catches network, parsing, and server errors, wrapping them in a [Result.Error].
 *
 * @param T The model type to decode the JSON response into.
 * @param path The [URL] endpoint for the request.
 * @param queryMap An optional map of key-value pairs to be appended as URL query parameters.
 * @return A [Result.Success] containing a [List] of [T] on success, or [Result.Error] on failure containing the exception that occurred.
 */
internal suspend inline fun <reified T> HttpClient.get(
    path: URL,
    queryMap: Map<String, String> = emptyMap(),
): Result<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        val response = get(urlString = path.path.encodeURLPath()) {
            queryMap.forEach { url.parameters.append(name = it.key, value = it.value) }
        }

        if (!response.status.isSuccess()) throw Throwable(message = "Unsuccessful response: ${response.status}")
        Result.Success(list = response.body<List<T>>())
    }.getOrElse { Result.Error(error = it) }
}
