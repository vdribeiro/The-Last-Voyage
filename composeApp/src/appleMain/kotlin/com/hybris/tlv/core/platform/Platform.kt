package com.hybris.tlv.core.platform

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
internal actual val isDebug: Boolean by lazy {
    kotlin.native.Platform.isDebugBinary
}

internal actual val platform: Platform = Platform.Ios
