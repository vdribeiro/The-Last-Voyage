package com.hybris.tlv.core.platform

import com.hybris.tlv.BuildConfig

actual val isDebug: Boolean = BuildConfig.DEBUG

actual val platform: Platform = Platform.Android
