@file:ShadowedInTesting

package com.hybris.tlv.http

import java.net.InetSocketAddress
import java.net.Socket
import kotlinx.coroutines.withContext
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual suspend fun getNetworkQuality(): NetworkQuality = withContext(context = Dispatcher.IO) {
    runCatching {
        val start = System.currentTimeMillis()
        Socket().use { it.connect(InetSocketAddress("8.8.8.8", 53), TIMEOUT) }
        val time = System.currentTimeMillis() - start
        when {
            time < FAST_MILLIS -> NetworkQuality.Fast
            time < MEDIUM_MILLIS -> NetworkQuality.Medium
            else -> NetworkQuality.Slow
        }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = NetworkQuality.Unknown)
}

private const val TAG = "Network"
private const val TIMEOUT = 1500
private const val FAST_MILLIS = 150
private const val MEDIUM_MILLIS = 500
