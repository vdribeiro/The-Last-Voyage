package com.hybris.tlv.core.system

<<<<<<<< HEAD:composeApp/src/webMain/kotlin/com/hybris/tlv/core/system/System.kt
import kotlinx.browser.window

internal actual val isDebug: Boolean by lazy {
    runCatching {
        with(receiver = window.location) {
            search.contains(other = "debug=true") || hostname == "localhost" || hostname == "127.0.0.1"
        }
    }.getOrDefault(defaultValue = false)
}
========
internal actual val platform: Platform = Platform.Web
>>>>>>>> 2f58d05d8c40c2f2e45ad43fc6e4355f76c55aed:composeApp/src/webMain/kotlin/com/hybris/tlv/core/platform/Platform.kt
