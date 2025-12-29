@file:Suppress("unused", "RedundantSuspendModifier")

package com.hybris.tlv.http

import com.hybris.tlv.TLV.flag

/**
 * Checks for internet availability.
 */
internal suspend fun isInternetAvailable(): Boolean = flag.http
