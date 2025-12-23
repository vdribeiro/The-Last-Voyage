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
        val translations: Map<String, Map<String, String>> = defaultTranslations
    )

    private val defaultTranslations = mapOf(
        DEFAULT_LANGUAGE to mapOf(
            "app_name" to Property.APP_NAME,
            "splash_screen__loading" to "Loading..."
        )
    )

    private val _cacheState = MutableStateFlow(value = CacheState())
    val cacheState: StateFlow<CacheState> = _cacheState.asStateFlow()

    fun reset() = _cacheState.update { CacheState() }

    fun set(translations: List<Translation>) {
        val languageIso = getLanguage()
        val translations = translations
            .groupBy { it.languageIso }
            .mapValues { entry -> entry.value.associate { it.key to it.value } }
            .ifEmpty { defaultTranslations }
        _cacheState.update { currentState -> currentState.copy(languageIso = languageIso, translations = translations) }
    }

    fun get(key: String, vararg args: String): String {
        val state = _cacheState.value
        val languageIso = state.languageIso
        val translations = state.translations
        val rawValue = translations[languageIso]?.get(key)
            ?: translations[DEFAULT_LANGUAGE]?.get(key)
            ?: key
        if (args.isEmpty()) return rawValue
        return args.foldIndexed(initial = rawValue) { index, translation, arg -> translation.replace(oldValue = $$"%$${index + 1}$s", newValue = arg) }
    }
}
