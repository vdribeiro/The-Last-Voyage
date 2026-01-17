@file:ShadowedInTesting

package com.hybris.tlv.data.http

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.value
import kotlinx.coroutines.withContext
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFTypeRef
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithAddress
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.SCNetworkReachabilityRef
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable
import platform.posix.AF_INET
import platform.posix.sockaddr_in
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun isInternetAvailable(): Boolean = withContext(context = Dispatcher.IO) {
    runCatching {
        memScoped {
            // Create a IPv4 zero address
            val zeroAddress: sockaddr_in = alloc<sockaddr_in>().apply {
                sin_len = sizeOf<sockaddr_in>().toUByte()
                sin_family = AF_INET.toUByte()
            }
            // Check general internet reachability
            val reachability: SCNetworkReachabilityRef = SCNetworkReachabilityCreateWithAddress(
                allocator = null,
                address = zeroAddress.ptr.reinterpret()
            ) ?: return@withContext false
            // Network status
            reachability.use {
                val networkStatus: SCNetworkReachabilityFlagsVar = alloc<SCNetworkReachabilityFlagsVar>()
                val success = SCNetworkReachabilityGetFlags(target = it, flags = networkStatus.ptr)
                if (!success) return@withContext false
                val isReachable = (networkStatus.value and kSCNetworkReachabilityFlagsReachable) != 0u
                val needsConnection = (networkStatus.value and kSCNetworkReachabilityFlagsConnectionRequired) != 0u
                isReachable && !needsConnection
            }
        }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = false)
}

@OptIn(ExperimentalForeignApi::class)
private inline fun <T: CFTypeRef?, R> T.use(block: (T) -> R): R {
    try {
        return block(this)
    } finally {
        if (this != null) CFRelease(cf = this)
    }
}

private const val TAG = "Network"
