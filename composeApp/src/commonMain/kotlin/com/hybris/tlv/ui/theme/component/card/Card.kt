package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Card(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    colors: CardColors = CardDefaults.cardColors(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    //TODO shape = RoundedCornerShape(size = 8.dp)

    Card(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
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
