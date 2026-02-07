@file:ShadowedInTesting

package com.hybris.tlv.core.locale

import kotlinx.coroutines.flow.Flow
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
 * Observe system locale changes.
 */
internal expect fun observeLocaleChanges(): Flow<Unit>
