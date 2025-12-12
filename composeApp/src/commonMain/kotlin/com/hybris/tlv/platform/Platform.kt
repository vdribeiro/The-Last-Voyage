package com.hybris.tlv.platform

internal sealed interface Platform {
    object Android: Platform
    object Ios: Platform
    object Windows: Platform
    object Mac: Platform
    object Linux: Platform
    object Unknown: Platform
}

internal expect val isDebug: Boolean

internal expect val platform: Platform
