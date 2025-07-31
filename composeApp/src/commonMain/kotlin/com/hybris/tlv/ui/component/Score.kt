package com.hybris.tlv.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun Score(
    isExpanded: Boolean?,
    score: String,
    utc: String,
    yearsTraveled: String,
    sensorRange: String,
    integrity: String,
    materials: String,
    fuel: String,
    cryopods: String
) {
    ScoreHeader(
        utc = utc,
        totalScore = score,
        isExpanded = isExpanded
    )

    AnimatedVisibility(
        visible = isExpanded != false,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {
            HorizontalDivider()
            Spacer(Modifier.height(height = 8.dp))
            InfoRow(label = getTranslation(key = "ship_years_traveled"), value = yearsTraveled)
            InfoRow(label = getTranslation(key = "ship_sensor"), value = sensorRange)
            InfoRow(label = getTranslation(key = "ship_integrity"), value = integrity)
            InfoRow(label = getTranslation(key = "ship_materials"), value = materials)
            InfoRow(label = getTranslation(key = "ship_fuel"), value = fuel)
            InfoRow(label = getTranslation(key = "ship_cryopods"), value = cryopods)
        }
    }
}

@Composable
private fun ScoreHeader(
    utc: String,
    totalScore: String,
    isExpanded: Boolean?
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(weight = 1f),
            text = utc,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = totalScore,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        if (isExpanded != null) {
            val arrowRotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)
            Spacer(Modifier.width(width = 8.dp))
            Icon(
                modifier = Modifier.rotate(degrees = arrowRotation),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
            )
        }
    }
}
