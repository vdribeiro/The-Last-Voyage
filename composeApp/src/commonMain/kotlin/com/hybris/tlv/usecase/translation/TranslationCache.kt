package com.hybris.tlv.usecase.translation

import com.hybris.tlv.locale.getLanguage
import com.hybris.tlv.usecase.translation.model.Translation

/**
 * Translations cache.
 * To avoid concurrency issues, we always access the cache in the main thread.
 * Tremble not, these are extremely fast, in-memory operations that typically complete in microseconds,
 * far from the threshold that triggers an ANR.
 */
internal object TranslationCache {

    const val DEFAULT_LANGUAGE = "en"
    private var languageIso = DEFAULT_LANGUAGE
    private val translations = listOf(
        Translation(
            languageIso = "en",
            key = "app_name",
            value = "The Last Voyage"
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
