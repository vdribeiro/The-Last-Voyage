package com.hybris.tlv.core.system

internal actual val isDebug: Boolean by lazy {
    runCatching {
        System.getProperty("debug") == "true"
    }.getOrDefault(defaultValue = false)
}
