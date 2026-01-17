@file:ShadowedInTesting

package com.hybris.tlv.infrastructure.platform

import kotlin.experimental.ExperimentalNativeApi
import com.hybris.tlv.test.ShadowedInTesting

@OptIn(ExperimentalNativeApi::class)
internal actual val isDebug: Boolean by lazy {
    kotlin.native.Platform.isDebugBinary
}

internal actual val platform: Platform by lazy {
    Platform.Ios
}
