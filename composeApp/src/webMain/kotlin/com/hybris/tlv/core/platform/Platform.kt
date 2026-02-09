package com.hybris.tlv.core.platform

import kotlinx.browser.window

internal actual val isDebug: Boolean by lazy {
    runCatching {
        with(receiver = window.location) {
            search.contains(other = "debug=true") || hostname == "localhost" || hostname == "127.0.0.1"
        }
    }.getOrDefault(defaultValue = false)
}

internal actual val platform: Platform = Platform.Web
