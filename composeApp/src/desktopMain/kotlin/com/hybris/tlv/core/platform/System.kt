package com.hybris.tlv.core.platform

internal actual val isDebug: Boolean by lazy {
    runCatching {
        System.getProperty("debug") == "true"
    }.getOrDefault(defaultValue = false)
}
