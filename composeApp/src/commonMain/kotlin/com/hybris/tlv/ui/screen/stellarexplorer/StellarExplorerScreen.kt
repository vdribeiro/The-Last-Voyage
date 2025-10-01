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
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.screen.stellarexplorer.content.PlanetContent
import com.hybris.tlv.ui.screen.stellarexplorer.content.StellarHostContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.ControlPanel
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StellarExplorerScreen(store: Store<StellarExplorerState, StellarExplorerAction>) {
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
                modifier = Modifier
                    .testTag(tag = STELLAR_EXPLORER_SCREEN_CONTROL_PANEL)
                    .statusBarsPadding(),
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

@Preview
@Composable
private fun StellarExplorerLoading() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
    AppTheme {
        StellarExplorerScreen(
            store = getStore(
                initialState = StellarExplorerState(
                    loading = true,
                    currentContent = Content.LIST_HOSTS,
                    listIndex = LazyListIndex(),
                    stellarHosts = emptyList(),
                    planets = emptyList(),
                    filteredStellarHosts = emptyList(),
                    filteredPlanets = emptyList(),
                    selectedStellarHost = null,
                    selectedPlanet = null,
                    search = "",
                    sortStellarHostProperty = StellarHostProperty.DISTANCE,
                    sortPlanetProperty = PlanetProperty.HABITABILITY,
                    sortAscending = true,
                    visibleStellarHostProperties = emptySet(),
                    visiblePlanetProperties = emptySet(),
                    searchableStellarHostProperties = setOf(StellarHostProperty.NAME),
                    searchablePlanetProperties = setOf(PlanetProperty.NAME)
                )
            )
        )
    }
}

@Preview
@Composable
private fun StellarExplorerHostList() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
    AppTheme {
        StellarExplorerScreen(
            store = getStore(
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.LIST_HOSTS,
                    listIndex = LazyListIndex(),
                    stellarHosts = emptyList(),
                    planets = emptyList(),
                    filteredStellarHosts = listOf(
                        StellarHost(
                            id = "sol",
                            name = "Sol",
                            systemName = "Sol",
                            spectralType = "G2V",
                            effectiveTemperature = 5778.0,
                            radius = 1.0,
                            mass = 1.0,
                            metallicity = 0.0,
                            luminosity = 1.0,
                            gravity = 1.0,
                            age = 4.6,
                            density = 1.410,
                            rotationalVelocity = 2.0,
                            rotationalPeriod = 25.05,
                            distance = 0.0,
                            ra = 0.0,
                            dec = 0.0
                        ),
                        StellarHost(
                            id = "proxima_centauri",
                            name = "Proxima Centauri",
                            systemName = "Alpha Centauri",
                            spectralType = "M5.5V",
                            effectiveTemperature = 2900.0,
                            radius = 0.141,
                            mass = 0.1221,
                            metallicity = null,
                            luminosity = -2.8,
                            gravity = 5.3201025,
                            age = null,
                            density = 48.7626491,
                            rotationalVelocity = null,
                            rotationalPeriod = 90.0,
                            distance = 4.2439092564,
                            ra = 217.3934657,
                            dec = -62.6761821
                        ),
                    ),
                    filteredPlanets = emptyList(),
                    selectedStellarHost = null,
                    selectedPlanet = null,
                    search = "",
                    sortStellarHostProperty = StellarHostProperty.entries.random(),
                    sortPlanetProperty = PlanetProperty.entries.random(),
                    sortAscending = true,
                    visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
                    visiblePlanetProperties = emptySet(),
                    searchableStellarHostProperties = emptySet(),
                    searchablePlanetProperties = emptySet()
                )
            )
        )
    }
}

