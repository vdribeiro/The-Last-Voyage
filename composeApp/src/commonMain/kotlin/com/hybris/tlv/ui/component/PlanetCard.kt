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
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.alkali_metal_clouds_gas_giant
import thelastvoyage.composeapp.generated.resources.ammonia_clouds_gas_giant
import thelastvoyage.composeapp.generated.resources.barren_planet
import thelastvoyage.composeapp.generated.resources.chthonian_planet
import thelastvoyage.composeapp.generated.resources.cold_eyeball_planet
import thelastvoyage.composeapp.generated.resources.crater_planet
import thelastvoyage.composeapp.generated.resources.desert_planet
import thelastvoyage.composeapp.generated.resources.disrupted_planet
import thelastvoyage.composeapp.generated.resources.earth_analog_planet
import thelastvoyage.composeapp.generated.resources.earth_like_planet
import thelastvoyage.composeapp.generated.resources.ellipsoid_planet
import thelastvoyage.composeapp.generated.resources.eyeball_planet
import thelastvoyage.composeapp.generated.resources.gas_giant
import thelastvoyage.composeapp.generated.resources.hot_eyebal_planet
import thelastvoyage.composeapp.generated.resources.hot_jupiter
import thelastvoyage.composeapp.generated.resources.hot_neptune
import thelastvoyage.composeapp.generated.resources.ice_giant
import thelastvoyage.composeapp.generated.resources.ice_planet
import thelastvoyage.composeapp.generated.resources.iron_planet
import thelastvoyage.composeapp.generated.resources.lava_planet
import thelastvoyage.composeapp.generated.resources.mega_earth
import thelastvoyage.composeapp.generated.resources.mini_neptune
import thelastvoyage.composeapp.generated.resources.ocean_planet
import thelastvoyage.composeapp.generated.resources.protoplanet
import thelastvoyage.composeapp.generated.resources.puffy_planet
import thelastvoyage.composeapp.generated.resources.silicate_clouds_gas_giant

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

private fun getImageResourceOfPlanet(type: PlanetType?): DrawableResource = when (type) {
    PlanetType.SUB_EARTH -> Res.drawable.barren_planet
    PlanetType.SUPER_EARTH -> Res.drawable.barren_planet
    PlanetType.MEGA_EARTH -> Res.drawable.mega_earth
    PlanetType.MINI_NEPTUNE -> Res.drawable.mini_neptune
    PlanetType.SUPER_NEPTUNE -> Res.drawable.barren_planet
    PlanetType.ICE_GIANT -> Res.drawable.ice_giant
    PlanetType.GAS_GIANT -> Res.drawable.gas_giant
    PlanetType.SUPER_JUPITER -> Res.drawable.barren_planet
    PlanetType.TERRESTRIAL_PLANET -> Res.drawable.barren_planet
    PlanetType.IRON_PLANET -> Res.drawable.iron_planet
    PlanetType.PUFFY_PLANET -> Res.drawable.puffy_planet
    PlanetType.SUPER_PUFF_PLANET -> Res.drawable.barren_planet
    PlanetType.OCEAN_PLANET -> Res.drawable.ocean_planet
    PlanetType.SUBSURFACE_OCEAN_PLANET -> Res.drawable.barren_planet
    PlanetType.LAVA_PLANET -> Res.drawable.lava_planet
    PlanetType.DESERT_PLANET -> Res.drawable.desert_planet
    PlanetType.ICE_PLANET -> Res.drawable.ice_planet
    PlanetType.HOT_JUPITER -> Res.drawable.hot_jupiter
    PlanetType.ULTRA_HOT_JUPITER -> Res.drawable.barren_planet
    PlanetType.HOT_NEPTUNE -> Res.drawable.hot_neptune
    PlanetType.ULTRA_HOT_NEPTUNE -> Res.drawable.barren_planet
    PlanetType.ULTRA_SHORT_PERIOD_PLANET -> Res.drawable.barren_planet
    PlanetType.EYEBALL_PLANET -> Res.drawable.eyeball_planet
    PlanetType.HOT_EYEBALL_PLANET -> Res.drawable.hot_eyebal_planet
    PlanetType.COLD_EYEBALL_PLANET -> Res.drawable.cold_eyeball_planet
    PlanetType.AMMONIA_CLOUDS_GAS_GIANT -> Res.drawable.ammonia_clouds_gas_giant
    PlanetType.WATER_CLOUDS_GAS_GIANT -> Res.drawable.barren_planet
    PlanetType.CLOUDLESS_GAS_GIANT -> Res.drawable.barren_planet
    PlanetType.ALKALI_METAL_CLOUDS_GAS_GIANT -> Res.drawable.alkali_metal_clouds_gas_giant
    PlanetType.SILICATE_CLOUDS_GAS_GIANT -> Res.drawable.silicate_clouds_gas_giant
    PlanetType.BARREN_PLANET -> Res.drawable.barren_planet
    PlanetType.EARTH_LIKE_PLANET -> Res.drawable.earth_like_planet
    PlanetType.EARTH_ANALOG_PLANET -> Res.drawable.earth_analog_planet
    PlanetType.SUPERHABITABLE_PLANET -> Res.drawable.barren_planet
    PlanetType.PROTOPLANET -> Res.drawable.protoplanet
    PlanetType.DISRUPTED_PLANET -> Res.drawable.disrupted_planet
    PlanetType.CHTHONIAN_PLANET -> Res.drawable.chthonian_planet
    PlanetType.CRATER_PLANET -> Res.drawable.crater_planet
    PlanetType.ELLIPSOID_PLANET -> Res.drawable.ellipsoid_planet
    null -> Res.drawable.barren_planet
}
