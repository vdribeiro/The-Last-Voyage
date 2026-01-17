@file:ShadowedInTesting

package com.hybris.tlv.data.http

import com.hybris.tlv.test.ShadowedInTesting

/**
 * Checks for internet availability.
 */
internal expect suspend fun isInternetAvailable(): Boolean
