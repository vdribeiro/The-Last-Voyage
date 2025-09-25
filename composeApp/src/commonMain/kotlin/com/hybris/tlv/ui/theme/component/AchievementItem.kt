package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography

@Composable
internal fun AchievementItem(
    modifier: Modifier = Modifier,
    name: String,
    description: String,
) {
    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(size = 12.dp)
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Image(
            //    painter = painterResource(resource = getDrawableResource(achievement.imageResource)),
            //    contentDescription = achievement.name,
            //    contentScale = ContentScale.Crop,
            //    modifier = Modifier
            //        .size(size = 64.dp)
            //        .clip(shape = CircleShape)
            //)

            Spacer(modifier = Modifier.width(width = 16.dp))

            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = name,
                    style = typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(height = 4.dp))
                Text(
                    text = description,
                    style = typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }
    }
}