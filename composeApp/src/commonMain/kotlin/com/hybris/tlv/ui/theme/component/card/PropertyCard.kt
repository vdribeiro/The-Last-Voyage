package com.hybris.tlv.ui.theme.component.card

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun PropertyCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
    icon: @Composable (() -> Unit) = { Icon() }
) {
    val typography = LocalTypography.current

    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(all = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(weight = 1f)) {
                name?.let {
                    Text(
                        text = it,
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (name != null && description != null) Spacer(modifier = Modifier.height(height = 4.dp))
                description?.let { Text(text = it, style = typography.bodyLarge) }
            }
            Spacer(modifier = Modifier.weight(weight = 0.1f))
            icon()
        }
    }
}

@Preview
@Composable
private fun PropertyCardPreview() = AppTheme {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        PropertyCard(
            name = "Property",
            description = "Hammer Time",
        )
        PropertyCard(name = "Property")
        PropertyCard(description = "Hammer Time")
        PropertyCard(
            name = "Property",
            description = "Hammer Time",
            icon = { Icon(imageVector = Icons.Filled.Check) }
        )
        PropertyCard(
            name = "Property",
            icon = { Icon(imageVector = Icons.Filled.Check) }
        )
        PropertyCard(icon = { Icon(imageVector = Icons.Filled.Check) })
    }
}
