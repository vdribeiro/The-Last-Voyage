package com.hybris.tlv.platform

import com.hybris.tlv.telemetry.Telemetry

internal actual val isDebug: Boolean by lazy {
    runCatching {
        System.getProperty("debug") == "true"
    }.getOrDefault(defaultValue = false)
}

internal actual val platform: Platform by lazy {
    runCatching {
        val os = System.getProperty("os.name").lowercase()
        when {
            os.contains(other = "win") -> Platform.Windows
            os.contains(other = "mac") -> Platform.Mac
            os.contains(other = "nix") || os.contains(other = "nux") || os.contains(other = "aix") -> Platform.Linux
            else -> Platform.Unknown
        }
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to get platform", throwable = it) }.getOrDefault(defaultValue = Platform.Unknown)
}

private const val TAG = "Platform"
