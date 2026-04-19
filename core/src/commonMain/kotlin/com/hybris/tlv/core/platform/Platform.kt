package com.hybris.tlv.core.platform

/**
 * Defines the possible platforms that the application can run on.
 */
sealed interface Platform {
    data object Android: Platform
    data object Ios: Platform
    data object Windows: Platform
    data object Mac: Platform
    data object Linux: Platform
    data object Web: Platform
    data object Unknown: Platform
}

/**
 * Indicates whether the application is running in a debug build.
 */
expect val isDebug: Boolean

/**
 * The current operating system [Platform].
 */
expect val platform: Platform
