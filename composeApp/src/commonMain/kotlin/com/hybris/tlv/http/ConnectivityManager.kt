package com.hybris.tlv.http

import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.timeout
import io.ktor.client.request.head
import com.hybris.tlv.platform.isDebug
import com.hybris.tlv.telemetry.Telemetry

internal object ConnectivityManager {
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
     * Checks for network quality. A debounce is used to avoid frequent checks.
     */
    suspend fun HttpClient.getNetworkQuality(): NetworkQuality = runCatching {
        mutex.withLock {
            val cacheMark = TimeSource.Monotonic.markNow()
            val previous = lastCheckTime?.elapsedNow() ?: INFINITE
            if (previous < cacheTTL) return@withLock lastKnownQuality

            val requestMark = TimeSource.Monotonic.markNow()
            head(urlString = PROBE_ADDRESS) { timeout { requestTimeoutMillis = 1500L } }
            val elapsed = requestMark.elapsedNow().inWholeMilliseconds
            val networkQuality = when {
                elapsed < FAST_THRESHOLD_MILLIS -> NetworkQuality.Fast
                elapsed < MEDIUM_THRESHOLD_MILLIS -> NetworkQuality.Medium
                else -> NetworkQuality.Slow
            }

            lastKnownQuality = networkQuality
            lastCheckTime = cacheMark
            return@withLock lastKnownQuality
        }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = NetworkQuality.Unknown)

    /**
     * Resets the cache to force a re-check on the next request.
     */
    suspend fun invalidateCache() {
        mutex.withLock {
            lastCheckTime = null
        }
    }

    internal fun HttpTimeoutConfig.setTimeout() {
        connectTimeoutMillis = 15_000L
        socketTimeoutMillis = 30_000L
        requestTimeoutMillis = 120_000L
    }

    private const val TAG = "ConnectivityManager"
    private const val PROBE_ADDRESS = "http://connectivitycheck.gstatic.com/generate_204"
    private const val FAST_THRESHOLD_MILLIS = 150
    private const val MEDIUM_THRESHOLD_MILLIS = 500
}
