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
        val update = { trySend(element = NetworkStatus(hasInternet = window.navigator.onLine)) }

        window.addEventListener(type = "online") { update() }
        window.addEventListener(type = "offline") { update() }

        update()

        awaitClose {
            window.removeEventListener(type = "online") {}
            window.removeEventListener(type = "offline") {}
        }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe network status", throwable = it)
        close(cause = it)
    }
}.distinctUntilChanged()

private const val TAG = "NetworkObserver"
