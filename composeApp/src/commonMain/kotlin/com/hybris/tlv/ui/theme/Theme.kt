package com.hybris.tlv.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue

@Composable
internal fun AppTheme(
    vararg compositionValues: ProvidedValue<*>,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(*compositionValues, getTranslationProvidedValue()) {
        MaterialTheme(
            colorScheme = LocalColorScheme.current,
            shapes = LocalShapes.current,
            typography = LocalTypography.current,
            content = content
        )
    }
}
