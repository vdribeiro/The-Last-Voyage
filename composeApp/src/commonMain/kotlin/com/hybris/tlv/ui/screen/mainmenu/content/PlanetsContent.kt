package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.PlanetCard
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.space.mapper.toDrawable
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun PlanetsContent(store: Store<MainMenuAction, MainMenuState>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = planets, key = { it.name }) { planet ->
            PlanetCard(
                name = planet.name,
                description = planet.description,
                typeDrawable = planet.type.toDrawable()
            )
        }
    }
}

private data class Planet(
    val name: String,
    val description: String,
    val type: PlanetType,
)

private val planets by lazy {
    listOf(
        Planet(
            name = getTranslation(key = "planet_type_terrestrial_planet"),
            description = getTranslation(key = "planet_type_terrestrial_planet_description"),
            type = PlanetType.TERRESTRIAL_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_sub_earth"),
            description = getTranslation(key = "planet_type_sub_earth_description"),
            type = PlanetType.SUB_EARTH
        ),
        Planet(
            name = getTranslation(key = "planet_type_super_earth"),
            description = getTranslation(key = "planet_type_super_earth_description"),
            type = PlanetType.SUPER_EARTH
        ),
        Planet(
            name = getTranslation(key = "planet_type_mega_earth"),
            description = getTranslation(key = "planet_type_mega_earth_description"),
            type = PlanetType.MEGA_EARTH
        ),
        Planet(
            name = getTranslation(key = "planet_type_earth_like_planet"),
            description = getTranslation(key = "planet_type_earth_like_planet_description"),
            type = PlanetType.EARTH_LIKE_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_earth_analog_planet"),
            description = getTranslation(key = "planet_type_earth_analog_planet_description"),
            type = PlanetType.EARTH_ANALOG_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_superhabitable_planet"),
            description = getTranslation(key = "planet_type_superhabitable_planet_description"),
            type = PlanetType.SUPERHABITABLE_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_lava_planet"),
            description = getTranslation(key = "planet_type_lava_planet_description"),
            type = PlanetType.LAVA_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_desert_planet"),
            description = getTranslation(key = "planet_type_desert_planet_description"),
            type = PlanetType.DESERT_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_ice_planet"),
            description = getTranslation(key = "planet_type_ice_planet_description"),
            type = PlanetType.ICE_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_subsurface_ocean_planet"),
            description = getTranslation(key = "planet_type_subsurface_ocean_planet_description"),
            type = PlanetType.SUBSURFACE_OCEAN_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_ocean_planet"),
            description = getTranslation(key = "planet_type_ocean_planet_description"),
            type = PlanetType.OCEAN_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_iron_planet"),
            description = getTranslation(key = "planet_type_iron_planet_description"),
            type = PlanetType.IRON_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_eyeball_planet"),
            description = getTranslation(key = "planet_type_eyeball_planet_description"),
            type = PlanetType.EYEBALL_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_hot_eyeball_planet"),
            description = getTranslation(key = "planet_type_hot_eyeball_planet_description"),
            type = PlanetType.HOT_EYEBALL_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_cold_eyeball_planet"),
            description = getTranslation(key = "planet_type_cold_eyeball_planet_description"),
            type = PlanetType.COLD_EYEBALL_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_barren_planet"),
            description = getTranslation(key = "planet_type_barren_planet_description"),
            type = PlanetType.BARREN_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_crater_planet"),
            description = getTranslation(key = "planet_type_crater_planet_description"),
            type = PlanetType.CRATER_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_chthonian_planet"),
            description = getTranslation(key = "planet_type_chthonian_planet_description"),
            type = PlanetType.CHTHONIAN_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_mini_neptune"),
            description = getTranslation(key = "planet_type_mini_neptune_description"),
            type = PlanetType.MINI_NEPTUNE
        ),
        Planet(
            name = getTranslation(key = "planet_type_hot_neptune"),
            description = getTranslation(key = "planet_type_hot_neptune_description"),
            type = PlanetType.HOT_NEPTUNE
        ),
        Planet(
            name = getTranslation(key = "planet_type_ultra_hot_neptune"),
            description = getTranslation(key = "planet_type_ultra_hot_neptune_description"),
            type = PlanetType.ULTRA_HOT_NEPTUNE
        ),
        Planet(
            name = getTranslation(key = "planet_type_super_neptune"),
            description = getTranslation(key = "planet_type_super_neptune_description"),
            type = PlanetType.SUPER_NEPTUNE
        ),
        Planet(
            name = getTranslation(key = "planet_type_ice_giant"),
            description = getTranslation(key = "planet_type_ice_giant_description"),
            type = PlanetType.ICE_GIANT
        ),
        Planet(
            name = getTranslation(key = "planet_type_gas_giant"),
            description = getTranslation(key = "planet_type_gas_giant_description"),
            type = PlanetType.GAS_GIANT
        ),
        Planet(
            name = getTranslation(key = "planet_type_super_jupiter"),
            description = getTranslation(key = "planet_type_super_jupiter_description"),
            type = PlanetType.SUPER_JUPITER
        ),
        Planet(
            name = getTranslation(key = "planet_type_hot_jupiter"),
            description = getTranslation(key = "planet_type_hot_jupiter_description"),
            type = PlanetType.HOT_JUPITER
        ),
        Planet(
            name = getTranslation(key = "planet_type_ultra_hot_jupiter"),
            description = getTranslation(key = "planet_type_ultra_hot_jupiter_description"),
            type = PlanetType.ULTRA_HOT_JUPITER
        ),
        Planet(
            name = getTranslation(key = "planet_type_ammonia_clouds_gas_giant"),
            description = getTranslation(key = "planet_type_ammonia_clouds_gas_giant_description"),
            type = PlanetType.AMMONIA_CLOUDS_GAS_GIANT
        ),
        Planet(
            name = getTranslation(key = "planet_type_water_clouds_gas_giant"),
            description = getTranslation(key = "planet_type_water_clouds_gas_giant_description"),
            type = PlanetType.WATER_CLOUDS_GAS_GIANT
        ),
        Planet(
            name = getTranslation(key = "planet_type_cloudless_gas_giant"),
            description = getTranslation(key = "planet_type_cloudless_gas_giant_description"),
            type = PlanetType.CLOUDLESS_GAS_GIANT
        ),
        Planet(
            name = getTranslation(key = "planet_type_alkali_metal_clouds_gas_giant"),
            description = getTranslation(key = "planet_type_alkali_metal_clouds_gas_giant_description"),
            type = PlanetType.ALKALI_METAL_CLOUDS_GAS_GIANT
        ),
        Planet(
            name = getTranslation(key = "planet_type_silicate_clouds_gas_giant"),
            description = getTranslation(key = "planet_type_silicate_clouds_gas_giant_description"),
            type = PlanetType.SILICATE_CLOUDS_GAS_GIANT
        ),
        Planet(
            name = getTranslation(key = "planet_type_puffy_planet"),
            description = getTranslation(key = "planet_type_puffy_planet_description"),
            type = PlanetType.PUFFY_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_super_puff_planet"),
            description = getTranslation(key = "planet_type_super_puff_planet_description"),
            type = PlanetType.SUPER_PUFF_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_protoplanet"),
            description = getTranslation(key = "planet_type_protoplanet_description"),
            type = PlanetType.PROTOPLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_ultra_short_period_planet"),
            description = getTranslation(key = "planet_type_ultra_short_period_planet_description"),
            type = PlanetType.ULTRA_SHORT_PERIOD_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_disrupted_planet"),
            description = getTranslation(key = "planet_type_disrupted_planet_description"),
            type = PlanetType.DISRUPTED_PLANET
        ),
        Planet(
            name = getTranslation(key = "planet_type_ellipsoid_planet"),
            description = getTranslation(key = "planet_type_ellipsoid_planet_description"),
            type = PlanetType.ELLIPSOID_PLANET
        ),
    )
}
