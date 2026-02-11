package com.hybris.tlv.data.http

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.hybris.tlv.applicationContext
import com.hybris.tlv.core.telemetry.Telemetry

internal actual fun isInternetAvailable(): Boolean = runCatching {
    val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check internet connection", throwable = it) }.getOrDefault(defaultValue = false)

private const val TAG = "Network"
