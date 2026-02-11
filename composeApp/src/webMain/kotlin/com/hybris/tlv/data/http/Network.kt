package com.hybris.tlv.data.http

import kotlinx.browser.window
import com.hybris.tlv.core.telemetry.Telemetry

internal actual fun isInternetAvailable(): Boolean = runCatching {
    window.navigator.onLine
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check internet connection", throwable = it) }.getOrDefault(defaultValue = false)

private const val TAG = "Network"
