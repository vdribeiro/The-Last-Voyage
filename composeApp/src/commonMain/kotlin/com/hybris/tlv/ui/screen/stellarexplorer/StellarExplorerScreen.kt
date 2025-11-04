package com.hybris.tlv.ui.screen.stellarexplorer

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.store.getStore
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.card.PlanetCard
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.divider.Divider
import com.hybris.tlv.ui.theme.component.list.LazyColumnWithScrollBar
import com.hybris.tlv.ui.theme.component.topbar.ControlPanel
import com.hybris.tlv.usecase.space.formula.spectralTypeToImage
import com.hybris.tlv.usecase.space.formula.toImage
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun StellarExplorerScreen(store: Store<StellarExplorerState, StellarExplorerAction>) {
    val storeState by store.stateFlow.collectAsState()
    val stellarHostProperties = remember { StellarHostProperty.entries.associateWith { getTranslation(key = it.displayName) } }
    val planetProperties = remember { PlanetProperty.entries.associateWith { getTranslation(key = it.displayName) } }

    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val hostListTranslation = remember(key1 = translationVersion) { getTranslation(key = "stellar_explorer_screen__host_list") }
    val planetListTranslation = remember(key1 = translationVersion) { getTranslation(key = "stellar_explorer_screen__planet_list") }

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
        loading = storeState.loading,
        onBackClick = { store.back() },
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        topBar = {
            ControlPanel(
                modifier = Modifier
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

@Composable
private fun StellarHostContent(store: Store<StellarExplorerState, StellarExplorerAction>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent
    val planet = storeState.selectedPlanet
    val visibleStellarHostProperties = storeState.visibleStellarHostProperties
    val visiblePlanetProperties = storeState.visiblePlanetProperties
    val listState = storeState.listIndex.getState()
    LazyColumnWithScrollBar(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        state = listState
    ) {
        if (currentContent == Content.DETAIL_PLANETS && planet != null) {
            item(key = planet.id) {
                PlanetCard(
                    name = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.NAME,
                        value = planet.name
                    ),
                    status = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.STATUS,
                        value = planet.status.displayName
                    ),
                    habitability = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.HABITABILITY,
                        value = planet.score?.habitabilityScore
                    ),
                    confidence = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.CONFIDENCE,
                        value = planet.score?.confidenceScore
                    ),
                    orbitalPeriod = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ORBITAL_PERIOD,
                        value = planet.orbitalPeriod
                    ),
                    orbitAxis = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ORBIT_AXIS,
                        value = planet.orbitAxis
                    ),
                    radius = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.RADIUS,
                        value = planet.radius
                    ),
                    mass = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.MASS,
                        value = planet.mass
                    ),
                    density = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.DENSITY,
                        value = planet.density
                    ),
                    eccentricity = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ECCENTRICITY,
                        value = planet.eccentricity
                    ),
                    insolationFlux = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.INSOLATION_FLUX,
                        value = planet.insolationFlux
                    ),
                    equilibriumTemperature = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TEMPERATURE,
                        value = planet.equilibriumTemperature
                    ),
                    occultationDepth = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.OCCULTATION_DEPTH,
                        value = planet.occultationDepth
                    ),
                    inclination = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.INCLINATION,
                        value = planet.inclination
                    ),
                    obliquity = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.OBLIQUITY,
                        value = planet.obliquity
                    ),
                    type = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TYPE,
                        value = planet.score?.planetType?.displayName
                    ),
                    image = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TYPE,
                        value = planet.score?.planetType.toImage()
                    ),
                    rocheScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ROCHE_SCORE,
                        value = planet.score?.rocheScore
                    ),
                    habitableZoneKopparapuScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.HABITABLE_ZONE_KOPPARAPU_SCORE,
                        value = planet.score?.habitableZoneKopparapuScore
                    ),
                    habitableZoneKastingScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.HABITABLE_ZONE_KASTING_SCORE,
                        value = planet.score?.habitableZoneKastingScore
                    ),
                    radiusScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.RADIUS_SCORE,
                        value = planet.score?.planetRadiusScore
                    ),
                    massScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.MASS_SCORE,
                        value = planet.score?.planetMassScore
                    ),
                    telluricityScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TELLURICITY_SCORE,
                        value = planet.score?.planetTelluricityScore
                    ),
                    eccentricityScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ECCENTRICITY_SCORE,
                        value = planet.score?.planetEccentricityScore
                    ),
                    temperatureScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TEMPERATURE_SCORE,
                        value = planet.score?.planetTemperatureScore
                    ),
                    obliquityScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.OBLIQUITY_SCORE,
                        value = planet.score?.planetObliquityScore
                    ),
                    esiScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ESI_SCORE,
                        value = planet.score?.planetEsiScore
                    ),
                    protectionScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.PROTECTION_SCORE,
                        value = planet.score?.planetProtectionScore
                    ),
                    tidalLockingScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TIDAL_LOCKING_SCORE,
                        value = planet.score?.planetTidalLockingScore
                    )
                )
            }
            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
        }
        items(items = storeState.filteredStellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                modifier = Modifier
                    .clickable {
                        store.send(
                            action = StellarExplorerAction.SaveIndex(
                                index = LazyListIndex(
                                    index = listState.firstVisibleItemIndex,
                                    scrollOffset = listState.firstVisibleItemScrollOffset
                                )
                            )
                        )
                        store.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = stellarHost))
                    },
                name = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.NAME,
                    value = stellarHost.name
                ),
                systemName = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.SYSTEM_NAME,
                    value = stellarHost.systemName
                ),
                planetCount = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.PLANET_COUNT,
                    value = stellarHost.planets.size
                ),
                spectralType = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.SPECTRAL_TYPE,
                    value = stellarHost.spectralType
                ),
                spectralImage = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.SPECTRAL_TYPE,
                    value = stellarHost.spectralType.spectralTypeToImage()
                ),
                effectiveTemperature = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.TEMPERATURE,
                    value = stellarHost.effectiveTemperature
                ),
                radius = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.RADIUS,
                    value = stellarHost.radius
                ),
                mass = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.MASS,
                    value = stellarHost.mass
                ),
                metallicity = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.METALLICITY,
                    value = stellarHost.metallicity
                ),
                luminosity = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.LUMINOSITY,
                    value = stellarHost.luminosity
                ),
                gravity = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.GRAVITY,
                    value = stellarHost.gravity
                ),
                age = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.AGE,
                    value = stellarHost.age
                ),
                density = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.DENSITY,
                    value = stellarHost.density
                ),
                rotationalVelocity = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.ROTATIONAL_VELOCITY,
                    value = stellarHost.rotationalVelocity
                ),
                rotationalPeriod = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.ROTATIONAL_PERIOD,
                    value = stellarHost.rotationalPeriod
                ),
                distance = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.DISTANCE,
                    value = stellarHost.distance
                ),
                ra = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.RA,
                    value = stellarHost.ra
                ),
                dec = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.DEC,
                    value = stellarHost.dec
                ),
                spectralTypeScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.SPECTRAL_TYPE_SCORE,
                    value = stellarHost.score?.stellarSpectralTypeScore
                ),
                massScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.MASS_SCORE,
                    value = stellarHost.score?.stellarMassScore
                ),
                ageScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.AGE_SCORE,
                    value = stellarHost.score?.stellarAgeScore
                ),
                activityScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.ACTIVITY_SCORE,
                    value = stellarHost.score?.stellarActivityScore
                ),
                rotationalPeriodScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.ROTATIONAL_PERIOD_SCORE,
                    value = stellarHost.score?.stellarRotationalPeriodScore
                ),
                gravityScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.GRAVITY_SCORE,
                    value = stellarHost.score?.stellarGravityScore
                ),
                metallicityScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.METALLICITY_SCORE,
                    value = stellarHost.score?.stellarMetallicityScore
                ),
                effectiveTemperatureScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.EFFECTIVE_TEMPERATURE_SCORE,
                    value = stellarHost.score?.stellarEffectiveTemperatureScore
                )
            )
        }
    }
}

