package com.hybris.tlv.ui.theme

import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.locale.observeLocaleChanges
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.TranslationUseCases

/**
 * Observes the locale changes and refreshes the translations cache.
 */
@Composable
internal fun ObserveTranslations(translation: TranslationUseCases) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        observeLocaleChanges {
            scope.launch(context = Dispatcher.IO) {
                translation.refreshCache()
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

internal val LocalTranslationState = staticCompositionLocalOf { TranslationCache.cacheState.value }
