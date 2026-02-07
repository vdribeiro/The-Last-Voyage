@file:ShadowedInTesting

package com.hybris.tlv.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.hybris.tlv.applicationContext
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun observeNetworkStatus(): Flow<NetworkStatus> = callbackFlow {
    runCatching {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callback = object: ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(element = NetworkStatus(hasInternet = true))
            }

            override fun onLost(network: Network) {
                trySend(element = NetworkStatus(hasInternet = false))
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                trySend(element = NetworkStatus(hasInternet = capabilities.hasInternet()))
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)

        trySend(element = NetworkStatus(hasInternet = hasInternet()))
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe network status", throwable = it)
        trySend(element = NetworkStatus(hasInternet = hasInternet()))
        close(cause = it)
    }
}.distinctUntilChanged()

private fun hasInternet(): Boolean = runCatching {
    val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork).hasInternet()
}.getOrDefault(defaultValue = false)

private fun NetworkCapabilities?.hasInternet(): Boolean = runCatching {
    this != null &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}.getOrDefault(defaultValue = false)

private const val TAG = "NetworkObserver"