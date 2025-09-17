package com.hybris.tlv.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
internal fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LocalColorScheme.current,
        shapes = LocalShapes.current,
        typography = LocalTypography.current,
        content = content
    )
}
