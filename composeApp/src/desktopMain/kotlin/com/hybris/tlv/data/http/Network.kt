package com.hybris.tlv.data.http

import java.net.NetworkInterface
import com.hybris.tlv.core.telemetry.Telemetry

internal actual fun isInternetAvailable(): Boolean = runCatching {
    NetworkInterface.getNetworkInterfaces().asSequence().any { it.isUp && !it.isLoopback }
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to check internet connection", throwable = it)
}.getOrDefault(defaultValue = false)

private const val TAG = "Network"
