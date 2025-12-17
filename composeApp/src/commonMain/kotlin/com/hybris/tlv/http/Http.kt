package com.hybris.tlv.http

import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import io.ktor.utils.io.toByteArray
import com.hybris.tlv.TLV.flag
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.platform.isDebug
import com.hybris.tlv.serializer.decode
import com.hybris.tlv.telemetry.Telemetry

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
            val bytes = channel.toByteArray()
            val list = decode<List<T>>(value = bytes.decodeToString()) ?: throw Throwable("Unable to decode response")
            Result.Success(list = list)
        }
    }.getOrElse { Result.Error(error = it) }
}

/**
 * Checks for internet availability with a debounce mechanism to avoid frequent checks.
 * This function uses a mutex to ensure thread safety and caches the internet status for a duration of [cacheTTL].
 */
private suspend fun isInternetAvailableDebounced(): Boolean = runCatching {
    mutex.withLock {
        val now = TimeSource.Monotonic.markNow()
        val previous = lastCheckTime?.elapsedNow() ?: INFINITE
        if (previous < cacheTTL) return@withLock lastKnownStatus
        lastKnownStatus = isInternetAvailable()
        lastCheckTime = now
        return@withLock lastKnownStatus
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = false)

private val mutex = Mutex()
private val cacheTTL: Duration = if (isDebug) ZERO else 5.seconds
private var lastCheckTime: TimeMark? = null
private var lastKnownStatus = false

private const val TAG = "Network"
