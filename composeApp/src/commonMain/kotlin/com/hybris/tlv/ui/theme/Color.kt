package com.hybris.tlv.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val primary = Color(color = 0xFF2C6FC2)
private val onPrimary = Color(color = 0xFF381E72)
private val primaryContainer = Color(color = 0xFF4F378B)
private val onPrimaryContainer = Color(color = 0xFFEADDFF)
private val inversePrimary = Color(color = 0xFF6750A4)
private val secondary = Color(color = 0xFFCCC2DC)
private val onSecondary = Color(color = 0xFF332D41)
private val secondaryContainer = Color(color = 0xFF4A4458)
private val onSecondaryContainer = Color(color = 0xFFE8DEF8)
private val tertiary = Color(color = 0xFFEFB8C8)
private val onTertiary = Color(color = 0xFF492532)
private val tertiaryContainer = Color(color = 0xFF633B48)
private val onTertiaryContainer = Color(color = 0xFFFFD8E4)
private val background = Color(color = 0xFF1C1B1F)
private val onBackground = Color(color = 0xFFE6E1E5)
private val surface = Color(color = 0xFF1C1B1F)
private val onSurface = Color(color = 0xFFE6E1E5)
private val surfaceVariant = Color(color = 0xFF49454F)
private val onSurfaceVariant = Color(color = 0xFFCAC4D0)
private val inverseSurface = Color(color = 0xFFE6E1E5)
private val inverseOnSurface = Color(color = 0xFF313033)
private val error = Color(color = 0xFFF2B8B5)
private val onError = Color(color = 0xFF601410)
private val errorContainer = Color(color = 0xFF8C1D18)
private val onErrorContainer = Color(color = 0xFFF9DEDC)
private val outline = Color(color = 0xFF938F99)
private val outlineVariant = Color(color = 0xFF49454F)
private val scrim = Color(color = 0xFF000000)
private val surfaceBright = Color(red = 59, green = 56, blue = 62)
private val surfaceContainer = Color(red = 33, green = 31, blue = 38)
private val surfaceContainerHigh = Color(red = 43, green = 41, blue = 48)
private val surfaceContainerHighest = Color(red = 54, green = 52, blue = 59)
private val surfaceContainerLow = Color(red = 29, green = 27, blue = 32)
private val surfaceContainerLowest = Color(red = 15, green = 13, blue = 19)
private val surfaceDim = Color(red = 20, green = 18, blue = 24)

private val colorScheme = ColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    inversePrimary = inversePrimary,
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiary,
    onTertiary = onTertiary,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    surfaceTint = primary,
    inverseSurface = inverseSurface,
    inverseOnSurface = inverseOnSurface,
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    outline = outline,
    outlineVariant = outlineVariant,
    scrim = scrim,
    surfaceBright = surfaceBright,
    surfaceContainer = surfaceContainer,
    surfaceContainerHigh = surfaceContainerHigh,
    surfaceContainerHighest = surfaceContainerHighest,
    surfaceContainerLow = surfaceContainerLow,
    surfaceContainerLowest = surfaceContainerLowest,
    surfaceDim = surfaceDim,
)

/**
 * Alpha used when a component is enabled or disabled.
 */
internal fun alpha(enabled: Boolean): Float = if (enabled) 1f else 0.4f

internal val LocalColorScheme = compositionLocalOf { colorScheme }
