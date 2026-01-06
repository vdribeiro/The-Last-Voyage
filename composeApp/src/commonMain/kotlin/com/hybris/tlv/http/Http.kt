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
import kotlinx.io.Buffer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.io.decodeFromSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.head
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import io.ktor.utils.io.readAvailable
import com.hybris.tlv.TLV.flag
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.HttpClientFactory.Companion.CONNECT_TIMEOUT_MILLIS
import com.hybris.tlv.http.HttpClientFactory.Companion.REQUEST_TIMEOUT_MILLIS
import com.hybris.tlv.http.HttpClientFactory.Companion.SOCKET_TIMEOUT_MILLIS
import com.hybris.tlv.platform.isDebug
import com.hybris.tlv.serializer.json
import com.hybris.tlv.telemetry.Telemetry

private val mutex = Mutex()
private val cacheTTL: Duration = if (isDebug) ZERO else 1.minutes
private var lastCheckTime: TimeMark? = null
private var lastKnownQuality: NetworkQuality = NetworkQuality.Unknown

internal sealed interface NetworkQuality {
    data object Slow: NetworkQuality
    data object Medium: NetworkQuality
    data object Fast: NetworkQuality
    data object Unknown: NetworkQuality
}

/**
 * Performs a GET request to the URL [path], given a map of query parameters [queryMap] to be appended to the URL,
 * and decodes the response body as a stream of objects of type [T].
 * This function handles network availability checks, URL encoding, query parameters, and JSON decoding.
 * It returns a [Result] object, which is either [Result.Success] containing the decoded list of objects,
 * or [Result.Error] containing the exception that occurred.
 * An additional lambda [block] can also be provided for further configuration of the [HttpRequestBuilder].
 */
@OptIn(ExperimentalSerializationApi::class)
internal suspend inline fun <reified T> HttpClient.getStream(
    path: URL,
    queryMap: Map<String, String> = emptyMap(),
    noinline onProgress: ((Float) -> Unit)? = null,
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        if (!flag.http) throw Throwable(message = "Network disabled")
        val networkQuality = getNetworkQuality()
        if (networkQuality is NetworkQuality.Unknown) throw Throwable(message = "No internet connection available")

        prepareGet(urlString = path.path.encodeURLPath()) {
            queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
            block()
            setTimeout(networkQuality = networkQuality)
        }.execute { httpResponse ->
            if (!httpResponse.status.isSuccess()) throw Throwable(message = "Unsuccessful response: ${httpResponse.status}")

            val channel = httpResponse.bodyAsChannel()
            val contentLength = httpResponse.contentLength() ?: -1L

            val sink = Buffer()
            val chunks = ByteArray(size = CHUNK_SIZE)
            var totalRead = 0L
            while (!channel.isClosedForRead) {
                val read = channel.readAvailable(buffer = chunks, offset = 0, length = chunks.size)
                if (read <= 0) break
                sink.write(source = chunks, startIndex = 0, endIndex = read)
                totalRead += read

                val progress = if (contentLength > 0) totalRead.toFloat() / contentLength else -1F
                onProgress?.invoke(progress)
            }
            onProgress?.invoke(1f)
            Result.Success(list = json.decodeFromSource<List<T>>(source = sink))
        }
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
        val previous = lastCheckTime?.elapsedNow() ?: INFINITE
        if (previous < cacheTTL) return@withLock lastKnownQuality

        val mark = TimeSource.Monotonic.markNow()
        when {
            isInternetAvailable() -> runCatching {
                head(urlString = URL.Probe.path) {
                    timeout {
                        connectTimeoutMillis = SLOW_THRESHOLD_MILLIS
                        socketTimeoutMillis = SLOW_THRESHOLD_MILLIS
                        requestTimeoutMillis = SLOW_THRESHOLD_MILLIS
                    }
                }
                val elapsed = mark.elapsedNow().inWholeMilliseconds
                when {
                    elapsed < FAST_THRESHOLD_MILLIS -> NetworkQuality.Fast
                    elapsed < MEDIUM_THRESHOLD_MILLIS -> NetworkQuality.Medium
                    else -> NetworkQuality.Slow
                }
            }.getOrDefault(defaultValue = NetworkQuality.Slow)

            else -> NetworkQuality.Unknown
        }.also {
            lastKnownQuality = it
            lastCheckTime = mark
        }
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = NetworkQuality.Unknown)

/**
 * Resets the cache to force a re-check on the next request.
 */
private suspend fun invalidateCache() =
    mutex.withLock { lastCheckTime = null }

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
private const val CHUNK_SIZE = 1024 * 8
private const val FAST_THRESHOLD_MILLIS = 150L
private const val MEDIUM_THRESHOLD_MILLIS = 500L
private const val SLOW_THRESHOLD_MILLIS = 5000L
