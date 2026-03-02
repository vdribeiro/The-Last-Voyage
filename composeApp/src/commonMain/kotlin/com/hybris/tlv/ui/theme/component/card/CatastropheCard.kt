package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun CatastropheCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
) {
    val typography = LocalTypography.current

    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    name?.let { Text(text = getTranslation(key = it), style = typography.titleLarge, fontWeight = FontWeight.Bold) }
                    if (name != null) Spacer(modifier = Modifier.width(width = 8.dp))
                }
                if (name != null && description != null) Spacer(modifier = Modifier.height(height = 4.dp))
                description?.let { Text(text = getTranslation(key = it), style = typography.bodyLarge) }
            }
            Spacer(modifier = Modifier.weight(weight = 0.1f))
        }
    }
}

@Preview
@Composable
private fun CatastropheCardPreview() = Preview {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        CatastropheCard(
            name = "Catastrophe",
            description = "Hammer Time",
        )
        CatastropheCard(name = "Catastrophe")
        CatastropheCard(description = "Hammer Time")
    }
}
