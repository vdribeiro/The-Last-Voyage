package com.hybris.tlv.http

import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.hybris.tlv.http.ConnectivityManager.cacheTTL
import com.hybris.tlv.platform.isDebug
import com.hybris.tlv.telemetry.Telemetry

internal object ConnectivityManager {

    private val mutex = Mutex()
    private val cacheTTL: Duration = if (isDebug) ZERO else 5.seconds
    private var lastCheckTime: TimeMark? = null
    private var lastKnownStatus: NetworkQuality = NetworkQuality.Unknown

    /**
     * Checks for internet availability with a debounce mechanism to avoid frequent checks.
     * This function uses a mutex to ensure thread safety and caches the internet status for a duration of [cacheTTL].
     */
    suspend fun getNetworkQualityDebounced(): NetworkQuality = runCatching {
        mutex.withLock {
            val mark = TimeSource.Monotonic.markNow()
            val previous = lastCheckTime?.elapsedNow() ?: INFINITE
            if (previous < cacheTTL) return@withLock lastKnownStatus
            lastKnownStatus = getNetworkQuality()
            lastCheckTime = mark
            return@withLock lastKnownStatus
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

    private const val TAG = "ConnectivityManager"

    const val FAST_THRESHOLD_MILLIS = 150
    const val MEDIUM_THRESHOLD_MILLIS = 500
}
