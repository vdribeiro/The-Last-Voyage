package com.hybris.tlv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.core.locale.getLanguage
import com.hybris.tlv.data.translation.TranslationCache
import com.hybris.tlv.domain.translation.Translation
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.test.VisibleForTesting

internal val LocalTranslationState = staticCompositionLocalOf { TranslationCache.cacheState.value }

/**
 * Gets the translation cache.
 */
@Composable
internal fun getTranslationState(): Map<String, String> {
    val state by TranslationCache.cacheState.collectAsStateWithLifecycle()
    return state
}

/**
 * Gets a translation for a specific key.
 */
@Composable
internal fun getTranslation(key: String, vararg args: String): String {
    val cacheState = LocalTranslationState.current
    return remember(key1 = cacheState, key2 = key, key3 = args) { TranslationCache.get(key = key, args = args) }
}

@ExcludeFromTesting
internal data class PreviewTranslation(
    val key: String,
    val value: String
)

/**
 * Inject translations to be used in [Preview]s.
 */
@VisibleForTesting
@Composable
internal fun InjectTranslations(translations: List<PreviewTranslation>) {
    val languageIso = getLanguage()
    TranslationCache.set(translations = translations.map { Translation(languageIso = languageIso, key = it.key, value = it.value) })
}
