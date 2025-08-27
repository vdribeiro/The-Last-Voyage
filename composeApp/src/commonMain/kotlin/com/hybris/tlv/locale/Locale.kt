package com.hybris.tlv.locale

/**
 * Locale that defines the language and date time format.
 */
internal interface Locale {

    /**
     * Get the ISO 639-1 language.
     */
    fun getLanguage(): String

    /**
     * Get the date time in the local format.
     */
    fun getLocalDateTime(utc: String): String
}
