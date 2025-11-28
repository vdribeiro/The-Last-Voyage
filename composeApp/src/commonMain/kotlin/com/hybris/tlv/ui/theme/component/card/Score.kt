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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.divider.Divider
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.space.roundTo
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

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
    val settledPlanetTranslation = getTranslation(key = "settled_planet")
    val habitabilityTranslation = getTranslation(key = "final_habitability")
    val engineTranslation = getTranslation(key = "engine")
    val assignedPointsTranslation = getTranslation(key = "points")
    val yearsTraveledTranslation = getTranslation(key = "ship_years_traveled")
    val sensorTranslation = getTranslation(key = "ship_sensor")
    val integrityTranslation = getTranslation(key = "ship_integrity")
    val fuelTranslation = getTranslation(key = "ship_fuel")
    val materialsTranslation = getTranslation(key = "ship_materials")
    val cryopodsTranslation = getTranslation(key = "ship_cryopods")

    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            if (score != null || utc != null) Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.weight(weight = 1f),
                    text = utc,
                    style = typography.titleLarge,
                )
                Text(
                    text = score?.roundTo(decimalPlaces = 2)?.toString(),
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
                val arrowRotation by animateFloatAsState(targetValue = if (isExpanded == true) 180f else 0f)
                Spacer(modifier = Modifier.width(width = 8.dp))
                Icon(
                    modifier = Modifier.rotate(degrees = arrowRotation),
                    imageVector = if (isExpanded != null) Icons.Default.KeyboardArrowDown else null,
                    contentDescription = "Expand",
                    emptySize = 12.dp
                )
            }
            AnimatedVisibility(
                visible = isExpanded != false,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    if (score != null || utc != null) {
                        Spacer(modifier = Modifier.height(height = 8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(height = 8.dp))
                    }
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

@Preview
@Composable
private fun ScorePreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "ship_sensor",
                value = "Sensor Range"
            ),
            Translation(
                key = "ship_integrity",
                value = "Integrity"
            ),
            Translation(
                key = "ship_materials",
                value = "Materials"
            ),
        )
    )
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        Score(
            isExpanded = true,
            score = 100.0,
            utc = getLocalDateTime(),
            sensorRange = 10,
            integrity = 10,
            materials = 10,
        )
        Score(
            isExpanded = false,
            score = 100.0,
            utc = getLocalDateTime(),
            sensorRange = 10,
            integrity = 10,
            materials = 10,
        )
        Score(
            utc = getLocalDateTime(),
            sensorRange = 10,
            integrity = 10,
            materials = 10,
        )
        Score(
            sensorRange = 10,
            integrity = 10,
            materials = 10,
        )
        Score(sensorRange = 10)
        Score()
    }
}
