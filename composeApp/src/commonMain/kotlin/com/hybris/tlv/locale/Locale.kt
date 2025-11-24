package com.hybris.tlv.locale

/**
 * Get the ISO 639-1 language.
 */
internal expect fun getLanguage(): String

/**
 * Get the date time in the local format.
 */
internal expect fun getLocalDateTime(utc: String = now()): String
