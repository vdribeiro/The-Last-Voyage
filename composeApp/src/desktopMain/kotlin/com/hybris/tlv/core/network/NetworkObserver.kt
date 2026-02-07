@file:ShadowedInTesting

package com.hybris.tlv.core.network

import java.net.NetworkInterface
import java.util.Timer
import java.util.TimerTask
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun observeNetworkStatus(): Flow<NetworkStatus> = callbackFlow {
    runCatching {
        val timer = Timer()
        timer.schedule(object: TimerTask() {
            override fun run() {
                trySend(element = NetworkStatus(hasInternet = hasInternet()))
            }
        }, 0, POOLING_INTERVAL_MS)

        trySend(element = NetworkStatus(hasInternet = hasInternet()))
        awaitClose { timer.cancel() }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe network status", throwable = it)
        trySend(element = NetworkStatus(hasInternet = hasInternet()))
        close(cause = it)
    }
}.distinctUntilChanged()

private fun hasInternet(): Boolean = runCatching {
    NetworkInterface.getNetworkInterfaces().asSequence().any { it.isUp && !it.isLoopback }
}.getOrDefault(defaultValue = false)

private const val TAG = "NetworkObserver"
private const val POOLING_INTERVAL_MS = 3000L
