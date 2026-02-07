@file:ShadowedInTesting

package com.hybris.tlv.core.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.dispatch_get_global_queue
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun observeNetworkStatus(): Flow<NetworkStatus> = callbackFlow {
    runCatching {
        val monitor = nw_path_monitor_create()
        nw_path_monitor_set_update_handler(monitor = monitor) { path ->
            val status = nw_path_get_status(path = path)
            trySend(element = NetworkStatus(hasInternet = status == nw_path_status_satisfied))
        }

        val queue = dispatch_get_global_queue(
            identifier = DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(),
            flags = 0u
        )
        nw_path_monitor_set_queue(monitor = monitor, queue = queue)
        nw_path_monitor_start(monitor = monitor)

        awaitClose { nw_path_monitor_cancel(monitor = monitor) }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe network status", throwable = it)
        trySend(element = NetworkStatus(hasInternet = false))
        close(cause = it)
    }
}.distinctUntilChanged()

private const val TAG = "NetworkObserver"
