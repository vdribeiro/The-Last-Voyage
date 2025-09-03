package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.runtime.Composable
import com.hybris.tlv.ui.component.Section
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun HabitabilityContent(store: Store<MainMenuAction, MainMenuState>) {

    Section(title = getTranslation(key = "main_menu_screen__habitability"), sections = habitability)
}

private val habitability by lazy {
    listOf(
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_title"),
            description = getTranslation(key = "main_menu_screen__habitability_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_roche_title"),
            description = getTranslation(key = "main_menu_screen__habitability_roche_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_hz_title"),
            description = getTranslation(key = "main_menu_screen__habitability_hz_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_planet_density_radius_title"),
            description = getTranslation(key = "main_menu_screen__habitability_planet_density_radius_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_planet_mass_title"),
            description = getTranslation(key = "main_menu_screen__habitability_planet_mass_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_planet_eccentricity_title"),
            description = getTranslation(key = "main_menu_screen__habitability_planet_eccentricity_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_planet_temperature_title"),
            description = getTranslation(key = "main_menu_screen__habitability_planet_temperature_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_planet_obliquity_title"),
            description = getTranslation(key = "main_menu_screen__habitability_planet_obliquity_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_planet_protection_title"),
            description = getTranslation(key = "main_menu_screen__habitability_planet_protection_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_planet_tidal_locking_title"),
            description = getTranslation(key = "main_menu_screen__habitability_planet_tidal_locking_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_planet_esi_title"),
            description = getTranslation(key = "main_menu_screen__habitability_planet_esi_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_star_spectral_type_title"),
            description = getTranslation(key = "main_menu_screen__habitability_star_spectral_type_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_star_age_title"),
            description = getTranslation(key = "main_menu_screen__habitability_star_age_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_star_activity_title"),
            description = getTranslation(key = "main_menu_screen__habitability_star_activity_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_star_gravity_title"),
            description = getTranslation(key = "main_menu_screen__habitability_star_gravity_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_star_metallicity_title"),
            description = getTranslation(key = "main_menu_screen__habitability_star_metallicity_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__habitability_star_effective_temperature_title"),
            description = getTranslation(key = "main_menu_screen__habitability_star_effective_temperature_description")
        ),
    )
}
