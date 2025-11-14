package com.hybris.tlv.ui.theme.component.card

import org.jetbrains.compose.ui.tooling.preview.Preview
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.divider.Divider
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun Score(
    modifier: Modifier = Modifier,
    isExpanded: Boolean? = null,
    score: Double? = null,
    utc: String? = null,
    settledPlanet: String? = null,
    habitability: Double? = null,
    engine: String? = null,
    assignedPoints: Int? = null,
    yearsTraveled: Double? = null,
    sensorRange: Int? = null,
    integrity: Int? = null,
    fuel: Int? = null,
    materials: Int? = null,
    cryopods: Int? = null
) {
    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val settledPlanetTranslation = remember(key1 = translationVersion) { getTranslation(key = "settled_planet") }
    val habitabilityTranslation = remember(key1 = translationVersion) { getTranslation(key = "final_habitability") }
    val engineTranslation = remember(key1 = translationVersion) { getTranslation(key = "engine") }
    val assignedPointsTranslation = remember(key1 = translationVersion) { getTranslation(key = "points") }
    val yearsTraveledTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_years_traveled") }
    val sensorTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_sensor") }
    val integrityTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_integrity") }
    val fuelTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_fuel") }
    val materialsTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_materials") }
    val cryopodsTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_cryopods") }

    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            if (score != null && utc != null) ScoreHeader(
                utc = utc,
                totalScore = score.roundTo(decimalPlaces = 2).toString(),
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
                    Divider()
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    settledPlanet?.let { InfoRow(label = settledPlanetTranslation, value = it) }
                    habitability?.let { InfoRow(label = habitabilityTranslation, value = it.roundTo(decimalPlaces = 2)) }
                    engine?.let { InfoRow(label = engineTranslation, value = getTranslation(key = it)) }
                    assignedPoints?.let { InfoRow(label = assignedPointsTranslation, value = it) }
                    yearsTraveled?.let { InfoRow(label = yearsTraveledTranslation, value = it.roundTo(decimalPlaces = 2)) }
                    sensorRange?.let { InfoRow(label = sensorTranslation, value = it) }
                    integrity?.let { InfoRow(label = integrityTranslation, value = it) }
                    fuel?.let { InfoRow(label = fuelTranslation, value = it) }
                    materials?.let { InfoRow(label = materialsTranslation, value = it) }
                    cryopods?.let { InfoRow(label = cryopodsTranslation, value = it) }
                }
            }
        }
    }
}

@Composable
private fun ScoreHeader(
    utc: String,
    totalScore: String,
    isExpanded: Boolean?
) {
    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(weight = 1f),
            text = utc,
            style = typography.titleLarge,
        )
        Text(
            text = totalScore,
            style = typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
        )
        if (isExpanded != null) {
            val arrowRotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)
            Spacer(modifier = Modifier.width(width = 8.dp))
            Icon(
                modifier = Modifier.rotate(degrees = arrowRotation),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
            )
        }
    }
}

@Preview
@Composable
private fun ScorePreview() = AppTheme {
    Score(
        isExpanded = true,
        score = 100.0,
        yearsTraveled = 10.0,
        sensorRange = 10,
        integrity = 10,
        materials = 10,
    )
}
