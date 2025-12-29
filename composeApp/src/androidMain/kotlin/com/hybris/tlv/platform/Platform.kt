package com.hybris.tlv.platform

import com.hybris.tlv.BuildConfig

internal actual val isDebug: Boolean by lazy {
    BuildConfig.DEBUG
}

internal actual val platform: Platform by lazy {
    Platform.Android
}
