package com.hybris.tlv.domain.usecase.translation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.hybris.tlv.core.locale.observeLocaleChanges
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.platform.Property

/**
 * Translations cache.
 */
internal object TranslationCache {
    private const val TAG = "TranslationCache"

    private val scope = CoroutineScope(context = SupervisorJob())

    private val defaultTranslations by lazy {
        mapOf(
            "app_name" to Property.APP_NAME,
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
     * Gets a translation for a specific key.
     */
    fun get(key: String, vararg args: String): String {
        val rawValue = _cacheState.value[key] ?: key
        if (args.isEmpty()) return rawValue
        return args.foldIndexed(initial = rawValue) { index, translation, arg -> translation.replace(oldValue = $$"%$${index + 1}$s", newValue = arg) }
    }

    /**
     * Observe system locale changes to refresh the cache.
     */
    fun registerListener(getTranslations: suspend () -> List<Translation>): Boolean =
        observeLocaleChanges {
            scope.launch {
                set(translations = getTranslations())
            }
        }
}
