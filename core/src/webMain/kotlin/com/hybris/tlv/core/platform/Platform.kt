package com.hybris.tlv.core.platform

import kotlinx.browser.window

actual val isDebug: Boolean by lazy {
    runCatching {
        with(receiver = window.location) {
            search.contains(other = "debug=true") || hostname == "localhost" || hostname == "127.0.0.1"
        }
    }.getOrDefault(defaultValue = false)
}

actual val platform: Platform = Platform.Web
