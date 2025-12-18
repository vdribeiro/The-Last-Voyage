package com.hybris.tlv.ui.screen.help

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.command.Command
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.platform.Platform
import com.hybris.tlv.platform.open
import com.hybris.tlv.platform.platform
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.bottombar.HelpBar
import com.hybris.tlv.ui.theme.component.bottombar.Snackbar
import com.hybris.tlv.ui.theme.component.container.LearnMenu
import com.hybris.tlv.ui.theme.component.container.PropertyList
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.component.topbar.ControlPanel
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.spectralTypeToImage
import com.hybris.tlv.usecase.space.toImage
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun HelpScreen(store: Store<HelpState, HelpAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val currentContent = storeState.currentContent

    Screen(
        loading = storeState.loading,
        onBackClick = { store.back() },
        onMusicClick = { store.command(command = Command.ToggleAudio) },
        onFeedbackClick = { store.navigate(screen = Screen.Feedback()) },
        bottomBar = {
            if (currentContent == Content.LEARN_MENU) HelpBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                onVersionClick = { store.send(action = HelpAction.VersionClick(reset = false)) },
                onResetClick = { store.send(action = HelpAction.Reset) }
            )
        },
        snackbarHost = {
            if (storeState.showSnackbar) {
                val message = getTranslation(
                    key = when (platform) {
                        Platform.Android, Platform.Ios -> "konami_mobile"
                        else -> "konami_desktop"
                    }
                )
                Snackbar(
                    message = message,
                    durationMillis = 5000L,
                    onDismiss = { store.send(action = HelpAction.VersionClick(reset = true)) }
                )
            }
        }
    ) {
        when (currentContent) {
            Content.LEARN_MENU -> LearnMenu(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                onNavigationClick = { store.send(action = HelpAction.Navigation) },
                onPanelClick = { store.send(action = HelpAction.ControlPanel) },
                onHostDefinitionClick = { store.send(action = HelpAction.HostDefinition) },
                onHostTypesClick = { store.send(action = HelpAction.HostType) },
                onPlanetDefinitionClick = { store.send(action = HelpAction.PlanetDefinition) },
                onPlanetTypesClick = { store.send(action = HelpAction.PlanetType) },
                onHabitabilityClick = { store.send(action = HelpAction.Habitability) },
                onScoreClick = { store.send(action = HelpAction.Score) },
                onMechanicsClick = { store.send(action = HelpAction.Mechanics) }
            )

            Content.NAVIGATION -> PropertyList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = getTranslation(key = "main_menu_screen__navigation"),
                properties = navigation,
                id = { it.id },
                description = { it.description },
                icon = { it.icon }
            )

            Content.CONTROL_PANEL -> PropertyList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = getTranslation(key = "main_menu_screen__control_panel"),
                properties = panel,
                id = { it.id },
                description = { it.description },
                icon = { it.icon },
                header = {
                    val name = getTranslation(key = "planet_name")
                    val habitability = getTranslation(key = "planet_habitability")
                    var ascending by remember { mutableStateOf(value = true) }
                    ControlPanel(
                        viewName = getTranslation(key = "stellar_explorer_screen__planet_list"),
                        viewIcon = Icons.Default.Public,
                        count = 1234,
                        properties = listOf(name, habitability),
                        selectedProperty = name,
                        ascending = ascending,
                        onSortDirectionChange = { ascending = !ascending },
                        visibleProperties = listOf(element = name),
                        selectedProperties = listOf(element = name),
                    )
                }
            )

            Content.HOST_DEFINITION -> PropertyList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = getTranslation(key = "main_menu_screen__host_definition"),
                properties = hostProperty,
                id = { it.id },
                description = { it.description },
            )

            Content.HOST_TYPE -> PropertyList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = getTranslation(key = "main_menu_screen__host_types"),
                properties = hostType,
                id = { it.id },
                description = { it.description },
                leadingImage = { it.image },
            )

            Content.PLANET_DEFINITION -> PropertyList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = getTranslation(key = "main_menu_screen__planet_definition"),
                properties = planetProperty,
                id = { it.id },
                description = { it.description }
            )

            Content.PLANET_TYPE -> PropertyList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = getTranslation(key = "main_menu_screen__planet_types"),
                properties = planetType,
                id = { it.id },
                description = { it.description },
                leadingImage = { it.image }
            )

            Content.HABITABILITY -> PropertyList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = getTranslation(key = "main_menu_screen__habitability"),
                properties = habitability,
                id = { it.id },
                description = { it.description },
                footer = {
                    storeState.formula?.let { formula ->
                        val formulaTranslation = getTranslation(key = "formula")

                        val uriHandler = LocalUriHandler.current
                        val typography = LocalTypography.current
                        val colorScheme = LocalColorScheme.current
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { uriHandler.open(uri = formula) },
                            text = formulaTranslation,
                            style = typography.headlineSmall.copy(
                                color = colorScheme.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                }
            )

            Content.SCORE -> PropertyList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = getTranslation(key = "main_menu_screen__score"),
                properties = score,
                id = { it.id },
                description = { it.description }
            )
        }
    }
}

