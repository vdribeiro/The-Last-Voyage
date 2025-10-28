package com.hybris.tlv.platform

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
internal actual val isDebug: Boolean by lazy {
    Platform.isDebugBinary
}

internal actual fun getPlatform(): Platform = Platform.Ios
