package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.text.Text
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SelectableAttribute(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    name: String = "",
    description: String = "",
    points: String = "",
) {
    val typography = LocalTypography.current

    Card(
        modifier = modifier.fillMaxWidth(),
        selected = selected
    ) {
        Row(
            modifier = Modifier
                .padding(all = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = name,
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(height = 4.dp))
                Text(text = description, style = typography.bodyMedium)
            }
            Spacer(modifier = Modifier.weight(weight = 0.1f))
            Text(
                text = points,
                style = typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
private fun SelectableCardPreview() = AppTheme {
    SelectableAttribute(
        name = "Property",
        description = "Hammer Time",
        points = "10"
    )
}
