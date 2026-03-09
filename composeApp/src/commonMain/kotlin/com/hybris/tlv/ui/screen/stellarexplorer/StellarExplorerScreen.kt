package com.hybris.tlv.ui.screen.stellarexplorer

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.usecase.space.model.Planet
import com.hybris.tlv.domain.usecase.space.model.PlanetStatus
import com.hybris.tlv.domain.usecase.space.model.StellarHost
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.list.ExoplanetList
import com.hybris.tlv.ui.theme.component.topbar.ControlPanel
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun StellarExplorerScreen(store: Store<StellarExplorerState, StellarExplorerAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val currentContent = storeState.currentContent

    val hostListTranslation = getTranslation(key = "stellar_explorer_screen__host_list")
    val planetListTranslation = getTranslation(key = "stellar_explorer_screen__planet_list")

    val hostListState = rememberLazyListState()
    val planetListState = rememberLazyListState()
    val hostDetailState = rememberLazyListState()
    val planetDetailState = rememberLazyListState()

    val listState = when (currentContent) {
        Content.LIST_HOSTS -> hostListState
        Content.LIST_PLANETS -> planetListState
        Content.DETAIL_HOSTS -> hostDetailState
        Content.DETAIL_PLANETS -> planetDetailState
    }
    Screen(
        loading = storeState.loading,
        onBackClick = { store.send(action = StellarExplorerAction.Back) },
        topBar = {
            val hostView = remember(key1 = currentContent) {
                when (currentContent) {
                    Content.LIST_HOSTS, Content.DETAIL_HOSTS -> true
                    Content.LIST_PLANETS, Content.DETAIL_PLANETS -> false
                }
            }
            val stellarProperty = remember(key1 = currentContent) {
                when (currentContent) {
                    Content.LIST_HOSTS, Content.DETAIL_PLANETS -> true
                    Content.LIST_PLANETS, Content.DETAIL_HOSTS -> false
                }
            }
            ControlPanel(
                modifier = Modifier
                    .testTag(tag = "stellar_explorer_control_panel")
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp
                    ),
                search = storeState.search,
                onSearch = { store.send(action = StellarExplorerAction.Search(search = it)) },
                viewName = if (hostView) hostListTranslation else planetListTranslation,
                viewIcon = if (hostView) Icons.Default.Flare else Icons.Default.Public,
                onChangeView = { store.send(action = StellarExplorerAction.ChangeView) },
                count = if (hostView) storeState.exoplanets.stellarHosts.size else storeState.exoplanets.planets.size,
                properties = storeState.properties,
                sortProperty = storeState.sortStellarHostProperty,
                ascending = if (stellarProperty) storeState.sortStellarHostAscending else storeState.sortPlanetAscending,
                onSortChange = { store.send(action = StellarExplorerAction.Sort(sort = it)) },
                onSortDirectionChange = { store.send(action = StellarExplorerAction.ChangeSortDirection) },
                visibleProperties = if (stellarProperty) storeState.visibleStellarHostProperties else storeState.visiblePlanetProperties,
                onVisibilityChange = { store.send(action = StellarExplorerAction.ChangeVisibility(property = it)) },
                searchableProperties = if (stellarProperty) storeState.searchableStellarHostProperties else storeState.searchablePlanetProperties,
                onFiltersChange = { store.send(action = StellarExplorerAction.ChangeSearchable(property = it)) },
            )
        }
    ) {
        ExoplanetList(
            modifier = Modifier
                .testTag(tag = "stellar_explorer_list")
                .fillMaxSize()
                .padding(all = 16.dp),
            listState = listState,
            hostsFirst = currentContent in listOf(Content.LIST_HOSTS, Content.DETAIL_HOSTS),
            stellarHosts = storeState.exoplanets.stellarHosts,
            stellarHostId = Exoplanets.Host::id,
            stellarHostName = { it.name },
            stellarHostSystemName = { it.systemName },
            stellarHostPlanetCount = { it.planetCount },
            stellarHostSpectralType = { it.spectralType },
            stellarHostSpectralImage = { it.image },
            stellarHostEffectiveTemperature = { it.effectiveTemperature },
            stellarHostRadius = { it.radius },
            stellarHostMass = { it.mass },
            stellarHostMetallicity = { it.metallicity },
            stellarHostLuminosity = { it.luminosity },
            stellarHostGravity = { it.gravity },
            stellarHostAge = { it.age },
            stellarHostDensity = { it.density },
            stellarHostRotationalVelocity = { it.rotationalVelocity },
            stellarHostRotationalPeriod = { it.rotationalPeriod },
            stellarHostDistance = { it.distance },
            stellarHostRa = { it.ra },
            stellarHostDec = { it.dec },
            stellarHostSpectralTypeScore = { it.spectralTypeScore },
            stellarHostMassScore = { it.massScore },
            stellarHostAgeScore = { it.ageScore },
            stellarHostActivityScore = { it.activityScore },
            stellarHostRotationalPeriodScore = { it.rotationalPeriodScore },
            stellarHostGravityScore = { it.gravityScore },
            stellarHostMetallicityScore = { it.metallicityScore },
            stellarHostEffectiveTemperatureScore = { it.effectiveTemperatureScore },
            onStellarHostClick = { if (currentContent == Content.LIST_HOSTS) store.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = it)) },
            planets = storeState.exoplanets.planets,
            planetId = Exoplanets.Planet::id,
            planetName = { it.name },
            planetStatus = { it.status },
            planetHabitability = { it.habitabilityScore },
            planetConfidence = { it.confidenceScore },
            planetOrbitalPeriod = { it.orbitalPeriod },
            planetOrbitAxis = { it.orbitAxis },
            planetRadius = { it.radius },
            planetMass = { it.mass },
            planetDensity = { it.density },
            planetEccentricity = { it.eccentricity },
            planetInsolationFlux = { it.insolationFlux },
            planetEquilibriumTemperature = { it.equilibriumTemperature },
            planetOccultationDepth = { it.occultationDepth },
            planetInclination = { it.inclination },
            planetObliquity = { it.obliquity },
            planetType = { it.type },
            planetImage = { it.image },
            planetRocheScore = { it.rocheScore },
            planetHabitableZoneKopparapuScore = { it.habitableZoneKopparapuScore },
            planetHabitableZoneKastingScore = { it.habitableZoneKastingScore },
            planetRadiusScore = { it.radiusScore },
            planetMassScore = { it.massScore },
            planetTelluricityScore = { it.telluricityScore },
            planetEccentricityScore = { it.eccentricityScore },
            planetTemperatureScore = { it.temperatureScore },
            planetObliquityScore = { it.obliquityScore },
            planetEsiScore = { it.esiScore },
            planetProtectionScore = { it.protectionScore },
            planetTidalLockingScore = { it.tidalLockingScore },
            onPlanetClick = { if (currentContent == Content.LIST_PLANETS) store.send(action = StellarExplorerAction.OpenPlanet(planet = it)) },
        )
    }
}

