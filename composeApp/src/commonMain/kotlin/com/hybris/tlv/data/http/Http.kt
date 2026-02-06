package com.hybris.tlv.data.http

import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.http.HttpClientFactory.Companion.CONNECT_TIMEOUT_MILLIS
import com.hybris.tlv.data.http.HttpClientFactory.Companion.REQUEST_TIMEOUT_MILLIS
import com.hybris.tlv.data.http.HttpClientFactory.Companion.SOCKET_TIMEOUT_MILLIS
import com.hybris.tlv.domain.flag.FeatureFlags.flags
import com.hybris.tlv.infrastructure.platform.Platform
import com.hybris.tlv.infrastructure.platform.platform

private val mutex: Mutex by lazy { Mutex() }
/**
 * Cache time to live for network quality checks.
 */
private val cacheTTL: Duration get() = if (flags.devMode) ZERO else 1.minutes
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
internal suspend inline fun <reified T> HttpClient.get(
    path: URL,
    queryMap: Map<String, String> = emptyMap(),
): Result<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        if (!flags.http) throw Throwable(message = "Network disabled")
        val networkQuality = getNetworkQuality()
        if (networkQuality is NetworkQuality.Unknown) throw Throwable(message = "No internet connection available")

        val response = get(urlString = path.path.encodeURLPath()) {
            queryMap.forEach { url.parameters.append(name = it.key, value = it.value) }
            setTimeout(networkQuality = networkQuality)
        }

        if (!response.status.isSuccess()) throw Throwable(message = "Unsuccessful response: ${response.status}")
        Result.Success(list = response.body<List<T>>())
    }.getOrElse { Result.Error(error = it) }
}

/**
 * Checks for network quality with a debounce mechanism to avoid frequent checks.
 */
private suspend fun HttpClient.getNetworkQuality(): NetworkQuality = withContext(context = Dispatcher.IO) {
    mutex.withLock {
        val previous = lastTimeMark.elapsed()
        if (previous < cacheTTL) return@withLock lastNetworkQuality
        lastTimeMark = TimeSource.Monotonic.markNow()

//        if (!isInternetAvailable()) return@withLock NetworkQuality.Unknown
        if (!flags.networkQuality || platform == Platform.Web) return@withLock NetworkQuality.Fast

        val response = runCatching {
            // Add small timeout to allow the HTTP client to return its own error gracefully
            withTimeout(timeMillis = SLOW_THRESHOLD_MILLIS + 500L) {
                get(urlString = URL.Probe.path) {
                    timeout {
                        connectTimeoutMillis = SLOW_THRESHOLD_MILLIS
                        socketTimeoutMillis = SLOW_THRESHOLD_MILLIS
                        requestTimeoutMillis = SLOW_THRESHOLD_MILLIS
                    }
                }
            }
        }.getOrElse {
            Telemetry.error(tag = TAG, message = "Unable to check network quality", throwable = it)
            return@withLock NetworkQuality.Unknown
        }

        // Check for Success or Redirect / Captive Portal
        if (!response.status.isSuccess() || !response.call.request.url.toString().contains(other = URL.Probe.path)) return@withLock NetworkQuality.Unknown

        val elapsed = lastTimeMark.elapsed().inWholeMilliseconds
        Telemetry.info(tag = TAG, message = "Network quality elapsed time: $elapsed")
        when {
            elapsed < FAST_THRESHOLD_MILLIS -> NetworkQuality.Fast
            elapsed < MEDIUM_THRESHOLD_MILLIS -> NetworkQuality.Medium
            else -> NetworkQuality.Slow
        }
    }.also {
        lastNetworkQuality = it
        Telemetry.info(tag = TAG, message = "Network quality: $it")
    }
}

/**
 * Returns the elapsed duration since the mark was set, or [INFINITE] if no mark has been set.
 */
private fun TimeMark?.elapsed(): Duration = runCatching {
    this?.elapsedNow()
}.getOrNull() ?: INFINITE

/**
 * Sets the timeout based on the [networkQuality].
 */
private fun HttpRequestBuilder.setTimeout(networkQuality: NetworkQuality) {
    val multiplier = when (networkQuality) {
        NetworkQuality.Slow -> 3L
        NetworkQuality.Medium -> 2L
        NetworkQuality.Fast,
        NetworkQuality.Unknown -> 1L
    }
    timeout {
        connectTimeoutMillis = (connectTimeoutMillis ?: CONNECT_TIMEOUT_MILLIS) * multiplier
        socketTimeoutMillis = (socketTimeoutMillis ?: SOCKET_TIMEOUT_MILLIS) * multiplier
        requestTimeoutMillis = (requestTimeoutMillis ?: REQUEST_TIMEOUT_MILLIS) * multiplier
    }
}

private const val TAG = "Http"
private const val FAST_THRESHOLD_MILLIS = 1000L
private const val MEDIUM_THRESHOLD_MILLIS = 2000L
private const val SLOW_THRESHOLD_MILLIS = 3000L
