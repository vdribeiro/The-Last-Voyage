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
import com.hybris.tlv.usecase.space.mapper.roundTo
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import thelastvoyage.composeapp.generated.resources.A
import thelastvoyage.composeapp.generated.resources.B
import thelastvoyage.composeapp.generated.resources.F
import thelastvoyage.composeapp.generated.resources.G
import thelastvoyage.composeapp.generated.resources.K
import thelastvoyage.composeapp.generated.resources.M
import thelastvoyage.composeapp.generated.resources.O
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.T
import thelastvoyage.composeapp.generated.resources.W

@Composable
internal fun StellarHostCard(
    name: String,
    systemName: String? = null,
    planetCount: Int? = null,
    spectralType: String? = null,
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
                painter = painterResource(resource = getImageResourceOfStellarHost(spectralType = spectralType)),
                contentDescription = name,
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(width = 16.dp))
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(text = name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(height = 4.dp))
                systemName?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_system_name"), value = it)
                }
                planetCount?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_planet_count"), value = it)
                }
                spectralType?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_spectral_type"), value = it)
                }
                effectiveTemperature?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_temperature"), value = "${it.roundTo(decimalPlaces = 1)} K")
                }
                radius?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_spectral_radius"), value = "${it.roundTo(decimalPlaces = 2)} R☉")
                }
                mass?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_spectral_mass"), value = "${it.roundTo(decimalPlaces = 2)} M☉")
                }
                metallicity?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_spectral_metallicity"), value = "${it.roundTo(decimalPlaces = 2)} dex")
                }
                luminosity?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_spectral_luminosity"), value = "${it.roundTo(decimalPlaces = 3)} L☉")
                }
                gravity?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_spectral_gravity"), value = "${it.roundTo(decimalPlaces = 2)} G☉")
                }
                age?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_spectral_age"), value = "${it.roundTo(decimalPlaces = 2)} Gyr")
                }
                density?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_spectral_density"), value = "${it.roundTo(decimalPlaces = 3)} g/cm^3")
                }
                rotationalVelocity?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_spectral_rotational_velocity"),
                        value = "${it.roundTo(decimalPlaces = 1)} km/s"
                    )
                }
                rotationalPeriod?.let {
                    InfoRow(
                        label = getTranslation(key = "stellar_host_spectral_rotational_period"),
                        value = "${it.roundTo(decimalPlaces = 2)} ${getTranslation(key = "period_unit")}"
                    )
                }
                ra?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_spectral_ra"), value = "${it.roundTo(decimalPlaces = 6)}º")
                }
                dec?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_spectral_dec"), value = "${it.roundTo(decimalPlaces = 6)}º")
                }
                distance?.let {
                    InfoRow(label = getTranslation(key = "stellar_host_distance"), value = "${it.roundTo(decimalPlaces = 2)} ly")
                }
            }
        }
    }
}

private fun getImageResourceOfStellarHost(spectralType: String?): DrawableResource {
    return when (spectralType?.firstOrNull()?.uppercase()) {
        "O" -> spectralTypesO.random()
        "B" -> spectralTypesB.random()
        "A" -> spectralTypesA.random()
        "F" -> spectralTypesF.random()
        "G" -> spectralTypesG.random()
        "K" -> spectralTypesK.random()
        "M" -> spectralTypesM.random()
        "W" -> spectralTypesW.random()
        "T" -> spectralTypesT.random()
        else -> spectralTypesG.random()
    }
}

private val spectralTypesO = listOf(
    Res.drawable.O,
)

private val spectralTypesB = listOf(
    Res.drawable.B,
)

private val spectralTypesA = listOf(
    Res.drawable.A,
)

private val spectralTypesF = listOf(
    Res.drawable.F,
)

private val spectralTypesG = listOf(
    Res.drawable.G,
)

private val spectralTypesK = listOf(
    Res.drawable.K,
)

private val spectralTypesM = listOf(
    Res.drawable.M,
)

private val spectralTypesW = listOf(
    Res.drawable.W,
)

private val spectralTypesT = listOf(
    Res.drawable.T,
)
