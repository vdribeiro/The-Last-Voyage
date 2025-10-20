package com.hybris.tlv.ui.theme.component.card

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalShapes
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.image.Image
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun AchievementCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
    image: String? = null,
    done: Boolean = false,
) {
    val typography = LocalTypography.current
    val shapes = LocalShapes.current

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(all = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            image?.let {
                Image(
                    modifier = Modifier
                        .size(size = 72.dp)
                        .clip(shape = shapes.small)
                        .align(alignment = Alignment.Top),
                    path = it,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.width(width = 16.dp))
            }
            Column(modifier = Modifier.weight(weight = 1f)) {
                name?.let {
                    Text(
                        text = it,
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(height = 4.dp))
                }
                description?.let { Text(text = it, style = typography.bodyLarge) }
            }
            Spacer(modifier = Modifier.weight(weight = 0.1f))
            if (done) Icon(
                imageVector = Icons.Filled.Check,
            )
        }
    }
}

@Preview
@Composable
private fun AchievementCardPreview() = AppTheme {
    Column {
        AchievementCard(
            name = "Achievement 1",
            description = "Achievement Description 1",
            done = false
        )
        AchievementCard(
            name = "Achievement 2",
            description = "Achievement Description 2",
            done = true
        )
    }
}
