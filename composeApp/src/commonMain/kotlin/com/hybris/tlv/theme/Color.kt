package com.hybris.tlv.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

private val primary = Color(color = 0xFF2C6FC2)
private val onPrimary = Color(color = 0xFF00214D)
private val primaryContainer = Color(color = 0xFF004A7C)
private val onPrimaryContainer = Color(color = 0xFFD0E6FF)
private val inversePrimary = Color(color = 0xFF40A9FF)
private val secondary = Color(color = 0xFF8D99AE)
private val onSecondary = Color(color = 0xFF1E293B)
private val secondaryContainer = Color(color = 0xFF334155)
private val onSecondaryContainer = Color(color = 0xFFCBD5E1)
private val tertiary = Color(color = 0xFFFB923C)
private val onTertiary = Color(color = 0xFF431407)
private val tertiaryContainer = Color(color = 0xFF7C2D12)
private val onTertiaryContainer = Color(color = 0xFFFFEDD5)
private val background = Color(color = 0xFF1C1B1F)
private val onBackground = Color(color = 0xFFE6E1E5)
private val surface = Color(color = 0xFF1C1B1F)
private val onSurface = Color(color = 0xFFE6E1E5)
private val surfaceVariant = Color(color = 0xFF49454F)
private val onSurfaceVariant = Color(color = 0xFFCAC4D0)
private val surfaceTint = Color(color = 0xFF2C6FC2)
private val inverseSurface = Color(color = 0xFFE6E1E5)
private val inverseOnSurface = Color(color = 0xFF313033)
private val error = Color(color = 0xFFF87171)
private val onError = Color(color = 0xFF450A0A)
private val errorContainer = Color(color = 0xFF991B1B)
private val onErrorContainer = Color(color = 0xFFFECACA)
private val outline = Color(color = 0xFF938F99)
private val outlineVariant = Color(color = 0xFF49454F)
private val scrim = Color(color = 0xFF000000)
private val surfaceBright = Color(color = 0xFF3B383E)
private val surfaceDim = Color(color = 0xFF141218)
private val surfaceContainer = Color(color = 0xFF211F26)
private val surfaceContainerHigh = Color(color = 0xFF2B2930)
private val surfaceContainerHighest = Color(color = 0xFF36343B)
private val surfaceContainerLow = Color(color = 0xFF1D1B20)
private val surfaceContainerLowest = Color(color = 0xFF0F0D13)

private val colorScheme = darkColorScheme(
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
    surfaceTint = surfaceTint,
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
    surfaceDim = surfaceDim,
    surfaceContainer = surfaceContainer,
    surfaceContainerHigh = surfaceContainerHigh,
    surfaceContainerHighest = surfaceContainerHighest,
    surfaceContainerLow = surfaceContainerLow,
    surfaceContainerLowest = surfaceContainerLowest,
)

/**
 * Alpha used when a component is enabled or disabled.
 */
internal fun alpha(enabled: Boolean): Float = if (enabled) 1f else 0.4f

internal val LocalColorScheme = compositionLocalOf { colorScheme }
