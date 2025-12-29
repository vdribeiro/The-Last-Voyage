@file:ShadowedInTesting

package com.hybris.tlv.platform

import com.hybris.tlv.test.ShadowedInTesting

/**
 * Indicates whether the application is running in a debug build.
 */
internal expect val isDebug: Boolean

/**
 * The current operating system [Platform].
 */
internal expect val platform: Platform
