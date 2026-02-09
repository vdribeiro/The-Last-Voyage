package com.hybris.tlv.core.platform

import com.hybris.tlv.BuildConfig

internal actual val isDebug: Boolean = BuildConfig.DEBUG

internal actual val platform: Platform = Platform.Android
