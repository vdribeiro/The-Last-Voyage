@file:ShadowedInTesting

package com.hybris.tlv.data.http

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.hybris.tlv.applicationContext
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun createHttpEngine(): HttpClientEngine = OkHttp.create()

internal actual fun isInternetAvailable(): Boolean = runCatching {
    val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check internet connection", throwable = it) }.getOrDefault(defaultValue = false)

private const val TAG = "HttpEngine"
