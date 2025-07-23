package com.hybris.tlv.usecase.translation

import com.hybris.tlv.usecase.translation.mapper.toTranslationCacheMap
import com.hybris.tlv.usecase.translation.model.domain.Translation

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

    fun set(languageIso: String?) {
        this.languageIso = languageIso ?: DEFAULT_LANGUAGE
    }

    fun set(translations: Map<String, String>) {
        translationsCache.putAll(from = translations)
    }

    fun get(key: String): String =
        translationsCache["${languageIso}__${key}"] ?: if (languageIso != DEFAULT_LANGUAGE) {
            translationsCache["${DEFAULT_LANGUAGE}__${key}"] ?: key
        } else key
}

fun getTranslation(key: String): String = TranslationCache.get(key)
