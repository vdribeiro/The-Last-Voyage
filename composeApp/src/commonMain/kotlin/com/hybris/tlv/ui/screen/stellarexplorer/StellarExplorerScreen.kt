package com.hybris.tlv.ui.screen.stellarexplorer

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.hybris.tlv.ui.screen.stellarexplorer.content.PlanetContent
import com.hybris.tlv.ui.screen.stellarexplorer.content.StellarHostContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.ControlPanel
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun StellarExplorerScreen(store: Store<StellarExplorerAction, StellarExplorerState>) {
    val storeState by store.stateFlow.collectAsState()
    val stellarHostProperties = remember { StellarHostProperty.entries.associateWith { getTranslation(key = it.displayName) } }
    val planetProperties = remember { PlanetProperty.entries.associateWith { getTranslation(key = it.displayName) } }
    val hostListTranslation = remember { getTranslation(key = "stellar_explorer_screen__host_list") }
    val planetListTranslation = remember { getTranslation(key = "stellar_explorer_screen__planet_list") }

    // Control panel definitions according to selected view (property visibility, sort, search, etc...)
    val enabled: Boolean
    val viewName: String
    val viewIcon: ImageVector
    val count: String
    val properties: List<String>
    val selectedProperty: String
    val onSortChange: (String) -> Unit
    val visibleProperties: List<String>
    val onVisibilityChange: (String) -> Unit
    val selectedProperties: List<String>
    val onFiltersChange: (String) -> Unit

    when (storeState.currentContent) {
        Content.LIST_HOSTS -> {
            enabled = true
            viewName = hostListTranslation
            viewIcon = Icons.Default.Flare
            count = storeState.filteredStellarHosts.size.toString()
            properties = stellarHostProperties.values.toList()
            selectedProperty = stellarHostProperties[storeState.sortStellarHostProperty].orEmpty()
            onSortChange = { property ->
                stellarHostProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.SortStellarHosts(sort = it))
                }
            }
            visibleProperties = storeState.visibleStellarHostProperties.mapNotNull { stellarHostProperties[it] }
            onVisibilityChange = { property ->
                stellarHostProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.ChangeStellarHostsVisibility(property = it))
                }
            }
            selectedProperties = storeState.searchableStellarHostProperties.mapNotNull { stellarHostProperties[it] }
            onFiltersChange = { property ->
                stellarHostProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.ChangeStellarHostsSearchable(property = it))
                }
            }
        }

        Content.DETAIL_HOSTS -> {
            enabled = false
            viewName = hostListTranslation
            viewIcon = Icons.Default.Flare
            count = storeState.filteredStellarHosts.size.toString()
            properties = stellarHostProperties.values.toList()
            selectedProperty = stellarHostProperties[storeState.sortStellarHostProperty].orEmpty()
            onSortChange = { property ->
                stellarHostProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.SortStellarHosts(sort = it))
                }
            }
            visibleProperties = storeState.visibleStellarHostProperties.mapNotNull { stellarHostProperties[it] }
            onVisibilityChange = { property ->
                stellarHostProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.ChangeStellarHostsVisibility(property = it))
                }
            }
            selectedProperties = storeState.searchableStellarHostProperties.mapNotNull { stellarHostProperties[it] }
            onFiltersChange = { property ->
                stellarHostProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.ChangeStellarHostsSearchable(property = it))
                }
            }
        }

        Content.LIST_PLANETS -> {
            enabled = true
            viewName = planetListTranslation
            viewIcon = Icons.Default.Public
            count = storeState.filteredPlanets.size.toString()
            properties = planetProperties.values.toList()
            selectedProperty = planetProperties[storeState.sortPlanetProperty].orEmpty()
            onSortChange = { property ->
                planetProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.SortPlanets(sort = it))
                }
            }
            visibleProperties = storeState.visiblePlanetProperties.mapNotNull { planetProperties[it] }
            onVisibilityChange = { property ->
                planetProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.ChangePlanetVisibility(property = it))
                }
            }
            selectedProperties = storeState.searchablePlanetProperties.mapNotNull { planetProperties[it] }
            onFiltersChange = { property ->
                planetProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.ChangePlanetSearchable(property = it))
                }
            }
        }

        Content.DETAIL_PLANETS -> {
            enabled = false
            viewName = planetListTranslation
            viewIcon = Icons.Default.Public
            count = storeState.filteredPlanets.size.toString()
            properties = planetProperties.values.toList()
            selectedProperty = planetProperties[storeState.sortPlanetProperty].orEmpty()
            onSortChange = { property ->
                planetProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.SortPlanets(sort = it))
                }
            }
            visibleProperties = storeState.visiblePlanetProperties.mapNotNull { planetProperties[it] }
            onVisibilityChange = { property ->
                planetProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.ChangePlanetVisibility(property = it))
                }
            }
            selectedProperties = storeState.searchablePlanetProperties.mapNotNull { planetProperties[it] }
            onFiltersChange = { property ->
                planetProperties.entries.find { it.value == property }?.key?.let {
                    store.send(action = StellarExplorerAction.ChangePlanetSearchable(property = it))
                }
            }
        }
    }

    Screen(
        modifier = Modifier
            .testTag(tag = STELLAR_EXPLORER_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        topBar = {
            ControlPanel(
                modifier = Modifier.statusBarsPadding(),
                enabled = enabled,
                search = storeState.search,
                onSearch = { store.send(action = StellarExplorerAction.Search(search = it)) },
                viewName = viewName,
                viewIcon = viewIcon,
                onChangeView = { store.send(action = StellarExplorerAction.ChangeView) },
                count = count,
                properties = properties,
                selectedProperty = selectedProperty,
                ascending = storeState.sortAscending,
                onSortChange = onSortChange,
                onSortDirectionChange = { store.send(action = StellarExplorerAction.ChangeSortDirection) },
                visibleProperties = visibleProperties,
                onVisibilityChange = onVisibilityChange,
                selectedProperties = selectedProperties,
                onFiltersChange = onFiltersChange,
            )
        }
    ) {
        when (storeState.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_PLANETS -> StellarHostContent(store = store)
            Content.LIST_PLANETS, Content.DETAIL_HOSTS -> PlanetContent(store = store)
        }
    }
}
