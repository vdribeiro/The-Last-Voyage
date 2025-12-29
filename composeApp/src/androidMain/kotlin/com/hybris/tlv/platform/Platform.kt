@file:ExcludeFromTesting

package com.hybris.tlv.platform

import com.hybris.tlv.BuildConfig
import com.hybris.tlv.test.ExcludeFromTesting

internal actual val isDebug: Boolean by lazy {
    BuildConfig.DEBUG
}

internal actual val platform: Platform by lazy {
    Platform.Android
}
