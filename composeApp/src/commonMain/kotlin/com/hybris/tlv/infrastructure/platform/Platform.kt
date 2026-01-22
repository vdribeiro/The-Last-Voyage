package com.hybris.tlv.infrastructure.platform

import com.hybris.tlv.test.ExcludeFromTesting

/**
 * Defines the possible platforms that the application can run on.
 */
@ExcludeFromTesting
internal sealed interface Platform {
    @ExcludeFromTesting
    data object Android: Platform
    @ExcludeFromTesting
    data object Ios: Platform
    @ExcludeFromTesting
    data object Windows: Platform
    @ExcludeFromTesting
    data object Mac: Platform
    @ExcludeFromTesting
    data object Linux: Platform
    @ExcludeFromTesting
    data object Web: Platform
    @ExcludeFromTesting
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
