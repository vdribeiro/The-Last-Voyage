package com.hybris.tlv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.hybris.tlv.usecase.translation.TranslationCache

@Composable
internal fun getTranslation(key: String, vararg args: String): String {
    val cacheState = LocalTranslationState.current
    return remember(key1 = cacheState) { TranslationCache.get(key = key, args = args) }
}

internal val LocalTranslationState = staticCompositionLocalOf { TranslationCache.cacheState.value }
