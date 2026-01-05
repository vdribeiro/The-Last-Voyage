@file:ShadowedInTesting

package com.hybris.tlv.http

import kotlinx.coroutines.withContext
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.hybris.tlv.applicationContext
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual suspend fun getNetworkQuality(): NetworkQuality = withContext(context = Dispatcher.IO) {
    runCatching {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) {
            val speed = capabilities.linkDownstreamBandwidthKbps
            when {
                speed < MAX_2G_KBPS -> NetworkQuality.Slow
                speed < MAX_3G_KBPS -> NetworkQuality.Medium
                else -> NetworkQuality.Fast
            }
        } else NetworkQuality.Unknown
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = NetworkQuality.Unknown)
}

private const val TAG = "Network"
private const val MAX_2G_KBPS = 150
private const val MAX_3G_KBPS = 3000
