package com.hybris.tlv.usecase.translation

import com.hybris.tlv.locale.getLanguage
import com.hybris.tlv.platform.Property
import com.hybris.tlv.usecase.translation.model.Translation

/**
 * Translations cache.
 */
internal object TranslationCache {

    const val DEFAULT_LANGUAGE = "en"
    private var languageIso = DEFAULT_LANGUAGE
    private val translations = listOf(
        Translation(
            languageIso = "en",
            key = "app_name",
            value = Property.APP_NAME
        ),
        Translation(
            languageIso = "en",
            key = "splash_screen__loading",
            value = "Loading..."
        ),
    )
    private val translationsCache = translations.toTranslationCacheMap().toMutableMap()

    fun set(translations: List<Translation>) {
        languageIso = getLanguage()
        translationsCache.clear()
        translationsCache.putAll(from = translations.toTranslationCacheMap())
    }

    fun get(key: String): String =
        translationsCache["${languageIso}__${key}"] ?: if (languageIso != DEFAULT_LANGUAGE) {
            translationsCache["${DEFAULT_LANGUAGE}__${key}"] ?: key
        } else key

    private fun List<Translation>.toTranslationCacheMap(): Map<String, String> =
        associate { "${it.languageIso}__${it.key}" to it.value }
}

fun getTranslation(key: String): String = TranslationCache.get(key)
