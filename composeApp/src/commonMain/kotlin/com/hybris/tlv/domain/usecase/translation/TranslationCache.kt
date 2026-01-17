package com.hybris.tlv.domain.usecase.translation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.hybris.tlv.core.locale.getLanguage
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.platform.Property

/**
 * Translations cache.
 */
internal object TranslationCache {

    private val defaultTranslations = mapOf(
        "app_name" to Property.APP_NAME,
        "splash_screen__loading" to "Loading..."
    )

    private val _cacheState = MutableStateFlow(value = defaultTranslations)
    val cacheState: StateFlow<Map<String, String>> = _cacheState.asStateFlow()

    fun reset() = _cacheState.update { defaultTranslations }

    /**
     * Sets the translations for a specific language.
     */
    fun set(translations: List<Translation>, languageIso: String = getLanguage()) {
        val translations = translations
            .filter { it.languageIso == languageIso }
            .associate { it.key to it.value }
            .ifEmpty { defaultTranslations }
        _cacheState.update { translations }
    }

    /**
     * Gets a translation for a specific key.
     */
    fun get(key: String, vararg args: String): String {
        val rawValue = _cacheState.value[key] ?: key
        if (args.isEmpty()) return rawValue
        return args.foldIndexed(initial = rawValue) { index, translation, arg -> translation.replace(oldValue = $$"%$${index + 1}$s", newValue = arg) }
    }
}
