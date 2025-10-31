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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalShapes
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Image
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.space.formula.toImage
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun PlanetCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
    status: String? = null,
    orbitalPeriod: Double? = null,
    orbitAxis: Double? = null,
    radius: Double? = null,
    mass: Double? = null,
    density: Double? = null,
    eccentricity: Double? = null,
    insolationFlux: Double? = null,
    equilibriumTemperature: Double? = null,
    occultationDepth: Double? = null,
    inclination: Double? = null,
    obliquity: Double? = null,
    habitability: Double? = null,
    confidence: Double? = null,
    type: String? = null,
    image: ImageResource? = null,
    rocheScore: Double? = null,
    habitableZoneKopparapuScore: Double? = null,
    habitableZoneKastingScore: Double? = null,
    radiusScore: Double? = null,
    massScore: Double? = null,
    telluricityScore: Double? = null,
    eccentricityScore: Double? = null,
    temperatureScore: Double? = null,
    obliquityScore: Double? = null,
    esiScore: Double? = null,
    protectionScore: Double? = null,
    tidalLockingScore: Double? = null,
) {
    val typography = LocalTypography.current
    val shapes = LocalShapes.current
    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val planetStatusTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_status") }
    val planetHabitabilityTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_habitability") }
    val planetConfidenceTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_confidence") }
    val planetTypeTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_type") }
    val planetOrbitalPeriodTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_orbital_period") }
    val periodUnitTranslation = remember(key1 = translationVersion) { getTranslation(key = "period_unit") }
    val planetOrbitAxisTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_orbit_axis") }
    val planetRadiusTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_radius") }
    val planetMassTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_mass") }
    val planetDensityTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_density") }
    val planetEccentricityTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_eccentricity") }
    val planetInsolationFluxTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_insolation_flux") }
    val planetTemperatureTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_temperature") }
    val planetOccultationDepthTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_occultation_depth") }
    val planetInclinationTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_inclination") }
    val planetObliquityTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_obliquity") }
    val planetRocheScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_roche_score") }
    val planetHabitableZoneKopparapuScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_habitable_zone_kopparapu_score") }
    val planetHabitableZoneKastingScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_habitable_zone_kasting_score") }
    val planetRadiusScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_radius_score") }
    val planetMassScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_mass_score") }
    val planetTelluricityScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_telluricity_score") }
    val planetEccentricityScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_eccentricity_score") }
    val planetTemperatureScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_temperature_score") }
    val planetObliquityScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_obliquity_score") }
    val planetEsiScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_esi_score") }
    val planetProtectionScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_protection_score") }
    val planetTidalLockingScoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "planet_tidal_locking_score") }

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
                    image = it,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.width(width = 16.dp))
            }
            Column(modifier = Modifier.weight(weight = 1f)) {
                name?.let {
                    Text(text = it, style = typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(height = 4.dp))
                }
                description?.let { Text(text = getTranslation(key = it), style = typography.bodyLarge) }
                status?.let { InfoRow(label = planetStatusTranslation, value = getTranslation(key = it)) }
                habitability?.let { InfoRow(label = planetHabitabilityTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                confidence?.let { InfoRow(label = planetConfidenceTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                type?.let { InfoRow(label = planetTypeTranslation, value = getTranslation(key = it)) }
                orbitalPeriod?.let { InfoRow(label = planetOrbitalPeriodTranslation, value = "${it.roundTo(decimalPlaces = 4)} $periodUnitTranslation") }
                orbitAxis?.let { InfoRow(label = planetOrbitAxisTranslation, value = "${it.roundTo(decimalPlaces = 4)} au") }
                radius?.let { InfoRow(label = planetRadiusTranslation, value = "${it.roundTo(decimalPlaces = 2)} R⊕") }
                mass?.let { InfoRow(label = planetMassTranslation, value = "${it.roundTo(decimalPlaces = 2)} M⊕") }
                density?.let { InfoRow(label = planetDensityTranslation, value = "${it.roundTo(decimalPlaces = 2)} g/cm³") }
                eccentricity?.let { InfoRow(label = planetEccentricityTranslation, value = "${it.roundTo(decimalPlaces = 2)} e") }
                insolationFlux?.let { InfoRow(label = planetInsolationFluxTranslation, value = "${it.roundTo(decimalPlaces = 2)} F⊕") }
                equilibriumTemperature?.let { InfoRow(label = planetTemperatureTranslation, value = "${it.roundTo(decimalPlaces = 1)} K") }
                occultationDepth?.let { InfoRow(label = planetOccultationDepthTranslation, value = "${it.roundTo(decimalPlaces = 2)} %") }
                inclination?.let { InfoRow(label = planetInclinationTranslation, value = "${it.roundTo(decimalPlaces = 2)}°") }
                obliquity?.let { InfoRow(label = planetObliquityTranslation, value = "${it.roundTo(decimalPlaces = 1)} ε") }
                rocheScore?.let { InfoRow(label = planetRocheScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                habitableZoneKopparapuScore?.let { InfoRow(label = planetHabitableZoneKopparapuScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                habitableZoneKastingScore?.let { InfoRow(label = planetHabitableZoneKastingScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                radiusScore?.let { InfoRow(label = planetRadiusScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                massScore?.let { InfoRow(label = planetMassScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                telluricityScore?.let { InfoRow(label = planetTelluricityScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                eccentricityScore?.let { InfoRow(label = planetEccentricityScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                temperatureScore?.let { InfoRow(label = planetTemperatureScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                obliquityScore?.let { InfoRow(label = planetObliquityScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                esiScore?.let { InfoRow(label = planetEsiScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                protectionScore?.let { InfoRow(label = planetProtectionScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                tidalLockingScore?.let { InfoRow(label = planetTidalLockingScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
            }
        }
    }
}

@Preview
@Composable
private fun PlanetCardPreview() = AppTheme {
    PlanetCard(
        name = "Earth",
        description = "Beautiful",
        image = PlanetType.EARTH_ANALOG_PLANET.toImage()
    )
}
