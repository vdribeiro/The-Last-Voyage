@file:ShadowedInTesting

package com.hybris.tlv.locale

import com.hybris.tlv.test.ShadowedInTesting

/**
 * Get the ISO 639-1 language.
 */
internal expect fun getLanguage(): String

/**
 * Get the date time in the local format.
 */
internal expect fun getLocalDateTime(utc: String = now()): String

/**
 * Call [onChanged] when the system locale changes.
 * Returns false if locale changes cannot be observed.
 */
internal expect fun observeLocaleChanges(onChanged: () -> Unit): Boolean
