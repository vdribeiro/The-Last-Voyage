package com.hybris.tlv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.hybris.tlv.usecase.translation.TranslationCache

@Composable
internal fun getTranslation(key: String, vararg args: String): String {
    val cacheState = LocalTranslationState.current
    return remember(key1 = cacheState, key2 = key, key3 = args) { TranslationCache.get(key = key, args = args) }
}

@Composable
internal fun TranslationProvider(content: @Composable () -> Unit) {
    val state by TranslationCache.cacheState.collectAsState()
    CompositionLocalProvider(value = LocalTranslationState provides state) { content() }
}

internal val LocalTranslationState = staticCompositionLocalOf { TranslationCache.cacheState.value }
