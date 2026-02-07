@file:ShadowedInTesting

package com.hybris.tlv.core.network

import java.net.NetworkInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun observeNetworkStatus(): Flow<NetworkStatus> = callbackFlow {
    runCatching {
        while (true) {
            trySend(element = NetworkStatus(hasInternet = NetworkInterface.getNetworkInterfaces().asSequence().any { it.isUp && !it.isLoopback }))
            delay(timeMillis = 5000)
        }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe network status", throwable = it)
        close(cause = it)
    }
}.distinctUntilChanged().flowOn(context = Dispatcher.IO)

private const val TAG = "NetworkObserver"
