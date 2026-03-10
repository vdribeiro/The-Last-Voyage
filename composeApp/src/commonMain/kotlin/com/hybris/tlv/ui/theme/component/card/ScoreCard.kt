package com.hybris.tlv.ui.theme.component.card

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.core.locale.getLocalDateTime
import com.hybris.tlv.domain.usecase.space.roundTo
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.divider.Divider
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun ScoreCard(
    modifier: Modifier = Modifier,
    isExpanded: Boolean? = null,
    score: Double? = null,
    utc: String? = null,
    settledPlanet: String? = null,
    habitability: Double? = null,
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
                    style = typography.bodyLarge,
                )
                Text(
                    text = score?.roundTo(decimalPlaces = 2)?.toString(),
                    style = typography.bodyLarge,
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
private fun ScoreCardPreview() = Preview {
    InjectTranslations(
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
        ScoreCard(
            isExpanded = true,
            score = 100.0,
            utc = getLocalDateTime(),
            sensorRange = 10,
            integrity = 10,
            materials = 10,
        )
        ScoreCard(
            isExpanded = false,
            score = 100.0,
            utc = getLocalDateTime(),
            sensorRange = 10,
            integrity = 10,
            materials = 10,
        )
        ScoreCard(
            utc = getLocalDateTime(),
            sensorRange = 10,
            integrity = 10,
            materials = 10,
        )
        ScoreCard(
            sensorRange = 10,
            integrity = 10,
            materials = 10,
        )
        ScoreCard(sensorRange = 10)
        ScoreCard()
    }
}
