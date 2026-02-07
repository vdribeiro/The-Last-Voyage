@file:ShadowedInTesting

package com.hybris.tlv.data.http

import kotlinx.browser.window
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun createHttpEngine(): HttpClientEngine = Js.create()

internal actual fun isInternetAvailable(): Boolean = runCatching {
    window.navigator.onLine
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check internet connection", throwable = it) }.getOrDefault(defaultValue = false)

private const val TAG = "HttpEngine"
