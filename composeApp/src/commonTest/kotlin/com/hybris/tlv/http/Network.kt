@file:Suppress("unused", "RedundantSuspendModifier")

package com.hybris.tlv.http

import com.hybris.tlv.TLV.flags

/**
 * Checks for internet availability.
 */
internal suspend fun isInternetAvailable(): Boolean = flags.value.http
