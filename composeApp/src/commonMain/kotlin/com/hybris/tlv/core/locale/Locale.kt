package com.hybris.tlv.core.locale

import kotlinx.coroutines.flow.Flow

/**
 * Get the ISO 639-1 language.
 */
internal expect fun getLanguage(): String

/**
 * Get the date time in the locale appropriate format.
 */
internal expect fun getLocalDateTime(utc: String = now()): String

/**
 * Observe system locale changes and emit the new language.
 */
internal expect fun observeLocale(): Flow<String>
