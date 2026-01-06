package com.hybris.tlv.http

import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import com.hybris.tlv.TLV.flag
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.platform.isDebug
import com.hybris.tlv.telemetry.Telemetry

private val mutex = Mutex()
private val cacheTTL: Duration = if (isDebug) ZERO else 1.minutes
private var lastCheckTime: TimeMark? = null
private var lastKnownStatus = false

/**
 * Performs a GET request to the URL [path], given a map of query parameters [queryMap] to be appended to the URL, and decodes the response body as a stream of objects of type [T].
 * This function handles network availability checks, URL encoding, query parameters, and JSON decoding.
 * It returns a [Result] object, which is either [Result.Success] containing the decoded list of objects, or [Result.Error] containing the exception that occurred.
 * An additional lambda [block] can also be provided for further configuration of the [HttpRequestBuilder].
 */
internal suspend inline fun <reified T> HttpClient.get(
    path: URL,
    queryMap: Map<String, String> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        if (!flag.http) throw Throwable(message = "Network disabled")
        if (!isInternetAvailableDebounced()) throw Throwable(message = "No internet connection available")

        val response = get(urlString = path.path.encodeURLPath()) {
            queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
            block()
        }

        if (!response.status.isSuccess()) throw Throwable(message = "Unsuccessful response: ${response.status}")
        Result.Success(list = response.body<List<T>>())
    }.getOrElse { Result.Error(error = it) }
}

/**
 * Checks for internet availability with a debounce mechanism to avoid frequent checks.
 */
private suspend fun isInternetAvailableDebounced(): Boolean = runCatching {
    mutex.withLock {
        val previous = lastCheckTime.elapsed()
        if (previous < cacheTTL) return@withLock lastKnownStatus
        lastCheckTime = TimeSource.Monotonic.markNow()
        return@withLock isInternetAvailable().also { lastKnownStatus = it }
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = false)

/**
 * Returns the elapsed duration since the mark was set, or [INFINITE] if no mark has been set.
 */
private fun TimeMark?.elapsed(): Duration = this?.elapsedNow() ?: INFINITE

private const val TAG = "Network"
