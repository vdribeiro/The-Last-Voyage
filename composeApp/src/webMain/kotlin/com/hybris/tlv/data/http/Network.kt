@file:ShadowedInTesting

package com.hybris.tlv.data.http

import kotlinx.browser.window
import kotlinx.coroutines.withContext
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual suspend fun isInternetAvailable(): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        window.navigator.onLine
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = false)
}

private const val TAG = "Network"
