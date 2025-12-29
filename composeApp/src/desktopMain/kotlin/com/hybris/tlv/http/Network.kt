@file:ShadowedInTesting

package com.hybris.tlv.http

import java.net.InetSocketAddress
import java.net.Socket
import kotlinx.coroutines.withContext
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual suspend fun isInternetAvailable(): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        Socket().use { it.connect(InetSocketAddress("8.8.8.8", 53), 1500) }
        true
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = false)
}

private const val TAG = "Network"
