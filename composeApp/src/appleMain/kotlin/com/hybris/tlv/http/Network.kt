package com.hybris.tlv.http

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.value
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithAddress
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable
import platform.posix.AF_INET
import platform.posix.sockaddr_in
import com.hybris.tlv.telemetry.Telemetry

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun isInternetAvailable(): Boolean = runCatching {
    memScoped {
        // Create a IPv4 zero address
        val zeroAddress = alloc<sockaddr_in>().apply {
            sin_len = sizeOf<sockaddr_in>().toUByte()
            sin_family = AF_INET.toUByte()
        }
        // Check general internet reachability
        val reachability = SCNetworkReachabilityCreateWithAddress(
            allocator = null,
            address = zeroAddress.ptr.reinterpret()
        ) ?: return false
        // Network status
        val networkStatus = alloc<SCNetworkReachabilityFlagsVar>()
        val success = SCNetworkReachabilityGetFlags(target = reachability, flags = networkStatus.ptr)
        if (!success) return false
        val isReachable = (networkStatus.value and kSCNetworkReachabilityFlagsReachable) != 0u
        val needsConnection = (networkStatus.value and kSCNetworkReachabilityFlagsConnectionRequired) != 0u

        isReachable && !needsConnection
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = false)

private const val TAG = "Network"
