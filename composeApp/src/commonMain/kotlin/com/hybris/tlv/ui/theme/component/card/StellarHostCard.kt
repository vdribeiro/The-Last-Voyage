package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.data.resource.ImageResource
import com.hybris.tlv.domain.usecase.space.roundTo
import com.hybris.tlv.domain.usecase.space.spectralTypeToImage
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalShapes
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Image
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun StellarHostCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    systemName: String? = null,
    planetCount: Int? = null,
    spectralType: String? = null,
    spectralImage: ImageResource? = null,
    effectiveTemperature: Double? = null,
    radius: Double? = null,
    mass: Double? = null,
    metallicity: Double? = null,
    luminosity: Double? = null,
    gravity: Double? = null,
    age: Double? = null,
    density: Double? = null,
    rotationalVelocity: Double? = null,
    rotationalPeriod: Double? = null,
    distance: Double? = null,
    ra: Double? = null,
    dec: Double? = null,
    spectralTypeScore: Double? = null,
    massScore: Double? = null,
    ageScore: Double? = null,
    activityScore: Double? = null,
    rotationalPeriodScore: Double? = null,
    gravityScore: Double? = null,
    metallicityScore: Double? = null,
    effectiveTemperatureScore: Double? = null
) {
    val typography = LocalTypography.current
    val shapes = LocalShapes.current
    val stellarHostSystemNameTranslation = getTranslation(key = "stellar_host_system_name")
    val stellarHostPlanetCountTranslation = getTranslation(key = "stellar_host_planet_count")
    val stellarHostTypeTranslation = getTranslation(key = "stellar_host_type")
    val stellarHostTemperatureTranslation = getTranslation(key = "stellar_host_temperature")
    val stellarHostRadiusTranslation = getTranslation(key = "stellar_host_radius")
    val stellarHostMassTranslation = getTranslation(key = "stellar_host_mass")
    val stellarHostMetallicityTranslation = getTranslation(key = "stellar_host_metallicity")
    val stellarHostLuminosityTranslation = getTranslation(key = "stellar_host_luminosity")
    val stellarHostGravityTranslation = getTranslation(key = "stellar_host_gravity")
    val stellarHostAgeTranslation = getTranslation(key = "stellar_host_age")
    val stellarHostDensityTranslation = getTranslation(key = "stellar_host_density")
    val stellarHostRotationalVelocityTranslation = getTranslation(key = "stellar_host_rotational_velocity")
    val stellarHostRotationalPeriodTranslation = getTranslation(key = "stellar_host_rotational_period")
    val periodUnitTranslation = getTranslation(key = "period_unit")
    val stellarHostRaTranslation = getTranslation(key = "stellar_host_ra")
    val stellarHostDecTranslation = getTranslation(key = "stellar_host_dec")
    val stellarHostDistanceTranslation = getTranslation(key = "stellar_host_distance")
    val stellarHostSpectralTypeScoreTranslation = getTranslation(key = "stellar_host_spectral_type_score")
    val stellarHostMassScoreTranslation = getTranslation(key = "stellar_host_mass_score")
    val stellarHostAgeScoreTranslation = getTranslation(key = "stellar_host_age_score")
    val stellarHostActivityScoreTranslation = getTranslation(key = "stellar_host_activity_score")
    val stellarHostRotationalPeriodScoreTranslation = getTranslation(key = "stellar_host_rotational_period_score")
    val stellarHostGravityScoreTranslation = getTranslation(key = "stellar_host_gravity_score")
    val stellarHostMetallicityScoreTranslation = getTranslation(key = "stellar_host_metallicity_score")
    val stellarHostEffectiveTemperatureScoreTranslation = getTranslation(key = "stellar_host_effective_temperature_score")

    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            spectralImage?.let {
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
            Column(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                name?.let { Text(text = it, style = typography.titleLarge, fontWeight = FontWeight.Bold) }
                systemName?.let { InfoRow(label = stellarHostSystemNameTranslation, value = it) }
                planetCount?.let { InfoRow(label = stellarHostPlanetCountTranslation, value = it) }
                spectralType?.let { InfoRow(label = stellarHostTypeTranslation, value = it) }
                effectiveTemperature?.let { InfoRow(label = stellarHostTemperatureTranslation, value = "${it.roundTo(decimalPlaces = 1)} K") }
                radius?.let { InfoRow(label = stellarHostRadiusTranslation, value = "${it.roundTo(decimalPlaces = 2)} R☉") }
                mass?.let { InfoRow(label = stellarHostMassTranslation, value = "${it.roundTo(decimalPlaces = 2)} M☉") }
                metallicity?.let { InfoRow(label = stellarHostMetallicityTranslation, value = "${it.roundTo(decimalPlaces = 2)} dex") }
                luminosity?.let { InfoRow(label = stellarHostLuminosityTranslation, value = "${it.roundTo(decimalPlaces = 3)} L☉") }
                gravity?.let { InfoRow(label = stellarHostGravityTranslation, value = "${it.roundTo(decimalPlaces = 2)} G☉") }
                age?.let { InfoRow(label = stellarHostAgeTranslation, value = "${it.roundTo(decimalPlaces = 2)} Gyr") }
                density?.let { InfoRow(label = stellarHostDensityTranslation, value = "${it.roundTo(decimalPlaces = 3)} g/cm^3") }
                rotationalVelocity?.let { InfoRow(label = stellarHostRotationalVelocityTranslation, value = "${it.roundTo(decimalPlaces = 1)} km/s") }
                rotationalPeriod?.let { InfoRow(label = stellarHostRotationalPeriodTranslation, value = "${it.roundTo(decimalPlaces = 2)} $periodUnitTranslation") }
                ra?.let { InfoRow(label = stellarHostRaTranslation, value = "${it.roundTo(decimalPlaces = 6)}°") }
                dec?.let { InfoRow(label = stellarHostDecTranslation, value = "${it.roundTo(decimalPlaces = 6)}°") }
                distance?.let { InfoRow(label = stellarHostDistanceTranslation, value = "${it.roundTo(decimalPlaces = 2)} ly") }
                spectralTypeScore?.let { InfoRow(label = stellarHostSpectralTypeScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                massScore?.let { InfoRow(label = stellarHostMassScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                ageScore?.let { InfoRow(label = stellarHostAgeScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                activityScore?.let { InfoRow(label = stellarHostActivityScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                rotationalPeriodScore?.let { InfoRow(label = stellarHostRotationalPeriodScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                gravityScore?.let { InfoRow(label = stellarHostGravityScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                metallicityScore?.let { InfoRow(label = stellarHostMetallicityScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
                effectiveTemperatureScore?.let { InfoRow(label = stellarHostEffectiveTemperatureScoreTranslation, value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%") }
            }
        }
    }
}

@Preview
@Composable
private fun StellarHostCardPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "stellar_host_system_name",
                value = "System Name"
            ),
            PreviewTranslation(
                key = "stellar_host_planet_count",
                value = "Planet Count"
            ),
            PreviewTranslation(
                key = "stellar_host_type",
                value = "Host"
            ),
            PreviewTranslation(
                key = "stellar_host_temperature",
                value = "Temperature"
            )
        )
    )
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        StellarHostCard(
            name = "Sun",
            systemName = "Sol",
            planetCount = 8,
            spectralType = "G",
            spectralImage = "G".spectralTypeToImage(),
            effectiveTemperature = 255.0
        )
        StellarHostCard(
            systemName = "Sol",
            spectralImage = "G".spectralTypeToImage(),
        )
        StellarHostCard(
            name = "Sun",
            systemName = "Sol",
            planetCount = 8,
            spectralType = "G",
            effectiveTemperature = 255.0
        )
        StellarHostCard(
            planetCount = 8,
            spectralType = "G",
            spectralImage = "G".spectralTypeToImage(),
            effectiveTemperature = 255.0
        )
    }
}
