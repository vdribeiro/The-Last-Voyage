package com.hybris.tlv.platform

import androidx.compose.ui.platform.UriHandler
import com.hybris.tlv.telemetry.Telemetry

internal fun UriHandler.open(uri: String?) {
    runCatching {
        uri ?: throw IllegalArgumentException("Uri is null")
        openUri(uri = uri)
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to open uri: $uri", throwable = it) }
}

private const val TAG = "Uri"