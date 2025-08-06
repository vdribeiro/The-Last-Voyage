package com.hybris.tlv.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.usecase.exoplanet.model.PlanetType
import com.hybris.tlv.usecase.space.mapper.roundTo
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.planet01
import thelastvoyage.composeapp.generated.resources.planet02
import thelastvoyage.composeapp.generated.resources.planet03
import thelastvoyage.composeapp.generated.resources.planet04

@Composable
internal fun PlanetCard(
    name: String? = null,
    status: PlanetStatus? = null,
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
    type: PlanetType? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(size = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(all = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(size = 72.dp)
                    .clip(shape = RoundedCornerShape(size = 8.dp))
                    .align(alignment = Alignment.Top),
                painter = painterResource(resource = getImageResourceOfPlanet(type = type)),
                contentDescription = name,
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(width = 16.dp))
            Column(modifier = Modifier.weight(weight = 1f)) {
                name?.let {
                    Text(text = it, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(height = 4.dp))
                }
                status?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_status"),
                        value = getTranslation(key = it.displayName)
                    )
                }
                habitability?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_habitability"),
                        value = "${(it * 100.0).roundTo(decimalPlaces = 2)}%"
                    )
                }
                type?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_type"),
                        value = getTranslation(key = it.displayName)
                    )
                }
                orbitalPeriod?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_orbital_period"),
                        value = "${it.roundTo(decimalPlaces = 4)} ${getTranslation(key = "period_unit")}"
                    )
                }
                orbitAxis?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_orbit_axis"),
                        value = "${it.roundTo(decimalPlaces = 4)} au"
                    )
                }
                radius?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_radius"),
                        value = "${it.roundTo(decimalPlaces = 2)} R⊕"
                    )
                }
                mass?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_mass"),
                        value = "${it.roundTo(decimalPlaces = 2)} M⊕"
                    )
                }
                density?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_density"),
                        value = "${it.roundTo(decimalPlaces = 2)} g/cm^3"
                    )
                }
                eccentricity?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_eccentricity"),
                        value = "${it.roundTo(decimalPlaces = 2)} e"
                    )
                }
                insolationFlux?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_insolation_flux"),
                        value = "${it.roundTo(decimalPlaces = 2)} F"
                    )
                }
                equilibriumTemperature?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_temperature"),
                        value = "${it.roundTo(decimalPlaces = 1)} K"
                    )
                }
                occultationDepth?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_occultation_depth"),
                        value = "${it.roundTo(decimalPlaces = 2)} %"
                    )
                }
                inclination?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_inclination"),
                        value = "${it.roundTo(decimalPlaces = 2)}º"
                    )
                }
                obliquity?.let {
                    InfoRow(
                        label = getTranslation(key = "planet_obliquity"),
                        value = "${it.roundTo(decimalPlaces = 1)} ε"
                    )
                }
            }
        }
    }
}

private fun getImageResourceOfPlanet(type: PlanetType?): DrawableResource =
    listOf(
        Res.drawable.planet01,
        Res.drawable.planet02,
        Res.drawable.planet03,
        Res.drawable.planet04,
    ).random()
