package com.hybris.tlv.ui.screen.explore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.ui.component.Section
import com.hybris.tlv.ui.screen.explore.content.MenuContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ExploreScreen(store: Store<ExploreAction, ExploreState>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent ?: return

    BackHandler(enabled = true) { store.send(action = ExploreAction.Back) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (currentContent) {
                Content.MENU -> MenuContent(store = store)
                Content.MECHANICS -> Section(title = getTranslation(key = "explore_screen__mechanics"), sections = mechanics)
                Content.HABITABILITY -> Section(title = getTranslation(key = "explore_screen__habitability"), sections = habitability)
                Content.PLANET_TYPES -> Section(title = getTranslation(key = "explore_screen__planet_types"), sections = planetTypes)
            }
        }
    }
}

private val mechanics by lazy {
    listOf(
        Section(
            title = getTranslation(key = "explore_screen__mechanics_goal_title"),
            description = getTranslation(key = "explore_screen__mechanics_goal_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__mechanics_attributes_title"),
            description = getTranslation(key = "explore_screen__mechanics_attributes_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__mechanics_travel_title"),
            description = getTranslation(key = "explore_screen__mechanics_travel_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__mechanics_game_over_title"),
            description = getTranslation(key = "explore_screen__mechanics_game_over_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__mechanics_score_title"),
            description = getTranslation(key = "explore_screen__mechanics_score_description")
        )
    )
}

private val habitability by lazy {
    listOf(
        Section(
            title = getTranslation(key = "explore_screen__habitability_title"),
            description = getTranslation(key = "explore_screen__habitability_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_roche_title"),
            description = getTranslation(key = "explore_screen__habitability_roche_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_hz_title"),
            description = getTranslation(key = "explore_screen__habitability_hz_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_planet_density_radius_title"),
            description = getTranslation(key = "explore_screen__habitability_planet_density_radius_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_planet_mass_title"),
            description = getTranslation(key = "explore_screen__habitability_planet_mass_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_planet_eccentricity_title"),
            description = getTranslation(key = "explore_screen__habitability_planet_eccentricity_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_planet_temperature_title"),
            description = getTranslation(key = "explore_screen__habitability_planet_temperature_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_planet_obliquity_title"),
            description = getTranslation(key = "explore_screen__habitability_planet_obliquity_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_planet_protection_title"),
            description = getTranslation(key = "explore_screen__habitability_planet_protection_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_planet_tidal_locking_title"),
            description = getTranslation(key = "explore_screen__habitability_planet_tidal_locking_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_planet_esi_title"),
            description = getTranslation(key = "explore_screen__habitability_planet_esi_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_star_spectral_type_title"),
            description = getTranslation(key = "explore_screen__habitability_star_spectral_type_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_star_age_title"),
            description = getTranslation(key = "explore_screen__habitability_star_age_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_star_activity_title"),
            description = getTranslation(key = "explore_screen__habitability_star_activity_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_star_gravity_title"),
            description = getTranslation(key = "explore_screen__habitability_star_gravity_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_star_metallicity_title"),
            description = getTranslation(key = "explore_screen__habitability_star_metallicity_description")
        ),
        Section(
            title = getTranslation(key = "explore_screen__habitability_star_effective_temperature_title"),
            description = getTranslation(key = "explore_screen__habitability_star_effective_temperature_description")
        ),
    )
}

private val planetTypes by lazy {
    listOf(
        Section(
            title = getTranslation(key = "planet_type_exoplanet"),
            description = getTranslation(key = "planet_type_exoplanet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_terrestrial_planet"),
            description = getTranslation(key = "planet_type_terrestrial_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_sub_earth"),
            description = getTranslation(key = "planet_type_sub_earth_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_super_earth"),
            description = getTranslation(key = "planet_type_super_earth_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_mega_earth"),
            description = getTranslation(key = "planet_type_mega_earth_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_lava_planet"),
            description = getTranslation(key = "planet_type_lava_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_desert_planet"),
            description = getTranslation(key = "planet_type_desert_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_ice_planet"),
            description = getTranslation(key = "planet_type_ice_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_subsurface_ocean_planet"),
            description = getTranslation(key = "planet_type_subsurface_ocean_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_ocean_planet"),
            description = getTranslation(key = "planet_type_ocean_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_mini_neptune"),
            description = getTranslation(key = "planet_type_mini_neptune_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_super_neptune"),
            description = getTranslation(key = "planet_type_super_neptune_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_giant_planet"),
            description = getTranslation(key = "planet_type_giant_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_gas_giant"),
            description = getTranslation(key = "planet_type_gas_giant_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_ice_giant"),
            description = getTranslation(key = "planet_type_ice_giant_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_super_jupiter"),
            description = getTranslation(key = "planet_type_super_jupiter_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_iron_planet"),
            description = getTranslation(key = "planet_type_iron_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_puffy_planet"),
            description = getTranslation(key = "planet_type_puffy_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_super_puff_planet"),
            description = getTranslation(key = "planet_type_super_puff_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_hot_jupiter"),
            description = getTranslation(key = "planet_type_hot_jupiter_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_ultra_hot_jupiter"),
            description = getTranslation(key = "planet_type_ultra_hot_jupiter_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_hot_neptune"),
            description = getTranslation(key = "planet_type_hot_neptune_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_ultra_hot_neptune"),
            description = getTranslation(key = "planet_type_ultra_hot_neptune_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_ultra_short_period_planet"),
            description = getTranslation(key = "planet_type_ultra_short_period_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_eyeball_planet"),
            description = getTranslation(key = "planet_type_eyeball_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_hot_eyeball_planet"),
            description = getTranslation(key = "planet_type_hot_eyeball_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_cold_eyeball_planet"),
            description = getTranslation(key = "planet_type_cold_eyeball_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_ammonia_clouds_gas_giant"),
            description = getTranslation(key = "planet_type_ammonia_clouds_gas_giant_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_water_clouds_gas_giant"),
            description = getTranslation(key = "planet_type_water_clouds_gas_giant_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_cloudless_gas_giant"),
            description = getTranslation(key = "planet_type_cloudless_gas_giant_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_alkali_metal_clouds_gas_giant"),
            description = getTranslation(key = "planet_type_alkali_metal_clouds_gas_giant_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_silicate_clouds_gas_giant"),
            description = getTranslation(key = "planet_type_silicate_clouds_gas_giant_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_barren_planet"),
            description = getTranslation(key = "planet_type_barren_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_earth_like_planet"),
            description = getTranslation(key = "planet_type_earth_like_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_earth_analog_planet"),
            description = getTranslation(key = "planet_type_earth_analog_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_superhabitable_planet"),
            description = getTranslation(key = "planet_type_superhabitable_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_protoplanet"),
            description = getTranslation(key = "planet_type_protoplanet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_disrupted_planet"),
            description = getTranslation(key = "planet_type_disrupted_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_chthonian_planet"),
            description = getTranslation(key = "planet_type_chthonian_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_crater_planet"),
            description = getTranslation(key = "planet_type_crater_planet_description")
        ),
        Section(
            title = getTranslation(key = "planet_type_ellipsoid_planet"),
            description = getTranslation(key = "planet_type_ellipsoid_planet_description")
        )
    )
}
