package com.hybris.tlv.ui.theme

import androidx.compose.foundation.Indication
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
internal fun AppTheme(
    colorScheme: ColorScheme = AppTheme.colorScheme,
    shapes: Shapes = AppTheme.shapes,
    typography: Typography = AppTheme.typography,
    rippleIndication: Indication = AppTheme.rippleIndication,
    selectionColors: TextSelectionColors = AppTheme.selectionColors,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
        content = content,
    )
    // TODO - Design System
    //CompositionLocalProvider(
    //    LocalColorScheme provides colorScheme,
    //    LocalShapes provides shapes,
    //    LocalTypography provides typography,
    //    LocalIndication provides rippleIndication,
    //    LocalTextSelectionColors provides selectionColors,
    //) {
    //    ProvideTextStyle(value = typography.bodyLarge, content = content)
    //}
}

internal object AppTheme {
    val colorScheme: ColorScheme
        @Composable @ReadOnlyComposable get() = LocalColorScheme.current

    val typography: Typography
        @Composable @ReadOnlyComposable get() = LocalTypography.current
    val shapes: Shapes
        @Composable @ReadOnlyComposable get() = LocalShapes.current
    val rippleIndication: Indication
        @Composable @ReadOnlyComposable get() = LocalIndication.current
    val selectionColors: TextSelectionColors
        @Composable @ReadOnlyComposable get() = LocalTextSelectionColors.current
}

private val rippleIndication = ripple(bounded = true, radius = Dp.Unspecified, color = Color.Unspecified)
internal val LocalColorScheme = staticCompositionLocalOf { colorScheme }
internal val LocalShapes = staticCompositionLocalOf { shapes }
internal val LocalTypography = staticCompositionLocalOf { typography }
internal val LocalIndication = staticCompositionLocalOf<Indication> { rippleIndication }
internal val LocalTextSelectionColors = compositionLocalOf { selectionColors }
