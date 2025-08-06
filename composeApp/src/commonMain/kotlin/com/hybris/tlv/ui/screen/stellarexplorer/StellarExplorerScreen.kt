package com.hybris.tlv.ui.screen.stellarexplorer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.ui.component.ControlPanel
import com.hybris.tlv.ui.screen.stellarexplorer.content.PlanetContent
import com.hybris.tlv.ui.screen.stellarexplorer.content.StellarHostContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun StellarExplorerScreen(store: Store<StellarExplorerAction, StellarExplorerState>) {
    val storeState by store.stateFlow.collectAsState()

    BackHandler(enabled = true) { store.send(action = StellarExplorerAction.Back) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ControlPanel(
                modifier = Modifier.statusBarsPadding(),
                enabled = when (storeState.currentContent) {
                    Content.LIST_HOSTS, Content.LIST_PLANETS -> true
                    null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> false
                },
                onSearch = { store.send(action = StellarExplorerAction.Search(search = it)) },
                viewName = getTranslation(
                    key = when (storeState.currentContent) {
                        Content.LIST_HOSTS, Content.DETAIL_HOSTS -> "stellar_explorer_screen__host_list"
                        Content.LIST_PLANETS, Content.DETAIL_PLANETS -> "stellar_explorer_screen__planet_list"
                        null -> ""
                    }
                ),
                onChangeView = { store.send(action = StellarExplorerAction.ChangeView) },
                properties = when (storeState.currentContent) {
                    Content.LIST_HOSTS -> stellarHostProperties.values
                    Content.LIST_PLANETS -> planetProperties.values
                    null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> emptyList()
                }.toList(),
                selectedProperty = when (storeState.currentContent) {
                    Content.LIST_HOSTS -> stellarHostProperties[storeState.sortStellarHostProperty]
                    Content.LIST_PLANETS -> planetProperties[storeState.sortPlanetProperty]
                    null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> null
                }.orEmpty(),
                ascending = storeState.sortAscending,
                onSortChange = { sort ->
                    when (storeState.currentContent) {
                        Content.LIST_HOSTS -> store.send(
                            action = StellarExplorerAction.SortStellarHosts(
                                sort = stellarHostProperties.entries.find { it.value == sort }?.key ?: StellarHostProperty.DISTANCE
                            )
                        )

                        Content.LIST_PLANETS -> store.send(
                            action = StellarExplorerAction.SortPlanets(
                                sort = planetProperties.entries.find { it.value == sort }?.key ?: PlanetProperty.NAME
                            )
                        )

                        null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
                    }
                },
                onSortDirectionChange = { store.send(action = StellarExplorerAction.ChangeSortDirection) },
                visibleProperties = when (storeState.currentContent) {
                    Content.LIST_HOSTS -> storeState.visibleStellarHostProperties.mapNotNull { stellarHostProperties[it] }
                    Content.LIST_PLANETS -> storeState.visiblePlanetProperties.mapNotNull { planetProperties[it] }
                    null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> emptyList()
                },
                onVisibilityChange = { property ->
                    when (storeState.currentContent) {
                        Content.LIST_HOSTS -> store.send(
                            action = StellarExplorerAction.ChangeStellarHostsVisibility(
                                property = stellarHostProperties.entries.find { it.value == property }?.key ?: StellarHostProperty.DISTANCE
                            )
                        )

                        Content.LIST_PLANETS -> store.send(
                            action = StellarExplorerAction.ChangePlanetVisibility(
                                property = planetProperties.entries.find { it.value == property }?.key ?: PlanetProperty.NAME
                            )
                        )

                        null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (storeState.currentContent) {
                null -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Content.LIST_HOSTS, Content.DETAIL_PLANETS -> StellarHostContent(store = store)
                Content.LIST_PLANETS, Content.DETAIL_HOSTS -> PlanetContent(store = store)
            }
        }
    }
}

private val stellarHostProperties by lazy {
    mapOf(
        StellarHostProperty.NAME to getTranslation(key = "stellar_host_name"),
        StellarHostProperty.SYSTEM_NAME to getTranslation(key = "stellar_host_system_name"),
        StellarHostProperty.PLANET_COUNT to getTranslation(key = "stellar_host_planet_count"),
        StellarHostProperty.SPECTRAL_TYPE to getTranslation(key = "stellar_host_spectral_type"),
        StellarHostProperty.TEMPERATURE to getTranslation(key = "stellar_host_temperature"),
        StellarHostProperty.RADIUS to getTranslation(key = "stellar_host_spectral_radius"),
        StellarHostProperty.MASS to getTranslation(key = "stellar_host_spectral_mass"),
        StellarHostProperty.METALLICITY to getTranslation(key = "stellar_host_spectral_metallicity"),
        StellarHostProperty.LUMINOSITY to getTranslation(key = "stellar_host_spectral_luminosity"),
        StellarHostProperty.GRAVITY to getTranslation(key = "stellar_host_spectral_gravity"),
        StellarHostProperty.AGE to getTranslation(key = "stellar_host_spectral_age"),
        StellarHostProperty.DENSITY to getTranslation(key = "stellar_host_spectral_density"),
        StellarHostProperty.ROTATIONAL_VELOCITY to getTranslation(key = "stellar_host_spectral_rotational_velocity"),
        StellarHostProperty.ROTATIONAL_PERIOD to getTranslation(key = "stellar_host_spectral_rotational_period"),
        StellarHostProperty.DISTANCE to getTranslation(key = "stellar_host_distance"),
        StellarHostProperty.RA to getTranslation(key = "stellar_host_spectral_ra"),
        StellarHostProperty.DEC to getTranslation(key = "stellar_host_spectral_dec")
    )
}

private val planetProperties by lazy {
    mapOf(
        PlanetProperty.NAME to getTranslation(key = "planet_name"),
        PlanetProperty.STATUS to getTranslation(key = "planet_status"),
        PlanetProperty.HABITABILITY to getTranslation(key = "planet_habitability"),
        PlanetProperty.TYPE to getTranslation(key = "planet_type"),
        PlanetProperty.ORBITAL_PERIOD to getTranslation(key = "planet_orbital_period"),
        PlanetProperty.ORBIT_AXIS to getTranslation(key = "planet_orbit_axis"),
        PlanetProperty.RADIUS to getTranslation(key = "planet_radius"),
        PlanetProperty.MASS to getTranslation(key = "planet_mass"),
        PlanetProperty.DENSITY to getTranslation(key = "planet_density"),
        PlanetProperty.ECCENTRICITY to getTranslation(key = "planet_eccentricity"),
        PlanetProperty.INCLINATION to getTranslation(key = "planet_insolation_flux"),
        PlanetProperty.TEMPERATURE to getTranslation(key = "planet_temperature"),
        PlanetProperty.OCCULTATION_DEPTH to getTranslation(key = "planet_occultation_depth"),
        PlanetProperty.INCLINATION to getTranslation(key = "planet_inclination"),
        PlanetProperty.OBLIQUITY to getTranslation(key = "planet_obliquity")
    )
}
