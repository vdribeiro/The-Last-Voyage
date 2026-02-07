@file:ShadowedInTesting

package com.hybris.tlv.core.network

import kotlinx.browser.window
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun observeNetworkStatus(): Flow<NetworkStatus> = callbackFlow {
    runCatching {
        window.addEventListener(type = "online") { trySend(element = NetworkStatus(hasInternet = hasInternet())) }
        window.addEventListener(type = "offline") { trySend(element = NetworkStatus(hasInternet = hasInternet())) }

        trySend(element = NetworkStatus(hasInternet = hasInternet()))
        awaitClose {
            window.removeEventListener(type = "online") {}
            window.removeEventListener(type = "offline") {}
        }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe network status", throwable = it)
        trySend(element = NetworkStatus(hasInternet = hasInternet()))
        close(cause = it)
    }
}.distinctUntilChanged()

private fun hasInternet(): Boolean = runCatching {
    window.navigator.onLine
}.getOrDefault(defaultValue = false)

private const val TAG = "NetworkObserver"
