package com.hybris.tlv.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@Composable
internal fun AppTheme(content: @Composable () -> Unit) {
    val typography = getTypography()
    val providers = remember(key1 = typography) {
        buildList {
            add(element = LocalTypography provides typography)
        }.toTypedArray()
    }

    CompositionLocalProvider(values = providers) {
        MaterialTheme(
            colorScheme = LocalColorScheme.current,
            shapes = LocalShapes.current,
            typography = LocalTypography.current,
            content = content
        )
    }
}