private data class Property(
    val id: String,
    val description: String,
    val image: ImageResource? = null,
    val icon: (@Composable () -> Unit)? = null
)

private val navigation = listOf(
    Property(
        id = "main_menu_screen__back_navigation",
        description = when (platform) {
            Platform.Android -> "main_menu_screen__navigation_info_android"
            Platform.Ios -> "main_menu_screen__navigation_info_ios"
            else -> "main_menu_screen__navigation_info_desktop"
        },
        icon = { Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack) },
    ),
    Property(
        id = "main_menu_screen__help_navigation",
        description = "main_menu_screen__help_navigation_description",
        icon = { Icon(imageVector = Icons.Default.QuestionMark) },
    ),
    Property(
        id = "main_menu_screen__music_navigation",
        description = "main_menu_screen__music_navigation_description",
        icon = { Icon(imageVector = Icons.Default.MusicNote) },
    ),
    Property(
        id = "main_menu_screen__feedback_navigation",
        description = "main_menu_screen__feedback_navigation_description",
        icon = { Icon(imageVector = Icons.Default.BugReport) },
    )
)
private val panel = listOf(
    Property(
        id = "help_screen__control_panel_search",
        description = "help_screen__control_panel_search_description",
        icon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.Search)
                Icon(imageVector = Icons.AutoMirrored.Filled.ManageSearch)
            }
        }
    ),
    Property(
        id = "help_screen__control_panel_view",
        description = "help_screen__control_panel_view_description",
        icon = { Icon(imageVector = Icons.Default.Public) }
    ),
    Property(
        id = "help_screen__control_panel_count",
        description = "help_screen__control_panel_count_description",
    ),
    Property(
        id = "help_screen__control_panel_sort",
        description = "help_screen__control_panel_sort_description",
        icon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowUpward)
                Icon(imageVector = Icons.AutoMirrored.Filled.Sort)
            }
        }
    ),
    Property(
        id = "help_screen__control_panel_visibility",
        description = "help_screen__control_panel_visibility_decription",
        icon = { Icon(imageVector = Icons.Default.Visibility) }
    )
)
private val hostType = listOf(
    Property(
        id = "stellar_host_type_o",
        description = "stellar_host_type_o_description",
        image = "O".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_b",
        description = "stellar_host_type_b_description",
        image = "B".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_a",
        description = "stellar_host_type_a_description",
        image = "A".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_f",
        description = "stellar_host_type_f_description",
        image = "F".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_g",
        description = "stellar_host_type_g_description",
        image = "G".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_k",
        description = "stellar_host_type_k_description",
        image = "K".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_m",
        description = "stellar_host_type_m_description",
        image = "M".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_w",
        description = "stellar_host_type_w_description",
        image = "W".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_l",
        description = "stellar_host_type_l_description",
        image = "L".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_t",
        description = "stellar_host_type_t_description",
        image = "T".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_y",
        description = "stellar_host_type_y_description",
        image = "Y".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_c",
        description = "stellar_host_type_c_description",
        image = "C".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_s",
        description = "stellar_host_type_s_description",
        image = "S".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_d",
        description = "stellar_host_type_d_description",
        image = "D".spectralTypeToImage()
    ),
    Property(
        id = "stellar_host_type_unknown",
        description = "stellar_host_type_unknown_description",
        image = "?".spectralTypeToImage()
    )
)
private val planetType = listOf(
    Property(
        id = "planet_type_terrestrial_planet",
        description = "planet_type_terrestrial_planet_description",
        image = PlanetType.TERRESTRIAL_PLANET.toImage()
    ),
    Property(
        id = "planet_type_sub_earth",
        description = "planet_type_sub_earth_description",
        image = PlanetType.SUB_EARTH.toImage()
    ),
    Property(
        id = "planet_type_super_earth",
        description = "planet_type_super_earth_description",
        image = PlanetType.SUPER_EARTH.toImage()
    ),
    Property(
        id = "planet_type_mega_earth",
        description = "planet_type_mega_earth_description",
        image = PlanetType.MEGA_EARTH.toImage()
    ),
    Property(
        id = "planet_type_earth_like_planet",
        description = "planet_type_earth_like_planet_description",
        image = PlanetType.EARTH_LIKE_PLANET.toImage()
    ),
    Property(
        id = "planet_type_earth_analog_planet",
        description = "planet_type_earth_analog_planet_description",
        image = PlanetType.EARTH_ANALOG_PLANET.toImage()
    ),
    Property(
        id = "planet_type_superhabitable_planet",
        description = "planet_type_superhabitable_planet_description",
        image = PlanetType.SUPERHABITABLE_PLANET.toImage()
    ),
    Property(
        id = "planet_type_lava_planet",
        description = "planet_type_lava_planet_description",
        image = PlanetType.LAVA_PLANET.toImage()
    ),
    Property(
        id = "planet_type_desert_planet",
        description = "planet_type_desert_planet_description",
        image = PlanetType.DESERT_PLANET.toImage()
    ),
    Property(
        id = "planet_type_ice_planet",
        description = "planet_type_ice_planet_description",
        image = PlanetType.ICE_PLANET.toImage()
    ),
    Property(
        id = "planet_type_subsurface_ocean_planet",
        description = "planet_type_subsurface_ocean_planet_description",
        image = PlanetType.SUBSURFACE_OCEAN_PLANET.toImage()
    ),
    Property(
        id = "planet_type_ocean_planet",
        description = "planet_type_ocean_planet_description",
        image = PlanetType.OCEAN_PLANET.toImage()
    ),
    Property(
        id = "planet_type_iron_planet",
        description = "planet_type_iron_planet_description",
        image = PlanetType.IRON_PLANET.toImage()
    ),
    Property(
        id = "planet_type_eyeball_planet",
        description = "planet_type_eyeball_planet_description",
        image = PlanetType.EYEBALL_PLANET.toImage()
    ),
    Property(
        id = "planet_type_hot_eyeball_planet",
        description = "planet_type_hot_eyeball_planet_description",
        image = PlanetType.HOT_EYEBALL_PLANET.toImage()
    ),
    Property(
        id = "planet_type_cold_eyeball_planet",
        description = "planet_type_cold_eyeball_planet_description",
        image = PlanetType.COLD_EYEBALL_PLANET.toImage()
    ),
    Property(
        id = "planet_type_barren_planet",
        description = "planet_type_barren_planet_description",
        image = PlanetType.BARREN_PLANET.toImage()
    ),
    Property(
        id = "planet_type_crater_planet",
        description = "planet_type_crater_planet_description",
        image = PlanetType.CRATER_PLANET.toImage()
    ),
    Property(
        id = "planet_type_chthonian_planet",
        description = "planet_type_chthonian_planet_description",
        image = PlanetType.CHTHONIAN_PLANET.toImage()
    ),
    Property(
        id = "planet_type_mini_neptune",
        description = "planet_type_mini_neptune_description",
        image = PlanetType.MINI_NEPTUNE.toImage()
    ),
    Property(
        id = "planet_type_hot_neptune",
        description = "planet_type_hot_neptune_description",
        image = PlanetType.HOT_NEPTUNE.toImage()
    ),
    Property(
        id = "planet_type_ultra_hot_neptune",
        description = "planet_type_ultra_hot_neptune_description",
        image = PlanetType.ULTRA_HOT_NEPTUNE.toImage()
    ),
    Property(
        id = "planet_type_super_neptune",
        description = "planet_type_super_neptune_description",
        image = PlanetType.SUPER_NEPTUNE.toImage()
    ),
    Property(
        id = "planet_type_ice_giant",
        description = "planet_type_ice_giant_description",
        image = PlanetType.ICE_GIANT.toImage()
    ),
    Property(
        id = "planet_type_gas_giant",
        description = "planet_type_gas_giant_description",
        image = PlanetType.GAS_GIANT.toImage()
    ),
    Property(
        id = "planet_type_super_jupiter",
        description = "planet_type_super_jupiter_description",
        image = PlanetType.SUPER_JUPITER.toImage()
    ),
    Property(
        id = "planet_type_hot_jupiter",
        description = "planet_type_hot_jupiter_description",
        image = PlanetType.HOT_JUPITER.toImage()
    ),
    Property(
        id = "planet_type_ultra_hot_jupiter",
        description = "planet_type_ultra_hot_jupiter_description",
        image = PlanetType.ULTRA_HOT_JUPITER.toImage()
    ),
    Property(
        id = "planet_type_ammonia_clouds_gas_giant",
        description = "planet_type_ammonia_clouds_gas_giant_description",
        image = PlanetType.AMMONIA_CLOUDS_GAS_GIANT.toImage()
    ),
    Property(
        id = "planet_type_water_clouds_gas_giant",
        description = "planet_type_water_clouds_gas_giant_description",
        image = PlanetType.WATER_CLOUDS_GAS_GIANT.toImage()
    ),
    Property(
        id = "planet_type_cloudless_gas_giant",
        description = "planet_type_cloudless_gas_giant_description",
        image = PlanetType.CLOUDLESS_GAS_GIANT.toImage()
    ),
    Property(
        id = "planet_type_alkali_metal_clouds_gas_giant",
        description = "planet_type_alkali_metal_clouds_gas_giant_description",
        image = PlanetType.ALKALI_METAL_CLOUDS_GAS_GIANT.toImage()
    ),
    Property(
        id = "planet_type_silicate_clouds_gas_giant",
        description = "planet_type_silicate_clouds_gas_giant_description",
        image = PlanetType.SILICATE_CLOUDS_GAS_GIANT.toImage()
    ),
    Property(
        id = "planet_type_puffy_planet",
        description = "planet_type_puffy_planet_description",
        image = PlanetType.PUFFY_PLANET.toImage()
    ),
    Property(
        id = "planet_type_super_puff_planet",
        description = "planet_type_super_puff_planet_description",
        image = PlanetType.SUPER_PUFF_PLANET.toImage()
    ),
    Property(
        id = "planet_type_protoplanet",
        description = "planet_type_protoplanet_description",
        image = PlanetType.PROTOPLANET.toImage()
    ),
    Property(
        id = "planet_type_ultra_short_period_planet",
        description = "planet_type_ultra_short_period_planet_description",
        image = PlanetType.ULTRA_SHORT_PERIOD_PLANET.toImage()
    ),
    Property(
        id = "planet_type_disrupted_planet",
        description = "planet_type_disrupted_planet_description",
        image = PlanetType.DISRUPTED_PLANET.toImage()
    ),
    Property(
        id = "planet_type_ellipsoid_planet",
        description = "planet_type_ellipsoid_planet_description",
        image = PlanetType.ELLIPSOID_PLANET.toImage()
    ),
    Property(
        id = "planet_type_unknown",
        description = "planet_type_unknown_description",
        image = PlanetType.UNKNOWN.toImage()
    )
)
private val hostProperty = listOf(
    Property(
        id = "stellar_host_name",
        description = "stellar_host_name_description",
    ),
    Property(
        id = "stellar_host_system_name",
        description = "stellar_host_system_name_description",
    ),
    Property(
        id = "stellar_host_planet_count",
        description = "stellar_host_planet_count_description",
    ),
    Property(
        id = "stellar_host_type",
        description = "stellar_host_type_description",
    ),
    Property(
        id = "stellar_host_temperature",
        description = "stellar_host_temperature_description",
    ),
    Property(
        id = "stellar_host_radius",
        description = "stellar_host_radius_description",
    ),
    Property(
        id = "stellar_host_mass",
        description = "stellar_host_mass_description",
    ),
    Property(
        id = "stellar_host_metallicity",
        description = "stellar_host_metallicity_description",
    ),
    Property(
        id = "stellar_host_luminosity",
        description = "stellar_host_luminosity_description",
    ),
    Property(
        id = "stellar_host_gravity",
        description = "stellar_host_gravity_description",
    ),
    Property(
        id = "stellar_host_age",
        description = "stellar_host_age_description",
    ),
    Property(
        id = "stellar_host_density",
        description = "stellar_host_density_description",
    ),
    Property(
        id = "stellar_host_rotational_velocity",
        description = "stellar_host_rotational_velocity_description",
    ),
    Property(
        id = "stellar_host_rotational_period",
        description = "stellar_host_rotational_period_description",
    ),
    Property(
        id = "stellar_host_ra",
        description = "stellar_host_ra_description",
    ),
    Property(
        id = "stellar_host_dec",
        description = "stellar_host_dec_description",
    ),
    Property(
        id = "stellar_host_distance",
        description = "stellar_host_distance_description",
    )
)
private val planetProperty = listOf(
    Property(
        id = "planet_name",
        description = "planet_name_description",
    ),
    Property(
        id = "planet_status",
        description = "planet_status_description",
    ),
    Property(
        id = "planet_type",
        description = "planet_type_description",
    ),
    Property(
        id = "planet_orbital_period",
        description = "planet_orbital_period_description",
    ),
    Property(
        id = "planet_orbit_axis",
        description = "planet_orbit_axis_description",
    ),
    Property(
        id = "planet_radius",
        description = "planet_radius_description",
    ),
    Property(
        id = "planet_mass",
        description = "planet_mass_description",
    ),
    Property(
        id = "planet_density",
        description = "planet_density_description",
    ),
    Property(
        id = "planet_eccentricity",
        description = "planet_eccentricity_description",
    ),
    Property(
        id = "planet_insolation_flux",
        description = "planet_insolation_flux_description",
    ),
    Property(
        id = "planet_temperature",
        description = "planet_temperature_description",
    ),
    Property(
        id = "planet_occultation_depth",
        description = "planet_occultation_depth_description",
    ),
    Property(
        id = "planet_inclination",
        description = "planet_inclination_description",
    ),
    Property(
        id = "planet_obliquity",
        description = "planet_obliquity_description",
    )
)
private val habitability = listOf(
    Property(
        id = "habitability",
        description = "habitability_description",
    ),
    Property(
        id = "habitability_roche",
        description = "habitability_roche_description",
    ),
    Property(
        id = "habitability_hz",
        description = "habitability_hz_description",
    ),
    Property(
        id = "habitability_planet_density_radius",
        description = "habitability_planet_density_radius_description",
    ),
    Property(
        id = "habitability_planet_mass",
        description = "habitability_planet_mass_description",
    ),
    Property(
        id = "habitability_planet_eccentricity",
        description = "habitability_planet_eccentricity_description",
    ),
    Property(
        id = "habitability_planet_temperature",
        description = "habitability_planet_temperature_description",
    ),
    Property(
        id = "habitability_planet_obliquity",
        description = "habitability_planet_obliquity_description",
    ),
    Property(
        id = "habitability_planet_protection",
        description = "habitability_planet_protection_description",
    ),
    Property(
        id = "habitability_planet_tidal_locking",
        description = "habitability_planet_tidal_locking_description",
    ),
    Property(
        id = "habitability_planet_esi",
        description = "habitability_planet_esi_description",
    ),
    Property(
        id = "habitability_star_spectral_type",
        description = "habitability_star_spectral_type_description",
    ),
    Property(
        id = "habitability_star_age",
        description = "habitability_star_age_description",
    ),
    Property(
        id = "habitability_star_activity",
        description = "habitability_star_activity_description",
    ),
    Property(
        id = "habitability_star_gravity",
        description = "habitability_star_gravity_description",
    ),
    Property(
        id = "habitability_star_metallicity",
        description = "habitability_star_metallicity_description",
    ),
    Property(
        id = "habitability_star_effective_temperature",
        description = "habitability_star_effective_temperature_description",
    )
)
private val score = listOf(
    Property(
        id = "help_screen__score_formula",
        description = "help_screen__score_formula_description",
    ),
    Property(
        id = "help_screen__score_habitability",
        description = "help_screen__score_habitability_description",
    ),
    Property(
        id = "help_screen__score_habitability_requirements",
        description = "help_screen__score_habitability_requirements_description",
    ),
)

