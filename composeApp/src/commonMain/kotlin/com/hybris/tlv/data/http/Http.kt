package com.hybris.tlv.data.http

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.domain.flag.FeatureFlags.flags

/**
 * Performs a GET request to the URL [path], given a map of query parameters [queryMap] to be appended to the URL, and decodes the response body as a stream of objects of type [T].
 * This function handles network availability checks, URL encoding, query parameters, and JSON decoding.
 * It returns a [Result] object, which is either [Result.Success] containing the decoded list of objects, or [Result.Error] containing the exception that occurred.
 */
internal suspend inline fun <reified T> HttpClient.get(
    path: URL,
    queryMap: Map<String, String> = emptyMap(),
): Result<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        if (!flags.http) throw Throwable(message = "Network disabled")
        if (!isInternetAvailable()) throw Throwable(message = "No internet connection available")

        val response = get(urlString = path.path.encodeURLPath()) {
            queryMap.forEach { url.parameters.append(name = it.key, value = it.value) }
        }

        if (!response.status.isSuccess()) throw Throwable(message = "Unsuccessful response: ${response.status}")
        Result.Success(list = response.body<List<T>>())
    }.getOrElse { Result.Error(error = it) }
}
