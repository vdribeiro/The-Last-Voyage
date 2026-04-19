package com.hybris.tlv.domain.usecase.translation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.hybris.tlv.App
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.translation.model.Translation

/**
 * Translations cache.
 */
internal object TranslationCache {
    private const val TAG = "TranslationCache"

    private val defaultTranslations by lazy {
        mapOf(
            "app_name" to App.NAME,
            "splash_screen__loading" to "Loading..."
        )
    }

    private val _cacheState = MutableStateFlow(value = defaultTranslations)
    val cacheState: StateFlow<Map<String, String>> = _cacheState.asStateFlow()

    /**
     * Sets the translations for a specific language.
     */
    fun set(translations: List<Translation>) {
        val translations = translations
            .associate { it.key to it.value }
            .ifEmpty { defaultTranslations }
        _cacheState.update { translations }
        Telemetry.info(tag = TAG, message = "Refreshed translations cache")
    }

    /**
     * Gets a translation for a specific [key] with optional arguments [args].
     */
    fun get(key: String, vararg args: String): String =
        _cacheState.value.getTranslation(key = key, args = args)

    fun Map<String, String>.getTranslation(key: String, vararg args: String): String {
        val rawValue = this[key] ?: key
        return if (args.isEmpty()) rawValue else args.foldIndexed(initial = rawValue) { index, translation, arg ->
            translation.replace(oldValue = $$"%$${index + 1}$s", newValue = arg)
        }
    }
}
