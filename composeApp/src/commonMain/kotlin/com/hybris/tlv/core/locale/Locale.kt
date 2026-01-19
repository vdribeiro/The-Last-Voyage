@file:ShadowedInTesting

package com.hybris.tlv.core.locale

import com.hybris.tlv.test.ShadowedInTesting

/**
 * Get the ISO 639-1 language.
 */
internal expect fun getLanguage(): String

/**
 * Get the date time in the local format.
 */
internal expect fun getLocalDateTime(utc: String = now()): String
