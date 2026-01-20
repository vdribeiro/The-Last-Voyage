package com.hybris.tlv.core.locale

/**
 * App default language.
 */
internal const val DEFAULT_LANGUAGE = "en"

// TODO - more translations
internal sealed class SupportedLanguage(val iso: String) {
    data object EN_US: SupportedLanguage(iso = "en-us")
    data object PT_PT: SupportedLanguage(iso = "pt-pt")
}