@Preview
@Composable
private fun StellarExplorerHostDetail() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
    AppTheme {
        StellarExplorerScreen(
            store = getStore(
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.DETAIL_HOSTS,
                    listIndex = LazyListIndex(),
                    stellarHosts = emptyList(),
                    planets = emptyList(),
                    filteredStellarHosts = emptyList(),
                    filteredPlanets = listOf(
                        Planet(
                            id = "earth",
                            name = "Earth",
                            stellarHostId = "sol",
                            status = PlanetStatus.CONFIRMED,
                            orbitalPeriod = 365.2,
                            orbitAxis = 1.000,
                            radius = 1.0,
                            mass = 1.0,
                            density = 5.514,
                            eccentricity = 0.017,
                            insolationFlux = 1.000,
                            equilibriumTemperature = 255.0,
                            occultationDepth = 0.000084,
                            inclination = 0.0,
                            obliquity = 23.4,
                        ),
                        Planet(
                            id = "mars",
                            name = "Mars",
                            stellarHostId = "sol",
                            status = PlanetStatus.CONFIRMED,
                            orbitalPeriod = 687.0,
                            orbitAxis = 1.524,
                            radius = 0.532,
                            mass = 0.107,
                            density = 3.934,
                            eccentricity = 0.094,
                            insolationFlux = 0.430,
                            equilibriumTemperature = 210.0,
                            occultationDepth = 0.000024,
                            inclination = 1.85,
                            obliquity = 25.2,
                        ),
                    ),
                    selectedStellarHost = StellarHost(
                        id = "sol",
                        name = "Sol",
                        systemName = "Sol",
                        spectralType = "G2V",
                        effectiveTemperature = 5778.0,
                        radius = 1.0,
                        mass = 1.0,
                        metallicity = 0.0,
                        luminosity = 1.0,
                        gravity = 1.0,
                        age = 4.6,
                        density = 1.410,
                        rotationalVelocity = 2.0,
                        rotationalPeriod = 25.05,
                        distance = 0.0,
                        ra = 0.0,
                        dec = 0.0
                    ),
                    selectedPlanet = null,
                    search = "",
                    sortStellarHostProperty = StellarHostProperty.entries.random(),
                    sortPlanetProperty = PlanetProperty.entries.random(),
                    sortAscending = true,
                    visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
                    visiblePlanetProperties = emptySet(),
                    searchableStellarHostProperties = emptySet(),
                    searchablePlanetProperties = emptySet()
                )
            )
        )
    }
}

@Preview
@Composable
private fun StellarExplorerSearchHosts() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
    AppTheme {
        StellarExplorerScreen(
            store = getStore(
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.LIST_HOSTS,
                    listIndex = LazyListIndex(),
                    stellarHosts = emptyList(),
                    planets = emptyList(),
                    filteredStellarHosts = emptyList(),
                    filteredPlanets = emptyList(),
                    selectedStellarHost = null,
                    selectedPlanet = null,
                    search = "Kepler",
                    sortStellarHostProperty = StellarHostProperty.entries.random(),
                    sortPlanetProperty = PlanetProperty.entries.random(),
                    sortAscending = true,
                    visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
                    visiblePlanetProperties = emptySet(),
                    searchableStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
                    searchablePlanetProperties = emptySet()
                )
            )
        )
    }
}

