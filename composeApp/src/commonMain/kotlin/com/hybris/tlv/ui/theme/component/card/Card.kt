package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalShapes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Card(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    selected: Boolean = false,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val colorScheme = LocalColorScheme.current
    val shapes = LocalShapes.current
    val colors = when {
        selected -> CardDefaults.cardColors(containerColor = colorScheme.primaryContainer)
        else -> CardDefaults.cardColors()
    }
    val border = when {
        selected -> BorderStroke(width = 2.dp, color = colorScheme.primary)
        else -> BorderStroke(width = 1.dp, color = colorScheme.outline)
    }

    Card(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shapes.small,
        colors = colors,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = border,
        content = content
    )
}

@Preview
@Composable
private fun CardPreview() = AppTheme {
    Card()
}
