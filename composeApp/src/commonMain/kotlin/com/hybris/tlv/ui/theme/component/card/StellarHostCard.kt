package com.hybris.tlv.ui.theme.component.card

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.space.formula.spectralTypeToDrawable
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun StellarHostCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
    systemName: String? = null,
    planetCount: Int? = null,
    spectralType: String? = null,
    spectralTypeDrawable: DrawableResource? = null,
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

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(all = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            spectralTypeDrawable?.let {
                Image(
                    modifier = Modifier
                        .size(size = 72.dp)
                        .clip(shape = shapes.small)
                        .align(alignment = Alignment.Top),
                    painter = painterResource(resource = it),
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
                description?.let {
                    Text(text = getTranslation(key = it), style = typography.bodyMedium)
                }
                systemName?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_system_name"),
                        value = it
                    )
                }
                planetCount?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_planet_count"),
                        value = it
                    )
                }
                spectralType?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_type"),
                        value = it
                    )
                }
                effectiveTemperature?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_temperature"),
                        value = "${it.roundTo(decimalPlaces = 1)} K"
                    )
                }
                radius?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_radius"),
                        value = "${it.roundTo(decimalPlaces = 2)} R☉"
                    )
                }
                mass?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_mass"),
                        value = "${it.roundTo(decimalPlaces = 2)} M☉"
                    )
                }
                metallicity?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_metallicity"),
                        value = "${it.roundTo(decimalPlaces = 2)} dex"
                    )
                }
                luminosity?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_luminosity"),
                        value = "${it.roundTo(decimalPlaces = 3)} L☉"
                    )
                }
                gravity?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_gravity"),
                        value = "${it.roundTo(decimalPlaces = 2)} G☉"
                    )
                }
                age?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_age"),
                        value = "${it.roundTo(decimalPlaces = 2)} Gyr"
                    )
                }
                density?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_density"),
                        value = "${it.roundTo(decimalPlaces = 3)} g/cm^3"
                    )
                }
                rotationalVelocity?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_rotational_velocity"),
                        value = "${it.roundTo(decimalPlaces = 1)} km/s"
                    )
                }
                rotationalPeriod?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_rotational_period"),
                        value = "${it.roundTo(decimalPlaces = 2)} ${getTranslation(key = "period_unit")}"
                    )
                }
                ra?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_ra"),
                        value = "${it.roundTo(decimalPlaces = 6)}º"
                    )
                }
                dec?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_dec"),
                        value = "${it.roundTo(decimalPlaces = 6)}º"
                    )
                }
                distance?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_distance"),
                        value = "${it.roundTo(decimalPlaces = 2)} ly"
                    )
                }
                spectralTypeScore?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_spectral_type_score"),
                        value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%"
                    )
                }
                massScore?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_mass_score"),
                        value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%"
                    )
                }
                ageScore?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_age_score"),
                        value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%"
                    )
                }
                activityScore?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_activity_score"),
                        value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%"
                    )
                }
                rotationalPeriodScore?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_rotational_period_score"),
                        value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%"
                    )
                }
                gravityScore?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_gravity_score"),
                        value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%"
                    )
                }
                metallicityScore?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_metallicity_score"),
                        value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%"
                    )
                }
                effectiveTemperatureScore?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_effective_temperature_score"),
                        value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun StellarHostCardPreview() = AppTheme {
    StellarHostCard(
        name = "Sun",
        description = "Bright",
        spectralTypeDrawable = "G".spectralTypeToDrawable()
    )
}
