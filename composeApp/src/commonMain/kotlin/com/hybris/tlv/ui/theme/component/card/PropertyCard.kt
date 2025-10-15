package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
internal fun PropertyCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
) {
    val typography = LocalTypography.current

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(all = 12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            name?.let {
                Text(
                    text = it,
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
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
private fun PropertyCardPreview() = AppTheme {
    PropertyCard(
        name = "Property",
        description = "Hammer Time",
    )
}
