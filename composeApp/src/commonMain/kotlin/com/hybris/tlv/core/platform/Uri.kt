package com.hybris.tlv.core.platform

import androidx.compose.ui.platform.UriHandler
import com.hybris.tlv.core.telemetry.Telemetry

/**
 * Safely attempts to open a [uri] using the provided [UriHandler].
 * This extension function provides a wrapper around [UriHandler.openUri] to handle common failures gracefully, such as:
 * - Null URI strings.
 * - Malformed or invalid URI formats.
 * - Lack of a corresponding application on the device to handle the URI.
 *
 * If the operation fails, the error is captured and logged instead of crashing the application.
 *
 * @param uri The string representation of the URI to open.
 */
internal fun UriHandler.open(uri: String?) {
    runCatching {
        uri ?: throw IllegalArgumentException("Uri is null")
        openUri(uri = uri)
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to open uri: $uri", throwable = it)
    }
}

private const val TAG = "Uri"