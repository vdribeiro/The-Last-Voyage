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
                val status = NetworkStatus(hasInternet = NetworkInterface.getNetworkInterfaces().asSequence().any { it.isUp && !it.isLoopback })
                trySend(element = status)
            }
        }, 0, POOLING_INTERVAL_MS)

        awaitClose { timer.cancel() }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe network status", throwable = it)
        close(cause = it)
    }
}.distinctUntilChanged()

private const val TAG = "NetworkObserver"
private const val POOLING_INTERVAL_MS = 3000L
