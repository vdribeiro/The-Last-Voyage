package com.hybris.tlv.core.platform

internal actual val platform: Platform by lazy {
    runCatching {
        val os = System.getProperty("os.name").lowercase()
        when {
            os.contains(other = "win") -> Platform.Windows
            os.contains(other = "mac") -> Platform.Mac
            os.contains(other = "nix") || os.contains(other = "nux") || os.contains(other = "aix") -> Platform.Linux
            else -> Platform.Unknown
        }
    }.getOrDefault(defaultValue = Platform.Unknown)
}
