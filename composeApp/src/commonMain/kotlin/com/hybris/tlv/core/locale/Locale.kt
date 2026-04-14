package com.hybris.tlv.core.locale

import kotlinx.coroutines.flow.Flow

/**
 * Returns the current language code based on the ISO 639-1 standard.
 * * @return A two-letter language code (e.g., "en", "fr", "es").
 */
internal expect fun getLanguage(): String

/**
 * Converts a UTC ISO-8601 string into a human-readable date-time string formatted according to the user's current locale.
 *
 * @param utc The source time string in UTC ISO-8601 format. Defaults to the current time via [now].
 * @return A localized string representation of the date and time.
 */
internal expect fun getLocalDateTime(utc: String = now()): String

/**
 * Observe the system-level locale changes.
 * Every time the user changes their language or region settings, this flow emits the new ISO 639-1 language code.
 *
 * @return A [Flow] of strings representing the updated language codes.
 */
internal expect fun observeLocale(): Flow<String>
