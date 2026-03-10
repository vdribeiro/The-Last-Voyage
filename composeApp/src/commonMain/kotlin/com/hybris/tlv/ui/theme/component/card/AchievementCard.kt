package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun AchievementCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
    trailingIcon: ImageVector? = null,
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
                name?.let { Text(text = it, style = typography.titleLarge, fontWeight = FontWeight.Bold) }
                if (name != null && description != null) Spacer(modifier = Modifier.height(height = 4.dp))
                description?.let { Text(text = it, style = typography.bodyLarge) }
            }
            Spacer(modifier = Modifier.weight(weight = 0.1f))
            trailingIcon?.let { Box(modifier.align(alignment = Alignment.CenterVertically)) { Icon(imageVector = it) } }
        }
    }
}

@Preview
@Composable
private fun AchievementCardPreview() = Preview {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        AchievementCard(
            name = "Star Quality",
            description = "Achieve the X Factor",
            trailingIcon = Icons.Filled.Check,
        )
        AchievementCard(
            name = "Very very long name of the achievement",
            description = "Very very long description of the achievement",
            trailingIcon = Icons.Filled.Check,
        )
        AchievementCard(
            name = "Star Quality"
        )
        AchievementCard(
            description = "Achieve the X Factor"
        )
        AchievementCard(
            trailingIcon = Icons.Filled.Check
        )
        AchievementCard(
            name = "Star Quality",
            description = "Achieve the X Factor",
        )
        AchievementCard(
            name = "Star Quality",
            trailingIcon = Icons.Filled.Check
        )
        AchievementCard(
            description = "Achieve the X Factor",
            trailingIcon = Icons.Filled.Check,
        )
    }
}
