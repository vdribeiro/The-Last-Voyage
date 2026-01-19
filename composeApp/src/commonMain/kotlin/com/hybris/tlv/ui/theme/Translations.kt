package com.hybris.tlv.ui.theme

import kotlinx.coroutines.withContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.domain.usecase.translation.TranslationUseCases
import com.hybris.tlv.ui.translation.TranslationCache

/**
 * Refreshes the translations cache.
 */
@Composable
internal fun Translations(translation: TranslationUseCases) {
    LaunchedEffect(key1 = Unit) {
        withContext(context = Dispatcher.IO) {
            TranslationCache.set(translations = translation.getTranslations())
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
