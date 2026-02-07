@file:ShadowedInTesting

package com.hybris.tlv.core.network

import kotlinx.browser.window
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting
import org.w3c.dom.events.Event

internal actual fun observeNetworkStatus(): Flow<NetworkStatus> = callbackFlow {
    runCatching {
        val listener: ((Event) -> Unit) = { trySend(element = NetworkStatus(hasInternet = hasInternet())) }
        window.addEventListener(type = "online", callback = listener)
        window.addEventListener(type = "offline", callback = listener)

        trySend(element = NetworkStatus(hasInternet = hasInternet()))
        awaitClose {
            window.removeEventListener(type = "online", callback = listener)
            window.removeEventListener(type = "offline", callback = listener)
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
