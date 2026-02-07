@file:ShadowedInTesting

package com.hybris.tlv.data.http

import java.net.NetworkInterface
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.java.Java
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun createHttpEngine(): HttpClientEngine = Java.create()

internal actual fun isInternetAvailable(): Boolean = runCatching {
    NetworkInterface.getNetworkInterfaces().asSequence().any { it.isUp && !it.isLoopback }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check internet connection", throwable = it) }.getOrDefault(defaultValue = false)

private const val TAG = "HttpEngine"
