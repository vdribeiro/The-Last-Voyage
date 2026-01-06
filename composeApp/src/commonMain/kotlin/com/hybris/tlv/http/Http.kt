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
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.head
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
import com.hybris.tlv.http.HttpClientFactory.Companion.CONNECT_TIMEOUT_MILLIS
import com.hybris.tlv.http.HttpClientFactory.Companion.REQUEST_TIMEOUT_MILLIS
import com.hybris.tlv.http.HttpClientFactory.Companion.SOCKET_TIMEOUT_MILLIS
import com.hybris.tlv.platform.isDebug
import com.hybris.tlv.serializer.decode
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
internal suspend inline fun <reified T> HttpClient.getStream(
    path: URL,
    queryMap: Map<String, String> = emptyMap(),
    noinline onProgress: ((Float) -> Unit)? = null,
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = withContext(context = Dispatcher.IO) {
    runCatching {
        if (!flag.http) throw Throwable(message = "Network disabled")
        prepareGet(urlString = path.path.encodeURLPath()) {
            val networkQuality = getNetworkQuality()
            if (networkQuality is NetworkQuality.Unknown) throw Throwable(message = "No internet connection available")

            queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
            block()
            setTimeout(networkQuality = networkQuality)
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

private fun HttpRequestBuilder.setTimeout(networkQuality: NetworkQuality) {
    val multiplier = when (networkQuality) {
        NetworkQuality.Fast -> 1L
        NetworkQuality.Medium -> 2L
        NetworkQuality.Slow -> 4L
        NetworkQuality.Unknown -> 0L
    }
    timeout {
        connectTimeoutMillis = (connectTimeoutMillis ?: CONNECT_TIMEOUT_MILLIS) * multiplier
        socketTimeoutMillis = (socketTimeoutMillis ?: SOCKET_TIMEOUT_MILLIS) * multiplier
        requestTimeoutMillis = (requestTimeoutMillis ?: REQUEST_TIMEOUT_MILLIS) * multiplier
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
            isInternetAvailable() -> {
                head(urlString = PROBE_ADDRESS) { timeout { requestTimeoutMillis = 1500L } }
                val elapsed = mark.elapsedNow().inWholeMilliseconds
                when {
                    elapsed < FAST_THRESHOLD_MILLIS -> NetworkQuality.Fast
                    elapsed < MEDIUM_THRESHOLD_MILLIS -> NetworkQuality.Medium
                    else -> NetworkQuality.Slow
                }
            }

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
internal suspend fun invalidateCache() =
    mutex.withLock { lastCheckTime = null }

private const val TAG = "ConnectivityManager"
private const val CHUNK_SIZE = 1024L * 8L
private const val PROBE_ADDRESS = "http://connectivitycheck.gstatic.com/generate_204"
private const val FAST_THRESHOLD_MILLIS = 150
private const val MEDIUM_THRESHOLD_MILLIS = 500
