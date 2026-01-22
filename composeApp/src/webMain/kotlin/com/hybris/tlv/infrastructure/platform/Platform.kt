@file:ShadowedInTesting

package com.hybris.tlv.infrastructure.platform

import kotlinx.browser.window
import com.hybris.tlv.test.ShadowedInTesting

internal actual val isDebug: Boolean by lazy {
    window.location.search.contains(other = "debug=true") ||
            window.location.hostname == "localhost" ||
            window.location.hostname == "127.0.0.1"
}

internal actual val platform: Platform by lazy {
    Platform.Web
}
