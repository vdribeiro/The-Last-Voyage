package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.getStore
import com.hybris.tlv.ui.screen.stellarexplorer.Content
import com.hybris.tlv.ui.screen.stellarexplorer.LazyListIndex
import com.hybris.tlv.ui.screen.stellarexplorer.PlanetProperty
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.screen.stellarexplorer.StellarHostProperty
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

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
