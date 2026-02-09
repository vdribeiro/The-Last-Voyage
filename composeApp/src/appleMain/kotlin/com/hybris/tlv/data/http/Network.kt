@file:ShadowedInTesting

package com.hybris.tlv.data.http

import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.value
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFTypeRef
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithAddress
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable
import platform.posix.AF_INET
import platform.posix.AF_INET6
import platform.posix.sockaddr
import platform.posix.sockaddr_in
import platform.posix.sockaddr_in6
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

@OptIn(ExperimentalForeignApi::class)
internal actual fun isInternetAvailable(): Boolean = runCatching {
    memScoped {
        checkAddress(address = alloc<sockaddr_in>().apply {
            sin_len = sizeOf<sockaddr_in>().toUByte()
            sin_family = AF_INET.toUByte()
        }.ptr.reinterpret()) || checkAddress(address = alloc<sockaddr_in6>().apply {
            sin6_len = sizeOf<sockaddr_in6>().toUByte()
            sin6_family = AF_INET6.toUByte()
        }.ptr.reinterpret())
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check internet connection", throwable = it) }.getOrDefault(defaultValue = false)

@OptIn(ExperimentalForeignApi::class)
private fun MemScope.checkAddress(address: CValuesRef<sockaddr>): Boolean =
    SCNetworkReachabilityCreateWithAddress(
        allocator = null,
        address = address
    )?.use {
        val networkStatus: SCNetworkReachabilityFlagsVar = alloc<SCNetworkReachabilityFlagsVar>()
        val success = SCNetworkReachabilityGetFlags(target = it, flags = networkStatus.ptr)
        if (!success) return false
        val isReachable = (networkStatus.value and kSCNetworkReachabilityFlagsReachable) != 0u
        val needsConnection = (networkStatus.value and kSCNetworkReachabilityFlagsConnectionRequired) != 0u
        isReachable && !needsConnection
    } ?: false

@OptIn(ExperimentalForeignApi::class)
private inline fun <T: CFTypeRef?, R> T.use(block: (T) -> R): R {
    try {
        return block(this)
    } finally {
        if (this != null) CFRelease(cf = this)
    }
}

private const val TAG = "Network"
