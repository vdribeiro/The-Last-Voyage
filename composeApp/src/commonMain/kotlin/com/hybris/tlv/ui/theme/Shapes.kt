package com.hybris.tlv.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

private val cornerExtraSmall = RoundedCornerShape(size = 4.0.dp)
private val cornerSmall = RoundedCornerShape(size = 8.0.dp)
private val cornerMedium = RoundedCornerShape(size = 12.0.dp)
private val cornerLarge = RoundedCornerShape(size = 16.0.dp)
private val cornerExtraLarge = RoundedCornerShape(size = 28.0.dp)

private val shapes = Shapes(
    extraSmall = cornerExtraSmall,
    small = cornerSmall,
    medium = cornerMedium,
    large = cornerLarge,
    extraLarge = cornerExtraLarge
)

internal val LocalShapes = staticCompositionLocalOf { shapes }
