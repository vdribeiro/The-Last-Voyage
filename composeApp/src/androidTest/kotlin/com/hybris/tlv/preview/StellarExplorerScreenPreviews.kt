package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.planets
import com.hybris.tlv.translations
import com.hybris.tlv.ui.screen.stellarexplorer.Content
import com.hybris.tlv.ui.screen.stellarexplorer.LazyListIndex
import com.hybris.tlv.ui.screen.stellarexplorer.PlanetProperty
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.screen.stellarexplorer.StellarHostProperty
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun StellarExplorerLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        StellarExplorerScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
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
    TranslationCache.set(translations = translations)
    AppTheme {
        StellarExplorerScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.LIST_HOSTS,
                    listIndex = LazyListIndex(),
                    stellarHosts = hostsWithPlanets,
                    planets = emptyList(),
                    filteredStellarHosts = hostsWithPlanets,
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
    TranslationCache.set(translations = translations)
    AppTheme {
        StellarExplorerScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.DETAIL_HOSTS,
                    listIndex = LazyListIndex(),
                    stellarHosts = hostsWithPlanets,
                    planets = emptyList(),
                    filteredStellarHosts = hostsWithPlanets,
                    filteredPlanets = emptyList(),
                    selectedStellarHost = hostsWithPlanets.random(),
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
    TranslationCache.set(translations = translations)
    AppTheme {
        StellarExplorerScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.LIST_HOSTS,
                    listIndex = LazyListIndex(),
                    stellarHosts = hostsWithPlanets,
                    planets = emptyList(),
                    filteredStellarHosts = hostsWithPlanets,
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
    TranslationCache.set(translations = translations)
    AppTheme {
        StellarExplorerScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.LIST_PLANETS,
                    listIndex = LazyListIndex(),
                    stellarHosts = emptyList(),
                    planets = planets,
                    filteredStellarHosts = emptyList(),
                    filteredPlanets = planets,
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
    TranslationCache.set(translations = translations)
    AppTheme {
        StellarExplorerScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.DETAIL_PLANETS,
                    listIndex = LazyListIndex(),
                    stellarHosts = emptyList(),
                    planets = planets,
                    filteredStellarHosts = emptyList(),
                    filteredPlanets = planets,
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
private fun StellarExplorerSearchPlanet() {
    TranslationCache.set(translations = translations)
    AppTheme {
        StellarExplorerScreen(
            store = Store(
                dispatcher = TestDispatchers(),
                navigation = PreviewNavigation(),
                initialState = StellarExplorerState(
                    loading = false,
                    currentContent = Content.LIST_PLANETS,
                    listIndex = LazyListIndex(),
                    stellarHosts = emptyList(),
                    planets = planets,
                    filteredStellarHosts = emptyList(),
                    filteredPlanets = planets,
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