@Preview
@Composable
private fun StellarExplorerPlanetList() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__planet_list",
                value = "Planets"
            ),
        )
    )
    AppTheme {
        StellarExplorerScreen(
            store = getStore(
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.LIST_PLANETS,
                    listIndex = LazyListIndex(),
                    stellarHosts = emptyList(),
                    planets = emptyList(),
                    filteredStellarHosts = emptyList(),
                    filteredPlanets = listOf(
                        Planet(
                            id = "earth",
                            name = "Earth",
                            stellarHostId = "sol",
                            status = PlanetStatus.CONFIRMED,
                            orbitalPeriod = 365.2,
                            orbitAxis = 1.000,
                            radius = 1.0,
                            mass = 1.0,
                            density = 5.514,
                            eccentricity = 0.017,
                            insolationFlux = 1.000,
                            equilibriumTemperature = 255.0,
                            occultationDepth = 0.000084,
                            inclination = 0.0,
                            obliquity = 23.4,
                        ),
                        Planet(
                            id = "mars",
                            name = "Mars",
                            stellarHostId = "sol",
                            status = PlanetStatus.CONFIRMED,
                            orbitalPeriod = 687.0,
                            orbitAxis = 1.524,
                            radius = 0.532,
                            mass = 0.107,
                            density = 3.934,
                            eccentricity = 0.094,
                            insolationFlux = 0.430,
                            equilibriumTemperature = 210.0,
                            occultationDepth = 0.000024,
                            inclination = 1.85,
                            obliquity = 25.2,
                        ),
                    ),
                    selectedStellarHost = null,
                    selectedPlanet = null,
                    search = "",
                    sortStellarHostProperty = StellarHostProperty.entries.random(),
                    sortPlanetProperty = PlanetProperty.entries.random(),
                    sortAscending = true,
                    visibleStellarHostProperties = emptySet(),
                    visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
                    searchableStellarHostProperties = emptySet(),
                    searchablePlanetProperties = emptySet()
                )
            )
        )
    }
}

@Preview
@Composable
private fun StellarExplorerPlanetDetail() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__planet_list",
                value = "Planets"
            ),
        )
    )
    AppTheme {
        StellarExplorerScreen(
            store = getStore(
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.DETAIL_PLANETS,
                    listIndex = LazyListIndex(),
                    stellarHosts = emptyList(),
                    planets = emptyList(),
                    filteredStellarHosts = listOf(
                        StellarHost(
                            id = "sol",
                            name = "Sol",
                            systemName = "Sol",
                            spectralType = "G2V",
                            effectiveTemperature = 5778.0,
                            radius = 1.0,
                            mass = 1.0,
                            metallicity = 0.0,
                            luminosity = 1.0,
                            gravity = 1.0,
                            age = 4.6,
                            density = 1.410,
                            rotationalVelocity = 2.0,
                            rotationalPeriod = 25.05,
                            distance = 0.0,
                            ra = 0.0,
                            dec = 0.0
                        ),
                    ),
                    filteredPlanets = emptyList(),
                    selectedStellarHost = null,
                    selectedPlanet = Planet(
                        id = "earth",
                        name = "Earth",
                        stellarHostId = "sol",
                        status = PlanetStatus.CONFIRMED,
                        orbitalPeriod = 365.2,
                        orbitAxis = 1.000,
                        radius = 1.0,
                        mass = 1.0,
                        density = 5.514,
                        eccentricity = 0.017,
                        insolationFlux = 1.000,
                        equilibriumTemperature = 255.0,
                        occultationDepth = 0.000084,
                        inclination = 0.0,
                        obliquity = 23.4,
                    ),
                    search = "",
                    sortStellarHostProperty = StellarHostProperty.entries.random(),
                    sortPlanetProperty = PlanetProperty.entries.random(),
                    sortAscending = true,
                    visibleStellarHostProperties = emptySet(),
                    visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
                    searchableStellarHostProperties = emptySet(),
                    searchablePlanetProperties = emptySet()
                )
            )
        )
    }
}

@Preview
@Composable
private fun StellarExplorerSearchPlanet() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__planet_list",
                value = "Planets"
            ),
        )
    )
    AppTheme {
        StellarExplorerScreen(
            store = getStore(
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.LIST_PLANETS,
                    listIndex = LazyListIndex(),
                    stellarHosts = emptyList(),
                    planets = emptyList(),
                    filteredStellarHosts = emptyList(),
                    filteredPlanets = emptyList(),
                    selectedStellarHost = null,
                    selectedPlanet = null,
                    search = "Kepler",
                    sortStellarHostProperty = StellarHostProperty.entries.random(),
                    sortPlanetProperty = PlanetProperty.entries.random(),
                    sortAscending = true,
                    visibleStellarHostProperties = emptySet(),
                    visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
                    searchableStellarHostProperties = emptySet(),
                    searchablePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
                )
            )
        )
    }
}
