package com.hybris.tlv.http

/**
 * Checks for internet availability.
 */
internal expect suspend fun isInternetAvailable(): Boolean
