package com.hybris.tlv.platform

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
internal actual val isDebug: Boolean by lazy {
    kotlin.native.Platform.isDebugBinary
}

internal actual val platform: Platform by lazy {
    Platform.Ios
}
