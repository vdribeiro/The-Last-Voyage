package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.App
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.planets
import com.hybris.tlv.translations
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.stellarexplorer.Content
import com.hybris.tlv.ui.screen.stellarexplorer.LazyListIndex
import com.hybris.tlv.ui.screen.stellarexplorer.PlanetProperty
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.screen.stellarexplorer.StellarHostProperty

@Preview
@Composable
private fun StellarExplorerNull() {
    val navigation = navigation(
        screen = Screen.STELLAR_EXPLORER,
        stateBuilder = StellarExplorerState()
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun StellarExplorerLoading() {
    val navigation = navigation(
        screen = Screen.STELLAR_EXPLORER,
        stateBuilder = StellarExplorerState(
            loading = true,
            currentContent = Content.LIST_HOSTS,
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun StellarExplorerHostList() {
    val navigation = navigation(
        screen = Screen.STELLAR_EXPLORER,
        stateBuilder = StellarExplorerState(
            loading = false,
            currentContent = Content.LIST_HOSTS,
            stellarHosts = hostsWithPlanets,
            planets = planets,
            listIndex = LazyListIndex(),
            filteredStellarHosts = hostsWithPlanets,
            filteredPlanets = planets,
            sortStellarHostProperty = StellarHostProperty.entries.random(),
            sortPlanetProperty = PlanetProperty.entries.random(),
            sortAscending = true,
            visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
            visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun StellarExplorerPlanetList() {
    val navigation = navigation(
        screen = Screen.STELLAR_EXPLORER,
        stateBuilder = StellarExplorerState(
            loading = false,
            currentContent = Content.LIST_PLANETS,
            stellarHosts = hostsWithPlanets,
            planets = planets,
            listIndex = LazyListIndex(),
            filteredStellarHosts = hostsWithPlanets,
            filteredPlanets = planets,
            sortStellarHostProperty = StellarHostProperty.entries.random(),
            sortPlanetProperty = PlanetProperty.entries.random(),
            sortAscending = true,
            visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
            visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun StellarExplorerHostDetail() {
    val navigation = navigation(
        screen = Screen.STELLAR_EXPLORER,
        stateBuilder = StellarExplorerState(
            loading = false,
            currentContent = Content.DETAIL_HOSTS,
            stellarHosts = hostsWithPlanets,
            planets = planets,
            listIndex = LazyListIndex(),
            filteredStellarHosts = hostsWithPlanets,
            filteredPlanets = planets,
            selectedStellarHost = hostsWithPlanets.random(),
            sortStellarHostProperty = StellarHostProperty.entries.random(),
            sortPlanetProperty = PlanetProperty.entries.random(),
            sortAscending = true,
            visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
            visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun StellarExplorerPlanetDetail() {
    val navigation = navigation(
        screen = Screen.STELLAR_EXPLORER,
        stateBuilder = StellarExplorerState(
            loading = false,
            currentContent = Content.DETAIL_PLANETS,
            stellarHosts = hostsWithPlanets,
            planets = planets,
            listIndex = LazyListIndex(),
            filteredStellarHosts = hostsWithPlanets,
            filteredPlanets = planets,
            selectedPlanet = planets.random(),
            sortStellarHostProperty = StellarHostProperty.entries.random(),
            sortPlanetProperty = PlanetProperty.entries.random(),
            sortAscending = true,
            visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
            visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun StellarExplorerSearchHosts() {
    val navigation = navigation(
        screen = Screen.STELLAR_EXPLORER,
        stateBuilder = StellarExplorerState(
            loading = false,
            currentContent = Content.LIST_HOSTS,
            stellarHosts = hostsWithPlanets,
            planets = planets,
            listIndex = LazyListIndex(),
            filteredStellarHosts = hostsWithPlanets,
            filteredPlanets = planets,
            search = "Kepler",
            sortStellarHostProperty = StellarHostProperty.entries.random(),
            sortPlanetProperty = PlanetProperty.entries.random(),
            sortAscending = true,
            visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
            visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
            searchableStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}

@Preview
@Composable
private fun StellarExplorerSearchPlanet() {
    val navigation = navigation(
        screen = Screen.STELLAR_EXPLORER,
        stateBuilder = StellarExplorerState(
            loading = false,
            currentContent = Content.LIST_PLANETS,
            stellarHosts = hostsWithPlanets,
            planets = planets,
            listIndex = LazyListIndex(),
            filteredStellarHosts = hostsWithPlanets,
            filteredPlanets = planets,
            search = "Kepler",
            sortStellarHostProperty = StellarHostProperty.entries.random(),
            sortPlanetProperty = PlanetProperty.entries.random(),
            sortAscending = true,
            visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).toSet(),
            visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
            searchablePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).toSet(),
        )
    )
    setTranslations(translations = translations)
    App(navigation = navigation)
}
