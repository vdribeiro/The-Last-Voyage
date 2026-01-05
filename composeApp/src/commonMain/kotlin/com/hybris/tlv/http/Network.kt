@file:ShadowedInTesting

package com.hybris.tlv.http

import com.hybris.tlv.test.ShadowedInTesting

internal sealed interface NetworkQuality {
    data object Slow: NetworkQuality
    data object Medium: NetworkQuality
    data object Fast: NetworkQuality
    data object Unknown: NetworkQuality
}

/**
 * Get network quality.
 */
internal expect suspend fun getNetworkQuality(): NetworkQuality
