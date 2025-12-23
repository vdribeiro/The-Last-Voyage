package com.hybris.tlv.usecase.translation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.hybris.tlv.locale.DEFAULT_LANGUAGE
import com.hybris.tlv.locale.getLanguage
import com.hybris.tlv.platform.Property
import com.hybris.tlv.usecase.translation.model.Translation

/**
 * Translations cache.
 */
internal object TranslationCache {

    data class CacheState(
        val languageIso: String = DEFAULT_LANGUAGE,
        val translations: Map<String, String> = defaultTranslations
    )

    private val defaultTranslations = listOf(
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
    ).toTranslationCacheMap()

    private val _cacheState = MutableStateFlow(value = CacheState())
    val cacheState: StateFlow<CacheState> = _cacheState.asStateFlow()

    fun reset() = _cacheState.update { CacheState() }

    fun set(translations: List<Translation>) {
        val languageIso = getLanguage()
        val translationMap = translations.toTranslationCacheMap().ifEmpty { defaultTranslations }
        _cacheState.update { currentState -> currentState.copy(languageIso = languageIso, translations = translationMap) }
    }

    fun get(key: String, vararg args: String): String {
        val state = _cacheState.value
        val languageIso = state.languageIso
        val translations = state.translations
        val rawValue = translations["${languageIso}__${key}"] ?: if (languageIso != DEFAULT_LANGUAGE) translations["${DEFAULT_LANGUAGE}__${key}"] ?: key else key
        if (args.isEmpty()) return rawValue
        return args.foldIndexed(initial = rawValue) { index, translation, arg -> translation.replace(oldValue = $$"%$${index + 1}$s", newValue = arg) }
    }

    private fun List<Translation>.toTranslationCacheMap(): Map<String, String> =
        associate { "${it.languageIso}__${it.key}" to it.value }
}
