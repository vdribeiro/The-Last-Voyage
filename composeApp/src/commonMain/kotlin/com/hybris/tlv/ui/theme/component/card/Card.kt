package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalShapes
import com.hybris.tlv.ui.theme.component.text.Text
import androidx.compose.material3.Card as MaterialCard

@Composable
internal fun Card(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val colorScheme = LocalColorScheme.current
    val shapes = LocalShapes.current
    val colors = when {
        selected -> CardDefaults.cardColors(containerColor = colorScheme.onSecondary)
        else -> CardDefaults.cardColors()
    }
    val border = when {
        selected -> BorderStroke(width = 2.dp, color = colorScheme.outline)
        else -> null
    }

    MaterialCard(
        modifier = modifier,
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
    Card { Text(text = "Preview") }
}
