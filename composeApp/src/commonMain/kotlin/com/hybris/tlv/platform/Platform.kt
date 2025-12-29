@file:ExcludeFromTesting

package com.hybris.tlv.platform

import com.hybris.tlv.test.ExcludeFromTesting

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
