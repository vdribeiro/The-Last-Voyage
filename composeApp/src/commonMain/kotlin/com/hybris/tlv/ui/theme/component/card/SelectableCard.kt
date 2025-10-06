package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SelectableCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val typography = LocalTypography.current

    val cardColors = if (selected) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    } else {
        CardDefaults.cardColors()
    }

    val border = if (selected) {
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = cardColors,
        border = border
    ) {
        Column(modifier = Modifier.weight(weight = 1f)) {
            name?.let {
                Text(text = it, style = typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(height = 4.dp))
            }
            description?.let {
                Text(text = it, style = typography.bodyMedium)
            }
        }
    }
}

@Preview
@Composable
private fun SelectableCardPreview() = AppTheme {
    SelectableCard(
        name = "Property",
        description = "Hammer Time",
    )
}
