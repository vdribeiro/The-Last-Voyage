package com.hybris.tlv.ui.screen.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.PlanetCard
import com.hybris.tlv.ui.component.Section
import com.hybris.tlv.ui.component.StellarHostCard
import com.hybris.tlv.ui.screen.learn.content.MenuContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.space.mapper.spectralTypeToDrawable
import com.hybris.tlv.usecase.space.mapper.toDrawable
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.translation.getTranslation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun LearnScreen(store: Store<LearnAction, LearnState>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent ?: return

    BackHandler(enabled = true) { store.send(action = LearnAction.Back) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (currentContent) {
                Content.MENU -> MenuContent(store = store)
                Content.HOST_TYPES -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {
                    items(items = stellarHosts, key = { it.name }) { stellarHost ->
                        StellarHostCard(
                            name = stellarHost.name,
                            description = stellarHost.description,
                            spectralTypeDrawable = stellarHost.spectralType.spectralTypeToDrawable(),
                        )
                    }
                }

                Content.PLANET_TYPES -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {
                    items(items = planets, key = { it.name }) { planet ->
                        PlanetCard(
                            name = planet.name,
                            description = planet.description,
                            typeDrawable = planet.type.toDrawable()
                        )
                    }
                }

                Content.PROPERTIES -> Section(title = getTranslation(key = "learn_screen__properties"), sections = properties)
                Content.MECHANICS -> Section(title = getTranslation(key = "learn_screen__mechanics"), sections = mechanics)
                Content.HABITABILITY -> Section(title = getTranslation(key = "learn_screen__habitability"), sections = habitability)
            }
        }
    }
}

private data class Host(
    val name: String,
    val description: String,
    val spectralType: String,
)

private data class Planet(
    val name: String,
    val description: String,
    val type: PlanetType,
)

private val stellarHosts by lazy {
    listOf(
        Host(
            name = getTranslation(key = "stellar_host_type_o"),
            description = getTranslation(key = "stellar_host_type_o_description"),
            spectralType = "O"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_b"),
            description = getTranslation(key = "stellar_host_type_b_description"),
            spectralType = "B"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_a"),
            description = getTranslation(key = "stellar_host_type_a_description"),
            spectralType = "A"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_f"),
            description = getTranslation(key = "stellar_host_type_f_description"),
            spectralType = "F"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_g"),
            description = getTranslation(key = "stellar_host_type_g_description"),
            spectralType = "G"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_k"),
            description = getTranslation(key = "stellar_host_type_k_description"),
            spectralType = "K"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_m"),
            description = getTranslation(key = "stellar_host_type_m_description"),
            spectralType = "M"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_w"),
            description = getTranslation(key = "stellar_host_type_w_description"),
            spectralType = "W"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_q"),
            description = getTranslation(key = "stellar_host_type_q_description"),
            spectralType = "Q"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_p"),
            description = getTranslation(key = "stellar_host_type_p_description"),
            spectralType = "P"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_l"),
            description = getTranslation(key = "stellar_host_type_l_description"),
            spectralType = "L"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_t"),
            description = getTranslation(key = "stellar_host_type_t_description"),
            spectralType = "T"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_y"),
            description = getTranslation(key = "stellar_host_type_y_description"),
            spectralType = "Y"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_c"),
            description = getTranslation(key = "stellar_host_type_c_description"),
            spectralType = "C"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_s"),
            description = getTranslation(key = "stellar_host_type_s_description"),
            spectralType = "S"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_d"),
            description = getTranslation(key = "stellar_host_type_d_description"),
            spectralType = "D"
        ),
        Host(
            name = getTranslation(key = "stellar_host_type_unknown"),
            description = getTranslation(key = "stellar_host_type_unknown_description"),
            spectralType = "?"
        ),
    )
}

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

private val properties by lazy {
    listOf(
        Section(
            title = getTranslation(key = "learn_screen__properties_stellar_host_title"),
            description = getTranslation(key = "learn_screen__properties_stellar_host_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__properties_planet_title"),
            description = getTranslation(key = "learn_screen__properties_planet_description")
        ),
    )
}

private val mechanics by lazy {
    listOf(
        Section(
            title = getTranslation(key = "learn_screen__mechanics_goal_title"),
            description = getTranslation(key = "learn_screen__mechanics_goal_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__mechanics_attributes_title"),
            description = getTranslation(key = "learn_screen__mechanics_attributes_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__mechanics_travel_title"),
            description = getTranslation(key = "learn_screen__mechanics_travel_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__mechanics_game_over_title"),
            description = getTranslation(key = "learn_screen__mechanics_game_over_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__mechanics_score_title"),
            description = getTranslation(key = "learn_screen__mechanics_score_description")
        )
    )
}

private val habitability by lazy {
    listOf(
        Section(
            title = getTranslation(key = "learn_screen__habitability_title"),
            description = getTranslation(key = "learn_screen__habitability_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_roche_title"),
            description = getTranslation(key = "learn_screen__habitability_roche_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_hz_title"),
            description = getTranslation(key = "learn_screen__habitability_hz_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_planet_density_radius_title"),
            description = getTranslation(key = "learn_screen__habitability_planet_density_radius_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_planet_mass_title"),
            description = getTranslation(key = "learn_screen__habitability_planet_mass_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_planet_eccentricity_title"),
            description = getTranslation(key = "learn_screen__habitability_planet_eccentricity_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_planet_temperature_title"),
            description = getTranslation(key = "learn_screen__habitability_planet_temperature_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_planet_obliquity_title"),
            description = getTranslation(key = "learn_screen__habitability_planet_obliquity_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_planet_protection_title"),
            description = getTranslation(key = "learn_screen__habitability_planet_protection_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_planet_tidal_locking_title"),
            description = getTranslation(key = "learn_screen__habitability_planet_tidal_locking_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_planet_esi_title"),
            description = getTranslation(key = "learn_screen__habitability_planet_esi_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_star_spectral_type_title"),
            description = getTranslation(key = "learn_screen__habitability_star_spectral_type_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_star_age_title"),
            description = getTranslation(key = "learn_screen__habitability_star_age_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_star_activity_title"),
            description = getTranslation(key = "learn_screen__habitability_star_activity_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_star_gravity_title"),
            description = getTranslation(key = "learn_screen__habitability_star_gravity_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_star_metallicity_title"),
            description = getTranslation(key = "learn_screen__habitability_star_metallicity_description")
        ),
        Section(
            title = getTranslation(key = "learn_screen__habitability_star_effective_temperature_title"),
            description = getTranslation(key = "learn_screen__habitability_star_effective_temperature_description")
        ),
    )
}
