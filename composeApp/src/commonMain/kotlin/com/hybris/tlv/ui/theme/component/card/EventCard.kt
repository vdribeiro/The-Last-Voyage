package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
internal fun EventCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    parent: String? = null,
    description: String? = null,
    outcome: String? = null
) {
    val typography = LocalTypography.current

    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 2.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            name?.let { Text(text = it, style = typography.titleLarge, fontWeight = FontWeight.Bold) }
            parent?.let { Text(text = it, style = typography.titleSmall) }
            description?.let { Text(text = it, style = typography.bodyLarge) }
            outcome?.let { Text(text = it, style = typography.bodySmall) }
        }
    }
}

@Preview
@Composable
private fun EventCardPreview() = Preview {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        EventCard(
            name = "Event",
            parent = "Parent",
            description = "Something happened",
            outcome = "+1 materials\n-1 fuel"
        )
        EventCard(
            name = "Very very long name of the event",
            parent = "Very very long parent of the event that has a very long name",
            description = "Very very long and longer description of the event",
            outcome = "+1 materials and all the other stuff and things that are very very long"
        )
        EventCard(
            name = "Event",
            parent = "Parent",
            description = "Something happened",
        )
        EventCard(
            name = "Event",
            description = "Something happened",
        )
        EventCard(
            name = "Event",
            description = "Something happened",
            outcome = "+1 materials\n-1 fuel"
        )
    }
}
