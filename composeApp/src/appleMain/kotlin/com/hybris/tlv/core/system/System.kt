package com.hybris.tlv.core.system

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
internal actual val isDebug: Boolean by lazy {
    Platform.isDebugBinary
}
