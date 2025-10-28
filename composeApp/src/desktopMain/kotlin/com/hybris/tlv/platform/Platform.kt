package com.hybris.tlv.platform

import com.hybris.tlv.telemetry.Telemetry

private object Debug

internal actual val isDebug: Boolean by lazy {
    runCatching {
        val java = Debug::class.java
        val protocol = java.getResource("${java.simpleName}.class")?.protocol
        protocol == "file"
    }.getOrDefault(defaultValue = false)
}

internal actual fun getPlatform(): Platform = runCatching {
    val os = System.getProperty("os.name").lowercase()
    when {
        os.contains(other = "win") -> Platform.Windows
        os.contains(other = "mac") -> Platform.Mac
        os.contains(other = "nix") || os.contains(other = "nux") || os.contains(other = "aix") -> Platform.Linux
        else -> Platform.Unknown
    }
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to get platform", throwable = it)
    Platform.Unknown
}

private const val TAG = "Platform"
