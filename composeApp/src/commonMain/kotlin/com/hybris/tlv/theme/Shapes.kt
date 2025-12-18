package com.hybris.tlv.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

private val extraSmall = RoundedCornerShape(size = 4.0.dp)
private val small = RoundedCornerShape(size = 8.0.dp)
private val medium = RoundedCornerShape(size = 12.0.dp)
private val large = RoundedCornerShape(size = 16.0.dp)
private val extraLarge = RoundedCornerShape(size = 28.0.dp)

private val shapes = Shapes(
    extraSmall = extraSmall,
    small = small,
    medium = medium,
    large = large,
    extraLarge = extraLarge
)

internal val LocalShapes = staticCompositionLocalOf { shapes }
