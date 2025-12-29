package com.hybris.tlv.platform

/**
 * Defines the possible platforms that the application can run on.
 */
internal sealed interface Platform {
    data object Android: Platform
    data object Ios: Platform
    data object Windows: Platform
    data object Mac: Platform
    data object Linux: Platform
    data object Unknown: Platform
}

/**
 * Indicates whether the application is running in a debug build.
 */
internal expect val isDebug: Boolean

/**
 * The current operating system [Platform].
 */
internal expect val platform: Platform