@Composable
private fun PlanetContent(store: Store<StellarExplorerState, StellarExplorerAction>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent
    val stellarHost = storeState.selectedStellarHost
    val visibleStellarHostProperties = storeState.visibleStellarHostProperties
    val visiblePlanetProperties = storeState.visiblePlanetProperties
    val listState = storeState.listIndex.getState()
    LazyColumnWithScrollBar(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        state = listState
    ) {
        if (currentContent == Content.DETAIL_HOSTS && stellarHost != null) {
            item(key = stellarHost.id) {
                StellarHostCard(
                    name = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.NAME,
                        value = stellarHost.name
                    ),
                    systemName = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.SYSTEM_NAME,
                        value = stellarHost.systemName
                    ),
                    planetCount = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.PLANET_COUNT,
                        value = stellarHost.planets.size
                    ),
                    spectralType = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.SPECTRAL_TYPE,
                        value = stellarHost.spectralType
                    ),
                    spectralImage = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.SPECTRAL_TYPE,
                        value = stellarHost.spectralType.spectralTypeToImage()
                    ),
                    effectiveTemperature = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.TEMPERATURE,
                        value = stellarHost.effectiveTemperature
                    ),
                    radius = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.RADIUS,
                        value = stellarHost.radius
                    ),
                    mass = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.MASS,
                        value = stellarHost.mass
                    ),
                    metallicity = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.METALLICITY,
                        value = stellarHost.metallicity
                    ),
                    luminosity = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.LUMINOSITY,
                        value = stellarHost.luminosity
                    ),
                    gravity = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.GRAVITY,
                        value = stellarHost.gravity
                    ),
                    age = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.AGE,
                        value = stellarHost.age
                    ),
                    density = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.DENSITY,
                        value = stellarHost.density
                    ),
                    rotationalVelocity = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.ROTATIONAL_VELOCITY,
                        value = stellarHost.rotationalVelocity
                    ),
                    rotationalPeriod = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.ROTATIONAL_PERIOD,
                        value = stellarHost.rotationalPeriod
                    ),
                    distance = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.DISTANCE,
                        value = stellarHost.distance
                    ),
                    ra = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.RA,
                        value = stellarHost.ra
                    ),
                    dec = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.DEC,
                        value = stellarHost.dec
                    ),
                    spectralTypeScore = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.SPECTRAL_TYPE_SCORE,
                        value = stellarHost.score?.stellarSpectralTypeScore
                    ),
                    massScore = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.MASS_SCORE,
                        value = stellarHost.score?.stellarMassScore
                    ),
                    ageScore = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.AGE_SCORE,
                        value = stellarHost.score?.stellarAgeScore
                    ),
                    activityScore = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.ACTIVITY_SCORE,
                        value = stellarHost.score?.stellarActivityScore
                    ),
                    rotationalPeriodScore = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.ROTATIONAL_PERIOD_SCORE,
                        value = stellarHost.score?.stellarRotationalPeriodScore
                    ),
                    gravityScore = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.GRAVITY_SCORE,
                        value = stellarHost.score?.stellarGravityScore
                    ),
                    metallicityScore = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.METALLICITY_SCORE,
                        value = stellarHost.score?.stellarMetallicityScore
                    ),
                    effectiveTemperatureScore = visibleStellarHostProperties.ifContains(
                        element = StellarHostProperty.EFFECTIVE_TEMPERATURE_SCORE,
                        value = stellarHost.score?.stellarEffectiveTemperatureScore
                    )
                )
            }
            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
        }
        items(items = storeState.filteredPlanets, key = { it.id }) { planet ->
            PlanetCard(
                modifier = Modifier
                    .clickable {
                        store.send(
                            action = StellarExplorerAction.SaveIndex(
                                index = LazyListIndex(
                                    index = listState.firstVisibleItemIndex,
                                    scrollOffset = listState.firstVisibleItemScrollOffset
                                )
                            )
                        )
                        store.send(action = StellarExplorerAction.OpenPlanet(planet = planet))
                    },
                name = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.NAME,
                    value = planet.name
                ),
                status = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.STATUS,
                    value = planet.status.displayName
                ),
                habitability = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.HABITABILITY,
                    value = planet.score?.habitabilityScore
                ),
                confidence = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.CONFIDENCE,
                    value = planet.score?.confidenceScore
                ),
                orbitalPeriod = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.ORBITAL_PERIOD,
                    value = planet.orbitalPeriod
                ),
                orbitAxis = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.ORBIT_AXIS,
                    value = planet.orbitAxis
                ),
                radius = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.RADIUS,
                    value = planet.radius
                ),
                mass = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.MASS,
                    value = planet.mass
                ),
                density = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.DENSITY,
                    value = planet.density
                ),
                eccentricity = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.ECCENTRICITY,
                    value = planet.eccentricity
                ),
                insolationFlux = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.INSOLATION_FLUX,
                    value = planet.insolationFlux
                ),
                equilibriumTemperature = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.TEMPERATURE,
                    value = planet.equilibriumTemperature
                ),
                occultationDepth = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.OCCULTATION_DEPTH,
                    value = planet.occultationDepth
                ),
                inclination = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.INCLINATION,
                    value = planet.inclination
                ),
                obliquity = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.OBLIQUITY,
                    value = planet.obliquity
                ),
                type = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.TYPE,
                    value = planet.score?.planetType?.displayName
                ),
                image = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.TYPE,
                    value = planet.score?.planetType.toImage()
                ),
                rocheScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.ROCHE_SCORE,
                    value = planet.score?.rocheScore
                ),
                habitableZoneKopparapuScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.HABITABLE_ZONE_KOPPARAPU_SCORE,
                    value = planet.score?.habitableZoneKopparapuScore
                ),
                habitableZoneKastingScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.HABITABLE_ZONE_KASTING_SCORE,
                    value = planet.score?.habitableZoneKastingScore
                ),
                radiusScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.RADIUS_SCORE,
                    value = planet.score?.planetRadiusScore
                ),
                massScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.MASS_SCORE,
                    value = planet.score?.planetMassScore
                ),
                telluricityScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.TELLURICITY_SCORE,
                    value = planet.score?.planetTelluricityScore
                ),
                eccentricityScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.ECCENTRICITY_SCORE,
                    value = planet.score?.planetEccentricityScore
                ),
                temperatureScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.TEMPERATURE_SCORE,
                    value = planet.score?.planetTemperatureScore
                ),
                obliquityScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.OBLIQUITY_SCORE,
                    value = planet.score?.planetObliquityScore
                ),
                esiScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.ESI_SCORE,
                    value = planet.score?.planetEsiScore
                ),
                protectionScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.PROTECTION_SCORE,
                    value = planet.score?.planetProtectionScore
                ),
                tidalLockingScore = visiblePlanetProperties.ifContains(
                    element = PlanetProperty.TIDAL_LOCKING_SCORE,
                    value = planet.score?.planetTidalLockingScore
                )
            )
        }
    }
}

@Preview
@Composable
private fun StellarExplorerLoadingPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
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

@Preview
@Composable
private fun StellarExplorerHostListPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
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

@Preview
@Composable
private fun StellarExplorerHostDetailPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
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

@Preview
@Composable
private fun StellarExplorerSearchHostsPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__host_list",
                value = "Stellar Hosts"
            ),
        )
    )
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

@Preview
@Composable
private fun StellarExplorerPlanetListPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__planet_list",
                value = "Planets"
            ),
        )
    )
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

@Preview
@Composable
private fun StellarExplorerPlanetDetailPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__planet_list",
                value = "Planets"
            ),
        )
    )
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

@Preview
@Composable
private fun StellarExplorerSearchPlanetPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "stellar_explorer_screen__planet_list",
                value = "Planets"
            ),
        )
    )
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
