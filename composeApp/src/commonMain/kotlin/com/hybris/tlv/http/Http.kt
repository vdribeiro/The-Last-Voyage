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
import kotlinx.serialization.ExperimentalSerializationApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import com.hybris.tlv.TLV.flag
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.HttpClientFactory.Companion.CONNECT_TIMEOUT_MILLIS
import com.hybris.tlv.http.HttpClientFactory.Companion.REQUEST_TIMEOUT_MILLIS
import com.hybris.tlv.http.HttpClientFactory.Companion.SOCKET_TIMEOUT_MILLIS
import com.hybris.tlv.platform.isDebug
import com.hybris.tlv.telemetry.Telemetry

private val mutex = Mutex()
private val cacheTTL: Duration = if (isDebug) ZERO else 1.minutes
private var lastTimeMark: TimeMark? = null
private var lastNetworkQuality: NetworkQuality = NetworkQuality.Unknown

internal sealed interface NetworkQuality {
    data object Slow: NetworkQuality
    data object Medium: NetworkQuality
    data object Fast: NetworkQuality
    data object Unknown: NetworkQuality
}

/**
 * Performs a GET request to the URL [path], given a map of query parameters [queryMap] to be appended to the URL, and decodes the response body as a stream of objects of type [T].
 * This function handles network availability checks, URL encoding, query parameters, and JSON decoding.
 * It returns a [Result] object, which is either [Result.Success] containing the decoded list of objects, or [Result.Error] containing the exception that occurred.
 * An additional lambda [block] can also be provided for further configuration of the [HttpRequestBuilder].
 */
@OptIn(ExperimentalSerializationApi::class)
internal suspend inline fun <reified T> HttpClient.get(
    path: URL,
    queryMap: Map<String, String> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        if (!flag.http) throw Throwable(message = "Network disabled")
        val networkQuality = getNetworkQuality()
        if (networkQuality is NetworkQuality.Unknown) throw Throwable(message = "No internet connection available")

        val response = get(urlString = path.path.encodeURLPath()) {
            queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
            block()
            setTimeout(networkQuality = networkQuality)
        }

        if (!response.status.isSuccess()) throw Throwable(message = "Unsuccessful response: ${response.status}")
        Result.Success(list = response.body<List<T>>())
    }.getOrElse {
        invalidateCache()
        Result.Error(error = it)
    }
}

/**
 * Checks for network quality. A debounce is used to avoid frequent checks.
 */
private suspend fun HttpClient.getNetworkQuality(): NetworkQuality = runCatching {
    mutex.withLock {
        val previous = lastTimeMark.elapsed()
        if (previous < cacheTTL) return@withLock lastNetworkQuality
        if (!isInternetAvailable()) return@withLock NetworkQuality.Unknown

        lastTimeMark = TimeSource.Monotonic.markNow()
        val response = head(urlString = URL.Probe.path) {
            timeout {
                connectTimeoutMillis = SLOW_THRESHOLD_MILLIS
                socketTimeoutMillis = SLOW_THRESHOLD_MILLIS
                requestTimeoutMillis = SLOW_THRESHOLD_MILLIS
            }
        }

        // Check for Success or Redirect / Captive Portal
        if (!response.status.isSuccess() || !response.call.request.url.toString().contains(other = URL.Probe.path)) {
            return@runCatching NetworkQuality.Unknown
        }

        val elapsed = lastTimeMark.elapsed().inWholeMilliseconds
        when {
            elapsed < FAST_THRESHOLD_MILLIS -> NetworkQuality.Fast
            elapsed < MEDIUM_THRESHOLD_MILLIS -> NetworkQuality.Medium
            else -> NetworkQuality.Slow
        }.also { lastNetworkQuality = it }
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = NetworkQuality.Slow)

/**
 * Resets the cache to force a re-check on the next request.
 */
private suspend fun invalidateCache() = mutex.withLock { lastTimeMark = null }

/**
 * Returns the elapsed duration since the mark was set, or [INFINITE] if no mark has been set.
 */
private fun TimeMark?.elapsed(): Duration = this?.elapsedNow() ?: INFINITE

/**
 * Sets the timeout based on the [networkQuality].
 */
private fun HttpRequestBuilder.setTimeout(networkQuality: NetworkQuality) {
    val multiplier = when (networkQuality) {
        NetworkQuality.Fast -> 1L
        NetworkQuality.Medium -> 2L
        NetworkQuality.Slow -> 4L
        NetworkQuality.Unknown -> 1L
    }
    timeout {
        connectTimeoutMillis = (connectTimeoutMillis ?: CONNECT_TIMEOUT_MILLIS) * multiplier
        socketTimeoutMillis = (socketTimeoutMillis ?: SOCKET_TIMEOUT_MILLIS) * multiplier
        requestTimeoutMillis = (requestTimeoutMillis ?: REQUEST_TIMEOUT_MILLIS) * multiplier
    }
}

private const val TAG = "Network"
private const val FAST_THRESHOLD_MILLIS = 150L
private const val MEDIUM_THRESHOLD_MILLIS = 500L
private const val SLOW_THRESHOLD_MILLIS = 5000L
