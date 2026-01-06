@file:ShadowedInTesting

package com.hybris.tlv.http

import java.net.NetworkInterface
import kotlinx.coroutines.withContext
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual suspend fun isInternetAvailable(): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        NetworkInterface.getNetworkInterfaces().asSequence().any { it.isUp && !it.isLoopback }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = false)
}

private const val TAG = "Network"
