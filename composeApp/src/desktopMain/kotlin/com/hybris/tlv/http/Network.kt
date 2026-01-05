@file:ShadowedInTesting

package com.hybris.tlv.http

import java.net.InetSocketAddress
import java.net.Socket
import kotlin.time.TimeSource
import kotlinx.coroutines.withContext
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.ConnectivityManager.FAST_THRESHOLD_MILLIS
import com.hybris.tlv.http.ConnectivityManager.MEDIUM_THRESHOLD_MILLIS
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

private val probeAddress by lazy { InetSocketAddress("8.8.8.8", 53) }

internal actual suspend fun getNetworkQuality(): NetworkQuality = withContext(context = Dispatcher.IO) {
    runCatching {
        val mark = TimeSource.Monotonic.markNow()
        Socket().use { it.connect(probeAddress, TIMEOUT) }
        val elapsed = mark.elapsedNow().inWholeMilliseconds
        when {
            elapsed < FAST_THRESHOLD_MILLIS -> NetworkQuality.Fast
            elapsed < MEDIUM_THRESHOLD_MILLIS -> NetworkQuality.Medium
            else -> NetworkQuality.Slow
        }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = NetworkQuality.Unknown)
}

private const val TAG = "Network"
private const val TIMEOUT = 1500
