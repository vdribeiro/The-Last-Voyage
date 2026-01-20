package com.hybris.tlv.ui.theme

import kotlinx.coroutines.withContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.domain.usecase.translation.model.Translation

internal val LocalTranslationState = staticCompositionLocalOf { TranslationCache.cacheState.value }

/**
 * Gets the provided value.
 */
@Composable
internal fun getTranslationProvidedValue(): ProvidedValue<Map<String, String>> {
    val state by TranslationCache.cacheState.collectAsStateWithLifecycle()
    return LocalTranslationState provides state
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
 * Refresh translations on recompose.
 */
@Composable
internal fun RefreshTranslations(getTranslations: suspend () -> List<Translation>) {
    LaunchedEffect(key1 = Unit) {
        val translations = withContext(context = Dispatcher.IO) { getTranslations() }
        TranslationCache.set(translations = translations)
    }
}

/**
 * Inject translations.
 */
@Composable
internal fun InjectTranslations(translations: List<Translation>) {
    InjectTranslations(translations = translations)
}
