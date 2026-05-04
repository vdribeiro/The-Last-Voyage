package com.hybris.tlv.core.platform

internal actual val isDebug: Boolean by lazy {
    kotlin.native.Platform.isDebugBinary
}
