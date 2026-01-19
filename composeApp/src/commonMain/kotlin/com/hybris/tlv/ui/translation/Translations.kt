package com.hybris.tlv.ui.translation

import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.locale.observeLocaleChanges
import com.hybris.tlv.domain.usecase.translation.TranslationUseCases

/**
 * Observes the locale changes and refreshes the translations cache.
 */
@Composable
internal fun ObserveTranslations(translation: TranslationUseCases) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        observeLocaleChanges {
            scope.launch(context = Dispatcher.IO) {
                TranslationCache.set(translations = translationUseCases.getTranslations())
            }
        }
    }
}

/**
 * Gets a translation for a specific key.
 */
@Composable
internal fun getTranslation(key: String, vararg args: String): String {
    val cacheState = LocalTranslationState.current
    return remember(key1 = cacheState, key2 = key, key3 = args) { TranslationCache.get(key = key, args = args) }
}

/**
 * Gets translations for a list of keys.
 */
@Composable
internal fun getTranslations(keys: List<String>): List<String> {
    val cacheState = LocalTranslationState.current
    return remember(key1 = cacheState, key2 = keys) { keys.map { key -> TranslationCache.get(key = key) } }
}

internal val LocalTranslationState = staticCompositionLocalOf { TranslationCache.cacheState.value }
