@file:ShadowedInTesting

package com.hybris.tlv.infrastructure.platform

import kotlinx.browser.window
import com.hybris.tlv.test.ShadowedInTesting

internal actual val isDebug: Boolean by lazy {
    runCatching {
        with(receiver = window.location) {
            search.contains(other = "debug=true") || hostname == "localhost" || hostname == "127.0.0.1"
        }
    }.getOrDefault(defaultValue = false)
}

internal actual val platform: Platform by lazy {
    Platform.Web
}
