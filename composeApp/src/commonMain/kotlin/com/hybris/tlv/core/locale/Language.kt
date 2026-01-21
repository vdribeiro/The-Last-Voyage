package com.hybris.tlv.core.locale

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.domain.usecase.translation.model.Translation

// TODO - add more translations
/**
 * App default language.
 */
internal const val DEFAULT_LANGUAGE = "en"

private val scope = CoroutineScope(context = SupervisorJob())
/**
 * Observe system locale changes to refresh the translation cache.
 */
internal fun refreshTranslationCache(getTranslations: suspend () -> List<Translation>): Boolean =
    observeLocaleChanges {
        scope.launch {
            TranslationCache.set(translations = getTranslations())
        }
    }
