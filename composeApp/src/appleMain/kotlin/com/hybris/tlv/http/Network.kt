@file:ShadowedInTesting

package com.hybris.tlv.http

import kotlin.time.TimeSource
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
import io.ktor.client.HttpClient
import io.ktor.client.request.head
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.ConnectivityManager.FAST_THRESHOLD_MILLIS
import com.hybris.tlv.http.ConnectivityManager.MEDIUM_THRESHOLD_MILLIS
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

private val probeClient by lazy { HttpClient() }

@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun getNetworkQuality(): NetworkQuality = withContext(context = Dispatcher.IO) {
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
            ) ?: return@withContext NetworkQuality.Unknown

            // Network status
            reachability.use {
                val networkStatus: SCNetworkReachabilityFlagsVar = alloc<SCNetworkReachabilityFlagsVar>()
                val success = SCNetworkReachabilityGetFlags(target = it, flags = networkStatus.ptr)
                if (!success) return@withContext NetworkQuality.Unknown

                val isReachable = (networkStatus.value and kSCNetworkReachabilityFlagsReachable) != 0u
                val needConnection = (networkStatus.value and kSCNetworkReachabilityFlagsConnectionRequired) != 0u

                if (isReachable && !needConnection) {
                    // Performs a lightweight HEAD request to measure actual round-trip time
                    val mark = TimeSource.Monotonic.markNow()
                    probeClient.use { httpClient -> httpClient.head(urlString = "https://www.google.com") }
                    val elapsed = mark.elapsedNow().inWholeMilliseconds
                    when {
                        elapsed < FAST_THRESHOLD_MILLIS -> NetworkQuality.Fast
                        elapsed < MEDIUM_THRESHOLD_MILLIS -> NetworkQuality.Medium
                        else -> NetworkQuality.Slow
                    }
                } else NetworkQuality.Unknown
            }
        }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = NetworkQuality.Unknown)
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