@Preview
@Composable
private fun HelpScreenLoadingPreview() = AppTheme {
    HelpScreen(
        store = Store(
            initialState = HelpState(
                loading = true,
            )
        )
    )
}

@Preview
@Composable
private fun HelpScreenPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__learn",
                value = "Help"
            ),
            Translation(
                key = "main_menu_screen__navigation",
                value = "Navigation"
            ),
            Translation(
                key = "main_menu_screen__host_definition",
                value = "Star Definition"
            ),
            Translation(
                key = "main_menu_screen__definition_example",
                value = "Example"
            ),
            Translation(
                key = "main_menu_screen__definition_properties",
                value = "Properties"
            ),
            Translation(
                key = "main_menu_screen__planet_definition",
                value = "Planet Definition"
            ),
            Translation(
                key = "main_menu_screen__host_types",
                value = "Host Types"
            ),
            Translation(
                key = "main_menu_screen__planet_types",
                value = "Planet Types"
            ),
            Translation(
                key = "main_menu_screen__habitability",
                value = "Habitability Formula"
            ),
            Translation(
                key = "main_menu_screen__mechanics",
                value = "Tutorial"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    HelpScreen(
        store = Store(
            initialState = HelpState(
                loading = false,
                formula = "Formula",
                currentContent = Content.LEARN_MENU
            )
        )
    )
}

@Preview
@Composable
private fun HelpScreenHostDefinitionPreview() = AppTheme {
    HelpScreen(
        store = Store(
            initialState = HelpState(
                loading = false,
                formula = "Formula",
                currentContent = Content.HOST_DEFINITION
            )
        )
    )
}

@Preview
@Composable
private fun HelpScreenPlanetDefinitionPreview() = AppTheme {
    HelpScreen(
        store = Store(
            initialState = HelpState(
                loading = false,
                formula = "Formula",
                currentContent = Content.PLANET_DEFINITION
            )
        )
    )
}

@Preview
@Composable
private fun HelpScreenHabitabilityPreview() = AppTheme {
    HelpScreen(
        store = Store(
            initialState = HelpState(
                loading = false,
                formula = "Formula",
                currentContent = Content.HABITABILITY
            )
        )
    )
}
