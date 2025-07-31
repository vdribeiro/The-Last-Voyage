package com.hybris.tlv.ui.screen.stellarexplorer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.ControlPanel
import com.hybris.tlv.ui.component.LazyListIndex
import com.hybris.tlv.ui.component.StellarHostCard
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
            val enabled = when (storeState.currentContent) {
                Content.LIST_HOSTS, Content.LIST_PLANETS -> true
                null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> false
            }
            val viewName = getTranslation(
                key = when (storeState.currentContent) {
                    Content.LIST_HOSTS, Content.DETAIL_PLANETS -> "stellar_explorer_screen__host_list"
                    Content.LIST_PLANETS, Content.DETAIL_HOSTS -> "stellar_explorer_screen__planet_list"
                    null -> ""
                }
            )
            val properties = when (storeState.currentContent) {
                Content.LIST_HOSTS -> stellarHostProperties
                Content.LIST_PLANETS -> planetProperties
                null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> emptyList()
            }
            val selectedProperty = "TODO"
            ControlPanel(
                enabled = enabled,
                onSearch = { store.send(action = StellarExplorerAction.Search(search = it)) },
                viewName = viewName,
                onChangeView = { store.send(action = StellarExplorerAction.ChangeView) },
                properties = properties,
                selectedProperty = selectedProperty,
                ascending = storeState.sortAscending,
                onSortChange = { store.send(action = StellarExplorerAction.Sort(sort = it)) },
                onSortDirectionChange = { store.send(action = StellarExplorerAction.ChangeSortDirection) },
                visibleProperties = storeState.visibleProperties,
                onVisibilityChange = { store.send(action = StellarExplorerAction.ChangeVisibility(property = it)) }
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
    listOf(
        getTranslation(key = "stellar_host_system_name"),
        getTranslation(key = "stellar_host_spectral_type"),
        getTranslation(key = "stellar_host_temperature"),
        getTranslation(key = "stellar_host_spectral_radius"),
        getTranslation(key = "stellar_host_spectral_mass"),
        getTranslation(key = "stellar_host_spectral_metallicity"),
        getTranslation(key = "stellar_host_spectral_luminosity"),
        getTranslation(key = "stellar_host_spectral_gravity"),
        getTranslation(key = "stellar_host_spectral_age"),
        getTranslation(key = "stellar_host_spectral_density"),
        getTranslation(key = "stellar_host_spectral_rotational_velocity"),
        getTranslation(key = "stellar_host_spectral_rotational_period"),
        getTranslation(key = "stellar_host_distance"),
        getTranslation(key = "stellar_host_spectral_ra"),
        getTranslation(key = "stellar_host_spectral_dec")
    )
}

private val planetProperties by lazy {
    listOf(
        getTranslation(key = "planet_status"),
        getTranslation(key = "planet_habitability"),
        getTranslation(key = "planet_orbital_period"),
        getTranslation(key = "planet_orbit_axis"),
        getTranslation(key = "planet_radius"),
        getTranslation(key = "planet_mass"),
        getTranslation(key = "planet_density"),
        getTranslation(key = "planet_eccentricity"),
        getTranslation(key = "planet_insolation_flux"),
        getTranslation(key = "planet_temperature"),
        getTranslation(key = "planet_occultation_depth"),
        getTranslation(key = "planet_inclination"),
        getTranslation(key = "planet_obliquity")
    )
}
