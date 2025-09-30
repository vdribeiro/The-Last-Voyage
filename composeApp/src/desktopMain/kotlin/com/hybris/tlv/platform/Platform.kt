package com.hybris.tlv.platform

private object Debug

internal actual val isDebug: Boolean by lazy {
    runCatching {
        val java = Debug::class.java
        val protocol = java.getResource("${java.simpleName}.class")?.protocol
        protocol == "file"
    }.getOrDefault(defaultValue = false)
}
