package com.hybris.tlv.ui.screen.stellarexplorer

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.command.Command
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTranslationState
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.list.PlanetList
import com.hybris.tlv.ui.theme.component.list.StellarHostList
import com.hybris.tlv.ui.theme.component.topbar.ControlPanel
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.ui.theme.modifier.clearFocus
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.spectralTypeToImage
import com.hybris.tlv.usecase.space.toImage
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun StellarExplorerScreen(store: Store<StellarExplorerState, StellarExplorerAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val currentContent = storeState.currentContent
    val listState = storeState.listState
    val visibleStellarHostProperties = storeState.visibleStellarHostProperties
    val visiblePlanetProperties = storeState.visiblePlanetProperties

    val state = LocalTranslationState.current
    val stellarHostProperties = remember(key1 = state) { StellarHostProperty.entries.associateWith { TranslationCache.get(key = it.displayName) } }
    val planetProperties = remember(key1 = state) { PlanetProperty.entries.associateWith { TranslationCache.get(key = it.displayName) } }

    val hostListTranslation = getTranslation(key = "stellar_explorer_screen__host_list")
    val planetListTranslation = getTranslation(key = "stellar_explorer_screen__planet_list")

    Screen(
        modifier = Modifier.clearFocus(),
        loading = storeState.loading,
        onBackClick = { store.back() },
        onHelpClick = { store.navigate(screen = Screen.Help) },
        onMusicClick = { store.command(command = Command.ToggleAudio) },
        onFeedbackClick = { store.navigate(screen = Screen.Feedback()) },
        topBar = {
            // Control panel definitions according to selected view
            val isHostView = currentContent in listOf(Content.LIST_HOSTS, Content.DETAIL_HOSTS)
            val viewName = if (isHostView) hostListTranslation else planetListTranslation
            val viewIcon = if (isHostView) Icons.Default.Flare else Icons.Default.Public
            val count = if (isHostView) storeState.stellarHosts.size else storeState.planets.size
            val properties: List<String>
            val selectedProperty: String
            val onSortChange: (String) -> Unit
            val visibleProperties: List<String>
            val onVisibilityChange: (String) -> Unit
            val selectedProperties: List<String>
            val onFiltersChange: (String) -> Unit
            when (isHostView) {
                true -> {
                    properties = stellarHostProperties.values.toList()
                    selectedProperty = stellarHostProperties[storeState.sortStellarHostProperty].orEmpty()
                    onSortChange = { property -> stellarHostProperties.findKey(value = property)?.let { store.send(action = StellarExplorerAction.SortStellarHosts(sort = it)) } }
                    visibleProperties = storeState.visibleStellarHostProperties.mapNotNull { stellarHostProperties[it] }
                    onVisibilityChange = { property -> stellarHostProperties.findKey(value = property)?.let { store.send(action = StellarExplorerAction.ChangeStellarHostsVisibility(property = it)) } }
                    selectedProperties = storeState.searchableStellarHostProperties.mapNotNull { stellarHostProperties[it] }
                    onFiltersChange = { property -> stellarHostProperties.findKey(value = property)?.let { store.send(action = StellarExplorerAction.ChangeStellarHostsSearchable(property = it)) } }
                }

                false -> {
                    properties = planetProperties.values.toList()
                    selectedProperty = planetProperties[storeState.sortPlanetProperty].orEmpty()
                    onSortChange = { property -> planetProperties.findKey(value = property)?.let { store.send(action = StellarExplorerAction.SortPlanets(sort = it)) } }
                    visibleProperties = storeState.visiblePlanetProperties.mapNotNull { planetProperties[it] }
                    onVisibilityChange = { property -> planetProperties.findKey(value = property)?.let { store.send(action = StellarExplorerAction.ChangePlanetVisibility(property = it)) } }
                    selectedProperties = storeState.searchablePlanetProperties.mapNotNull { planetProperties[it] }
                    onFiltersChange = { property -> planetProperties.findKey(value = property)?.let { store.send(action = StellarExplorerAction.ChangePlanetSearchable(property = it)) } }
                }
            }
            ControlPanel(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp
                ),
                enabled = currentContent in listOf(Content.LIST_HOSTS, Content.LIST_PLANETS),
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
        when (currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_PLANETS -> {
                val planet = storeState.selectedPlanet
                StellarHostList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 16.dp),
                    listState = listState,
                    showPlanet = currentContent == Content.DETAIL_PLANETS && planet != null,
                    planetName = visiblePlanetProperties.ifContains(element = PlanetProperty.NAME, value = planet?.name),
                    planetStatus = visiblePlanetProperties.ifContains(element = PlanetProperty.STATUS, value = planet?.status?.displayName),
                    planetHabitability = visiblePlanetProperties.ifContains(element = PlanetProperty.HABITABILITY, value = planet?.score?.habitabilityScore),
                    planetConfidence = visiblePlanetProperties.ifContains(element = PlanetProperty.CONFIDENCE, value = planet?.score?.confidenceScore),
                    planetOrbitalPeriod = visiblePlanetProperties.ifContains(element = PlanetProperty.ORBITAL_PERIOD, value = planet?.orbitalPeriod),
                    planetOrbitAxis = visiblePlanetProperties.ifContains(element = PlanetProperty.ORBIT_AXIS, value = planet?.orbitAxis),
                    planetRadius = visiblePlanetProperties.ifContains(element = PlanetProperty.RADIUS, value = planet?.radius),
                    planetMass = visiblePlanetProperties.ifContains(element = PlanetProperty.MASS, value = planet?.mass),
                    planetDensity = visiblePlanetProperties.ifContains(element = PlanetProperty.DENSITY, value = planet?.density),
                    planetEccentricity = visiblePlanetProperties.ifContains(element = PlanetProperty.ECCENTRICITY, value = planet?.eccentricity),
                    planetInsolationFlux = visiblePlanetProperties.ifContains(element = PlanetProperty.INSOLATION_FLUX, value = planet?.insolationFlux),
                    planetEquilibriumTemperature = visiblePlanetProperties.ifContains(element = PlanetProperty.TEMPERATURE, value = planet?.equilibriumTemperature),
                    planetOccultationDepth = visiblePlanetProperties.ifContains(element = PlanetProperty.OCCULTATION_DEPTH, value = planet?.occultationDepth),
                    planetInclination = visiblePlanetProperties.ifContains(element = PlanetProperty.INCLINATION, value = planet?.inclination),
                    planetObliquity = visiblePlanetProperties.ifContains(element = PlanetProperty.OBLIQUITY, value = planet?.obliquity),
                    planetType = visiblePlanetProperties.ifContains(element = PlanetProperty.TYPE, value = planet?.score?.planetType?.displayName),
                    planetImage = visiblePlanetProperties.ifContains(element = PlanetProperty.TYPE, value = planet?.score?.planetType.toImage()),
                    planetRocheScore = visiblePlanetProperties.ifContains(element = PlanetProperty.ROCHE_SCORE, value = planet?.score?.rocheScore),
                    planetHabitableZoneKopparapuScore = visiblePlanetProperties.ifContains(element = PlanetProperty.HABITABLE_ZONE_KOPPARAPU_SCORE, value = planet?.score?.habitableZoneKopparapuScore),
                    planetHabitableZoneKastingScore = visiblePlanetProperties.ifContains(element = PlanetProperty.HABITABLE_ZONE_KASTING_SCORE, value = planet?.score?.habitableZoneKastingScore),
                    planetRadiusScore = visiblePlanetProperties.ifContains(element = PlanetProperty.RADIUS_SCORE, value = planet?.score?.planetRadiusScore),
                    planetMassScore = visiblePlanetProperties.ifContains(element = PlanetProperty.MASS_SCORE, value = planet?.score?.planetMassScore),
                    planetTelluricityScore = visiblePlanetProperties.ifContains(element = PlanetProperty.TELLURICITY_SCORE, value = planet?.score?.planetTelluricityScore),
                    planetEccentricityScore = visiblePlanetProperties.ifContains(element = PlanetProperty.ECCENTRICITY_SCORE, value = planet?.score?.planetEccentricityScore),
                    planetTemperatureScore = visiblePlanetProperties.ifContains(element = PlanetProperty.TEMPERATURE_SCORE, value = planet?.score?.planetTemperatureScore),
                    planetObliquityScore = visiblePlanetProperties.ifContains(element = PlanetProperty.OBLIQUITY_SCORE, value = planet?.score?.planetObliquityScore),
                    planetEsiScore = visiblePlanetProperties.ifContains(element = PlanetProperty.ESI_SCORE, value = planet?.score?.planetEsiScore),
                    planetProtectionScore = visiblePlanetProperties.ifContains(element = PlanetProperty.PROTECTION_SCORE, value = planet?.score?.planetProtectionScore),
                    planetTidalLockingScore = visiblePlanetProperties.ifContains(element = PlanetProperty.TIDAL_LOCKING_SCORE, value = planet?.score?.planetTidalLockingScore),
                    stellarHosts = storeState.stellarHosts,
                    stellarHostId = { it.id },
                    stellarHostName = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.NAME, value = it.name) },
                    stellarHostSystemName = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.SYSTEM_NAME, value = it.systemName) },
                    stellarHostPlanetCount = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.PLANET_COUNT, value = it.planets.size) },
                    stellarHostSpectralType = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.SPECTRAL_TYPE, value = it.spectralType) },
                    stellarHostSpectralImage = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.SPECTRAL_TYPE, value = it.spectralType.spectralTypeToImage()) },
                    stellarHostEffectiveTemperature = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.TEMPERATURE, value = it.effectiveTemperature) },
                    stellarHostRadius = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.RADIUS, value = it.radius) },
                    stellarHostMass = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.MASS, value = it.mass) },
                    stellarHostMetallicity = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.METALLICITY, value = it.metallicity) },
                    stellarHostLuminosity = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.LUMINOSITY, value = it.luminosity) },
                    stellarHostGravity = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.GRAVITY, value = it.gravity) },
                    stellarHostAge = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.AGE, value = it.age) },
                    stellarHostDensity = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.DENSITY, value = it.density) },
                    stellarHostRotationalVelocity = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.ROTATIONAL_VELOCITY, value = it.rotationalVelocity) },
                    stellarHostRotationalPeriod = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.ROTATIONAL_PERIOD, value = it.rotationalPeriod) },
                    stellarHostDistance = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.DISTANCE, value = it.distance) },
                    stellarHostRa = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.RA, value = it.ra) },
                    stellarHostDec = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.DEC, value = it.dec) },
                    stellarHostSpectralTypeScore = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.SPECTRAL_TYPE_SCORE, value = it.score?.stellarSpectralTypeScore) },
                    stellarHostMassScore = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.MASS_SCORE, value = it.score?.stellarMassScore) },
                    stellarHostAgeScore = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.AGE_SCORE, value = it.score?.stellarAgeScore) },
                    stellarHostActivityScore = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.ACTIVITY_SCORE, value = it.score?.stellarActivityScore) },
                    stellarHostRotationalPeriodScore = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.ROTATIONAL_PERIOD_SCORE, value = it.score?.stellarRotationalPeriodScore) },
                    stellarHostGravityScore = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.GRAVITY_SCORE, value = it.score?.stellarGravityScore) },
                    stellarHostMetallicityScore = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.METALLICITY_SCORE, value = it.score?.stellarMetallicityScore) },
                    stellarHostEffectiveTemperatureScore = { visibleStellarHostProperties.ifContains(element = StellarHostProperty.EFFECTIVE_TEMPERATURE_SCORE, value = it.score?.stellarEffectiveTemperatureScore) },
                    onStellarHostClick = {
                        store.send(action = StellarExplorerAction.SaveListState(listState = listState))
                        store.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = it))
                    }
                )
            }

            Content.LIST_PLANETS, Content.DETAIL_HOSTS -> {
                val stellarHost = storeState.selectedStellarHost
                PlanetList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 16.dp),
                    listState = listState,
                    showStellarHost = currentContent == Content.DETAIL_HOSTS && stellarHost != null,
                    stellarHostName = visibleStellarHostProperties.ifContains(element = StellarHostProperty.NAME, value = stellarHost?.name),
                    stellarHostSystemName = visibleStellarHostProperties.ifContains(element = StellarHostProperty.SYSTEM_NAME, value = stellarHost?.systemName),
                    stellarHostPlanetCount = visibleStellarHostProperties.ifContains(element = StellarHostProperty.PLANET_COUNT, value = stellarHost?.planets?.size),
                    stellarHostSpectralType = visibleStellarHostProperties.ifContains(element = StellarHostProperty.SPECTRAL_TYPE, value = stellarHost?.spectralType),
                    stellarHostSpectralImage = visibleStellarHostProperties.ifContains(element = StellarHostProperty.SPECTRAL_TYPE, value = stellarHost?.spectralType.spectralTypeToImage()),
                    stellarHostEffectiveTemperature = visibleStellarHostProperties.ifContains(element = StellarHostProperty.TEMPERATURE, value = stellarHost?.effectiveTemperature),
                    stellarHostRadius = visibleStellarHostProperties.ifContains(element = StellarHostProperty.RADIUS, value = stellarHost?.radius),
                    stellarHostMass = visibleStellarHostProperties.ifContains(element = StellarHostProperty.MASS, value = stellarHost?.mass),
                    stellarHostMetallicity = visibleStellarHostProperties.ifContains(element = StellarHostProperty.METALLICITY, value = stellarHost?.metallicity),
                    stellarHostLuminosity = visibleStellarHostProperties.ifContains(element = StellarHostProperty.LUMINOSITY, value = stellarHost?.luminosity),
                    stellarHostGravity = visibleStellarHostProperties.ifContains(element = StellarHostProperty.GRAVITY, value = stellarHost?.gravity),
                    stellarHostAge = visibleStellarHostProperties.ifContains(element = StellarHostProperty.AGE, value = stellarHost?.age),
                    stellarHostDensity = visibleStellarHostProperties.ifContains(element = StellarHostProperty.DENSITY, value = stellarHost?.density),
                    stellarHostRotationalVelocity = visibleStellarHostProperties.ifContains(element = StellarHostProperty.ROTATIONAL_VELOCITY, value = stellarHost?.rotationalVelocity),
                    stellarHostRotationalPeriod = visibleStellarHostProperties.ifContains(element = StellarHostProperty.ROTATIONAL_PERIOD, value = stellarHost?.rotationalPeriod),
                    stellarHostDistance = visibleStellarHostProperties.ifContains(element = StellarHostProperty.DISTANCE, value = stellarHost?.distance),
                    stellarHostRa = visibleStellarHostProperties.ifContains(element = StellarHostProperty.RA, value = stellarHost?.ra),
                    stellarHostDec = visibleStellarHostProperties.ifContains(element = StellarHostProperty.DEC, value = stellarHost?.dec),
                    stellarHostSpectralTypeScore = visibleStellarHostProperties.ifContains(element = StellarHostProperty.SPECTRAL_TYPE_SCORE, value = stellarHost?.score?.stellarSpectralTypeScore),
                    stellarHostMassScore = visibleStellarHostProperties.ifContains(element = StellarHostProperty.MASS_SCORE, value = stellarHost?.score?.stellarMassScore),
                    stellarHostAgeScore = visibleStellarHostProperties.ifContains(element = StellarHostProperty.AGE_SCORE, value = stellarHost?.score?.stellarAgeScore),
                    stellarHostActivityScore = visibleStellarHostProperties.ifContains(element = StellarHostProperty.ACTIVITY_SCORE, value = stellarHost?.score?.stellarActivityScore),
                    stellarHostRotationalPeriodScore = visibleStellarHostProperties.ifContains(element = StellarHostProperty.ROTATIONAL_PERIOD_SCORE, value = stellarHost?.score?.stellarRotationalPeriodScore),
                    stellarHostGravityScore = visibleStellarHostProperties.ifContains(element = StellarHostProperty.GRAVITY_SCORE, value = stellarHost?.score?.stellarGravityScore),
                    stellarHostMetallicityScore = visibleStellarHostProperties.ifContains(element = StellarHostProperty.METALLICITY_SCORE, value = stellarHost?.score?.stellarMetallicityScore),
                    stellarHostEffectiveTemperatureScore = visibleStellarHostProperties.ifContains(element = StellarHostProperty.EFFECTIVE_TEMPERATURE_SCORE, value = stellarHost?.score?.stellarEffectiveTemperatureScore),
                    planets = storeState.planets,
                    planetId = { it.id },
                    planetName = { visiblePlanetProperties.ifContains(element = PlanetProperty.NAME, value = it.name) },
                    planetStatus = { visiblePlanetProperties.ifContains(element = PlanetProperty.STATUS, value = it.status.displayName) },
                    planetHabitability = { visiblePlanetProperties.ifContains(element = PlanetProperty.HABITABILITY, value = it.score?.habitabilityScore) },
                    planetConfidence = { visiblePlanetProperties.ifContains(element = PlanetProperty.CONFIDENCE, value = it.score?.confidenceScore) },
                    planetOrbitalPeriod = { visiblePlanetProperties.ifContains(element = PlanetProperty.ORBITAL_PERIOD, value = it.orbitalPeriod) },
                    planetOrbitAxis = { visiblePlanetProperties.ifContains(element = PlanetProperty.ORBIT_AXIS, value = it.orbitAxis) },
                    planetRadius = { visiblePlanetProperties.ifContains(element = PlanetProperty.RADIUS, value = it.radius) },
                    planetMass = { visiblePlanetProperties.ifContains(element = PlanetProperty.MASS, value = it.mass) },
                    planetDensity = { visiblePlanetProperties.ifContains(element = PlanetProperty.DENSITY, value = it.density) },
                    planetEccentricity = { visiblePlanetProperties.ifContains(element = PlanetProperty.ECCENTRICITY, value = it.eccentricity) },
                    planetInsolationFlux = { visiblePlanetProperties.ifContains(element = PlanetProperty.INSOLATION_FLUX, value = it.insolationFlux) },
                    planetEquilibriumTemperature = { visiblePlanetProperties.ifContains(element = PlanetProperty.TEMPERATURE, value = it.equilibriumTemperature) },
                    planetOccultationDepth = { visiblePlanetProperties.ifContains(element = PlanetProperty.OCCULTATION_DEPTH, value = it.occultationDepth) },
                    planetInclination = { visiblePlanetProperties.ifContains(element = PlanetProperty.INCLINATION, value = it.inclination) },
                    planetObliquity = { visiblePlanetProperties.ifContains(element = PlanetProperty.OBLIQUITY, value = it.obliquity) },
                    planetType = { visiblePlanetProperties.ifContains(element = PlanetProperty.TYPE, value = it.score?.planetType?.displayName) },
                    planetImage = { visiblePlanetProperties.ifContains(element = PlanetProperty.TYPE, value = it.score?.planetType.toImage()) },
                    planetRocheScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.ROCHE_SCORE, value = it.score?.rocheScore) },
                    planetHabitableZoneKopparapuScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.HABITABLE_ZONE_KOPPARAPU_SCORE, value = it.score?.habitableZoneKopparapuScore) },
                    planetHabitableZoneKastingScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.HABITABLE_ZONE_KASTING_SCORE, value = it.score?.habitableZoneKastingScore) },
                    planetRadiusScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.RADIUS_SCORE, value = it.score?.planetRadiusScore) },
                    planetMassScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.MASS_SCORE, value = it.score?.planetMassScore) },
                    planetTelluricityScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.TELLURICITY_SCORE, value = it.score?.planetTelluricityScore) },
                    planetEccentricityScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.ECCENTRICITY_SCORE, value = it.score?.planetEccentricityScore) },
                    planetTemperatureScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.TEMPERATURE_SCORE, value = it.score?.planetTemperatureScore) },
                    planetObliquityScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.OBLIQUITY_SCORE, value = it.score?.planetObliquityScore) },
                    planetEsiScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.ESI_SCORE, value = it.score?.planetEsiScore) },
                    planetProtectionScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.PROTECTION_SCORE, value = it.score?.planetProtectionScore) },
                    planetTidalLockingScore = { visiblePlanetProperties.ifContains(element = PlanetProperty.TIDAL_LOCKING_SCORE, value = it.score?.planetTidalLockingScore) },
                    onPlanetClick = {
                        store.send(action = StellarExplorerAction.SaveListState(listState = listState))
                        store.send(action = StellarExplorerAction.OpenPlanet(planet = it))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun StellarExplorerScreenLoadingPreview() = AppTheme {
    TranslationCache.set(
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
                listState = LazyListState(),
                stellarHosts = emptyList(),
                planets = emptyList(),
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
private fun StellarExplorerScreenHostListPreview() = AppTheme {
    TranslationCache.set(
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
                listState = LazyListState(),
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
                    ),
                ),
                planets = emptyList(),
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
private fun StellarExplorerScreenHostDetailPreview() = AppTheme {
    TranslationCache.set(
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
                listState = LazyListState(),
                stellarHosts = emptyList(),
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
private fun StellarExplorerScreenSearchHostsPreview() = AppTheme {
    TranslationCache.set(
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
                listState = LazyListState(),
                stellarHosts = emptyList(),
                planets = emptyList(),
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
private fun StellarExplorerScreenPlanetListPreview() = AppTheme {
    TranslationCache.set(
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
                listState = LazyListState(),
                stellarHosts = emptyList(),
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
private fun StellarExplorerScreenPlanetDetailPreview() = AppTheme {
    TranslationCache.set(
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
                listState = LazyListState(),
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
                ),
                planets = emptyList(),
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
private fun StellarExplorerScreenSearchPlanetPreview() = AppTheme {
    TranslationCache.set(
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
                listState = LazyListState(),
                stellarHosts = emptyList(),
                planets = emptyList(),
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
