package com.hybris.tlv.platform

/**
 * Defines the possible platforms that the application can run on.
 */
internal sealed interface Platform {
    object Android: Platform
    object Ios: Platform
    object Windows: Platform
    object Mac: Platform
    object Linux: Platform
    object Unknown: Platform
}

/**
 * Indicates whether the application is running in a debug build.
 */
internal expect val isDebug: Boolean

/**
 * The current operating system [Platform].
 */
internal expect val platform: Platform
