package com.hybris.tlv.core.platform

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual val isDebug: Boolean by lazy {
    kotlin.native.Platform.isDebugBinary
}

actual val platform: Platform = Platform.Ios
