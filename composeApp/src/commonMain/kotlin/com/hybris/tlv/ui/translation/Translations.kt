package com.hybris.tlv.ui.translation

import kotlinx.coroutines.withContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.domain.usecase.translation.TranslationUseCases

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
