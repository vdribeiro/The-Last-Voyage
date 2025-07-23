package com.hybris.tlv.locale

internal interface Locale {

    /**
     * Get the ISO 639-1 language.
     */
    fun getLanguage(): String

    fun getLocalDateTime(utc: String): String
}
