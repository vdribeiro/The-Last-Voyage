package com.hybris.tlv.http

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.build
import io.ktor.utils.io.core.size
import io.ktor.utils.io.core.writePacket
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.readText
import com.hybris.tlv.TLV.flag
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.serializer.decode

/**
 * Performs a GET request to the URL [path], given a map of query parameters [queryMap] to be appended to the URL,
 * and decodes the response body as a stream of objects of type [T].
 * This function handles network availability checks, URL encoding, query parameters, and JSON decoding.
 * It returns a [Result] object, which is either [Result.Success] containing the decoded list of objects,
 * or [Result.Error] containing the exception that occurred.
 * An additional lambda [block] can also be provided for further configuration of the [HttpRequestBuilder].
 */
internal suspend inline fun <reified T> HttpClient.getStream(
    path: String,
    queryMap: Map<String, String> = emptyMap(),
    noinline onProgress: ((Float) -> Unit)? = null,
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        if (!flag.http) throw Throwable(message = "Network disabled")
        if (!isInternetAvailableDebounced()) throw Throwable(message = "No internet connection available")

        prepareGet(urlString = path.encodeURLPath()) {
            queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
            block()
        }.execute { httpResponse ->
            if (!httpResponse.status.isSuccess()) throw Throwable(message = "Unsuccessful response: ${httpResponse.status}")

            val channel = httpResponse.bodyAsChannel()
            val contentLength = httpResponse.contentLength() ?: -1L
            val raw = BytePacketBuilder().use {
                while (!channel.isClosedForRead) {
                    val packet = channel.readRemaining(max = CHUNK_SIZE)
                    it.writePacket(packet = packet)
                    onProgress?.invoke(if (contentLength > 0) it.size.toFloat() / contentLength else -1F)
                }
                onProgress?.invoke(1f)
                it.build().readText()
            }
            Result.Success(list = decode<List<T>>(value = raw) ?: throw Throwable("Unable to decode response"))
        }
    }.getOrElse { Result.Error(error = it) }
}

private const val CHUNK_SIZE = 1024L * 8L
