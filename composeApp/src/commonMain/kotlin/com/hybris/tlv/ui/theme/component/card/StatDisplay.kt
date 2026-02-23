package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun StatDisplay(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    label: String? = null,
    value: String? = null,
) {
    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            icon?.let {
                Icon(
                    modifier = Modifier.size(size = 40.dp),
                    imageVector = it,
                    contentDescription = label,
                    tint = colorScheme.primary,
                    emptySize = 20.dp
                )
                Spacer(modifier = Modifier.width(width = 16.dp))
            }
            Column(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                label?.let {
                    Text(
                        text = it,
                        style = typography.bodyLarge,
                        color = colorScheme.onSurfaceVariant,
                    )
                }
                value?.let {
                    Text(
                        text = it,
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = Preview {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        StatDisplay(
            icon = Icons.Filled.Check,
            label = "Stat",
            value = "100"
        )
        StatDisplay(label = "Stat", value = "100")
        StatDisplay(icon = Icons.Filled.Check)
        StatDisplay(icon = Icons.Filled.Check, label = "Stat")
        StatDisplay(icon = Icons.Filled.Check, value = "100")
        StatDisplay(value = "100")
        StatDisplay(label = "Stat")
        StatDisplay()
    }
}

