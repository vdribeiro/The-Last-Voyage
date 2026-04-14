package com.hybris.tlv.data.http

/**
 * Check if internet is available.
 *
 * @return `true` if the device has an active internet connection, `false` otherwise.
 */
internal expect fun isInternetAvailable(): Boolean
