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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            name?.let { Text(text = getTranslation(key = it), style = typography.titleLarge, fontWeight = FontWeight.Bold) }
            if (name != null && description != null) Spacer(modifier = Modifier.height(height = 4.dp))
            description?.let { Text(text = getTranslation(key = it), style = typography.bodyLarge) }
        }
    }
}

@Preview
@Composable
private fun CatastropheCardPreview() = Preview {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        CatastropheCard(
            name = "Catastrophe",
            description = "Boom",
        )
        CatastropheCard(
            name = "Very very long name of the catastrophe",
            description = "Very very long and longer description of the catastrophe",
        )
        CatastropheCard(name = "Catastrophe")
        CatastropheCard(description = "Boom")
    }
}
