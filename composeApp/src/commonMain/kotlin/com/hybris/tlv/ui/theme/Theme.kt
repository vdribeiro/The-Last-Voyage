package com.hybris.tlv.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hybris.tlv.usecase.translation.TranslationCache

@Composable
internal fun AppTheme(content: @Composable () -> Unit) {
    val state by TranslationCache.cacheState.collectAsState()
    CompositionLocalProvider(value = LocalTranslationState provides state) {
        MaterialTheme(
            colorScheme = LocalColorScheme.current,
            shapes = LocalShapes.current,
            typography = LocalTypography.current,
            content = content
        )
    }
}
