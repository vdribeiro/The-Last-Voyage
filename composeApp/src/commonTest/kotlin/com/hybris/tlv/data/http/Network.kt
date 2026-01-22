@file:Suppress("unused", "RedundantSuspendModifier")

package com.hybris.tlv.data.http

import com.hybris.tlv.domain.flag.FeatureFlags.flags

/**
 * Checks for internet availability.
 */
internal suspend fun isInternetAvailable(): Boolean = flags.http