@Preview
@Composable
private fun StellarExplorerScreenLoadingPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
    StellarExplorerScreen(
        store = Store(
            initialState = StellarExplorerState(
                loading = true,
                currentContent = Content.LIST_HOSTS,
                exoplanets = Exoplanets(),
                search = "",
                properties = persistentListOf(),
                sortStellarHostProperty = "",
                sortPlanetProperty = "",
                sortStellarHostAscending = true,
                sortPlanetAscending = true,
                visibleStellarHostProperties = persistentListOf(),
                visiblePlanetProperties = persistentListOf(),
                searchableStellarHostProperties = persistentListOf(),
                searchablePlanetProperties = persistentListOf(),
            )
        )
    )
}

@Preview
@Composable
private fun StellarExplorerScreenHostListPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
    StellarExplorerScreen(
        store = Store(
            initialState = StellarExplorerState(
                loading = false,
                currentContent = Content.LIST_HOSTS,
                exoplanets = Exoplanets(
                    stellarHosts = listOf(
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
                        )
                    ).map { it.toExoplanetsHost() }.toPersistentList(),
                    planets = persistentListOf()
                ),
                search = "Something awesome",
                properties = StellarHostProperty.entries.map { it.name to it.displayName }.toPersistentList(),
                sortStellarHostProperty = StellarHostProperty.entries.random().name,
                sortStellarHostAscending = true,
                visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).map { it.name }.toPersistentList(),
                searchableStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).map { it.name }.toPersistentList(),
            )
        )
    )
}

@Preview
@Composable
private fun StellarExplorerScreenHostDetailPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
    StellarExplorerScreen(
        store = Store(
            initialState = StellarExplorerState(
                loading = false,
                currentContent = Content.DETAIL_HOSTS,
                exoplanets = Exoplanets(
                    stellarHosts = listOf(
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
                        )
                    ).map { it.toExoplanetsHost() }.toPersistentList(),
                    planets = listOf(
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
                    ).map { it.toExoplanetsPlanet() }.toPersistentList(),
                ),
                search = "",
                properties = PlanetProperty.entries.map { it.name to it.displayName }.toPersistentList(),
                sortPlanetProperty = PlanetProperty.entries.random().name,
                sortPlanetAscending = true,
                visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).map { it.name }.toPersistentList(),
                searchablePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).map { it.name }.toPersistentList(),
            )
        )
    )
}

@Preview
@Composable
private fun StellarExplorerScreenPlanetListPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__planet_list",
                value = "Planets"
            ),
        )
    )
    StellarExplorerScreen(
        store = Store(
            initialState = StellarExplorerState(
                loading = false,
                currentContent = Content.LIST_PLANETS,
                exoplanets = Exoplanets(
                    stellarHosts = persistentListOf(),
                    planets = listOf(
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
                    ).map { it.toExoplanetsPlanet() }.toPersistentList(),
                ),
                search = "Planets",
                properties = PlanetProperty.entries.map { it.name to it.displayName }.toPersistentList(),
                sortPlanetProperty = PlanetProperty.entries.random().name,
                sortPlanetAscending = true,
                visiblePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).map { it.name }.toPersistentList(),
                searchablePlanetProperties = PlanetProperty.entries.shuffled().take(n = 5).map { it.name }.toPersistentList(),
            )
        )
    )
}

@Preview
@Composable
private fun StellarExplorerScreenPlanetDetailPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__planet_list",
                value = "Planets"
            ),
        )
    )
    StellarExplorerScreen(
        store = Store(
            initialState = StellarExplorerState(
                loading = false,
                currentContent = Content.DETAIL_PLANETS,
                exoplanets = Exoplanets(
                    stellarHosts = listOf(
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
                    ).map { it.toExoplanetsHost() }.toPersistentList(),
                    planets = listOf(
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
                    ).map { it.toExoplanetsPlanet() }.toPersistentList()
                ),
                search = "",
                properties = StellarHostProperty.entries.map { it.name to it.displayName }.toPersistentList(),
                sortStellarHostProperty = StellarHostProperty.entries.random().name,
                sortStellarHostAscending = true,
                visibleStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).map { it.name }.toPersistentList(),
                searchableStellarHostProperties = StellarHostProperty.entries.shuffled().take(n = 5).map { it.name }.toPersistentList(),
            )
        )
    )
}
