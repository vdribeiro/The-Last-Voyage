package com.hybris.tlv.platform

import com.hybris.tlv.BuildConfig

internal actual val isDebug: Boolean by lazy {
    BuildConfig.DEBUG
}
