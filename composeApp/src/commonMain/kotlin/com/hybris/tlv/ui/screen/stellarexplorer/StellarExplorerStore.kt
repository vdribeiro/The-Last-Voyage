package com.hybris.tlv.ui.screen.stellarexplorer

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.component.LazyListIndex
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.stellarexplorer.model.PlanetProperty
import com.hybris.tlv.ui.screen.stellarexplorer.model.StellarHostProperty
import com.hybris.tlv.ui.screen.stellarexplorer.model.getPlanetsComparator
import com.hybris.tlv.ui.screen.stellarexplorer.model.getStellarHostComparator
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.formula.Habitability
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import kotlinx.coroutines.Job
import com.hybris.tlv.ui.screen.mainmenu.Content as MainMenuContent

internal sealed interface StellarExplorerAction {
    data class SaveIndex(val index: LazyListIndex): StellarExplorerAction
    data object ChangeView: StellarExplorerAction
    data class Search(val search: String): StellarExplorerAction
    data class OpenStellarHost(val stellarHost: StellarHost): StellarExplorerAction
    data class OpenPlanet(val planet: Planet): StellarExplorerAction
    data class SortStellarHosts(val sort: StellarHostProperty): StellarExplorerAction
    data class SortPlanets(val sort: PlanetProperty): StellarExplorerAction
    data object ChangeSortDirection: StellarExplorerAction
    data class ChangeStellarHostsVisibility(val property: StellarHostProperty): StellarExplorerAction
    data class ChangePlanetVisibility(val property: PlanetProperty): StellarExplorerAction
    data class ChangeStellarHostsSearchable(val property: StellarHostProperty): StellarExplorerAction
    data class ChangePlanetSearchable(val property: PlanetProperty): StellarExplorerAction
}

internal data class StellarExplorerState(
    val loading: Boolean = true,
    val currentContent: Content = Content.LIST_HOSTS,
    val stellarHosts: List<StellarHost> = emptyList(),
    val planets: List<Planet> = emptyList(),
    val listIndex: LazyListIndex = LazyListIndex(),
    val filteredStellarHosts: List<StellarHost> = emptyList(),
    val filteredPlanets: List<Planet> = emptyList(),
    val selectedStellarHost: StellarHost? = null,
    val selectedPlanet: Planet? = null,
    val sortStellarHostProperty: StellarHostProperty = StellarHostProperty.DISTANCE,
    val sortPlanetProperty: PlanetProperty = PlanetProperty.NAME,
    val sortAscending: Boolean = true,
    val visibleStellarHostProperties: List<StellarHostProperty> = listOf(
        StellarHostProperty.NAME,
        StellarHostProperty.SYSTEM_NAME,
        StellarHostProperty.PLANET_COUNT,
        StellarHostProperty.SPECTRAL_TYPE,
        StellarHostProperty.TEMPERATURE,
        StellarHostProperty.RADIUS,
        StellarHostProperty.MASS,
        StellarHostProperty.METALLICITY,
        StellarHostProperty.LUMINOSITY,
        StellarHostProperty.GRAVITY,
        StellarHostProperty.AGE,
        StellarHostProperty.DENSITY,
        StellarHostProperty.ROTATIONAL_VELOCITY,
        StellarHostProperty.ROTATIONAL_PERIOD,
        StellarHostProperty.DISTANCE,
        StellarHostProperty.RA,
        StellarHostProperty.DEC,
    ),
    val visiblePlanetProperties: List<PlanetProperty> = listOf(
        PlanetProperty.NAME,
        PlanetProperty.STATUS,
        PlanetProperty.HABITABILITY,
        PlanetProperty.CONFIDENCE,
        PlanetProperty.TYPE,
        PlanetProperty.ORBITAL_PERIOD,
        PlanetProperty.ORBIT_AXIS,
        PlanetProperty.RADIUS,
        PlanetProperty.MASS,
        PlanetProperty.DENSITY,
        PlanetProperty.ECCENTRICITY,
        PlanetProperty.INSOLATION_FLUX,
        PlanetProperty.TEMPERATURE,
        PlanetProperty.OCCULTATION_DEPTH,
        PlanetProperty.INCLINATION,
        PlanetProperty.OBLIQUITY,
    ),
    val searchableStellarHostProperties: List<StellarHostProperty> = listOf(StellarHostProperty.NAME),
    val searchablePlanetProperties: List<PlanetProperty> = listOf(PlanetProperty.NAME)
)

internal enum class Content {
    LIST_HOSTS,
    DETAIL_HOSTS,
    LIST_PLANETS,
    DETAIL_PLANETS,
}

internal class StellarExplorerStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: StellarExplorerState,
    private val spaceUseCases: SpaceUseCases,
): Store<StellarExplorerAction, StellarExplorerState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launch {
        val stellarHosts = spaceUseCases.getExoplanets().apply {
            forEach { stellarHost ->
                stellarHost.score = Habitability.calculateScores(
                    stellarHost = stellarHost,
                    planet = null,
                    formula = Formula()
                )
                stellarHost.planets.forEach { planet ->
                    planet.score = Habitability.calculateScores(
                        stellarHost = stellarHost,
                        planet = planet,
                        formula = Formula()
                    )
                }
            }
        }.sortedWith(comparator = compareBy<StellarHost, Double?>(comparator = nullsLast()) { it.distance }.thenBy { it.id })
        val planets = stellarHosts.map { it.planets }.flatten()
            .sortedWith(comparator = compareBy(comparator = nullsLast()) { it.name })
        updateState {
            it.copy(
                loading = false,
                stellarHosts = stellarHosts,
                planets = planets,
                filteredStellarHosts = stellarHosts,
                filteredPlanets = planets
            )
        }
    }

    private fun changeView(state: StellarExplorerState): Job = launch {
        when (state.currentContent) {
            Content.LIST_HOSTS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        listIndex = LazyListIndex(),
                        filteredPlanets = state.planets
                    )
                }.join()
                send(action = StellarExplorerAction.SortStellarHosts(sort = state.sortStellarHostProperty))
            }

            Content.LIST_PLANETS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        listIndex = LazyListIndex(),
                        filteredStellarHosts = state.stellarHosts
                    )
                }.join()
                send(action = StellarExplorerAction.SortPlanets(sort = state.sortPlanetProperty))
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun search(state: StellarExplorerState, action: StellarExplorerAction.Search): Job = launch {
        when (state.currentContent) {
            Content.LIST_HOSTS -> {
                val filteredStellarHosts = searchStellarHosts(
                    search = action.search,
                    searchable = state.searchableStellarHostProperties,
                    stellarHosts = state.stellarHosts
                )
                updateState { it.copy(filteredStellarHosts = filteredStellarHosts) }
            }

            Content.LIST_PLANETS -> {
                val filteredPlanets = searchPlanets(
                    search = action.search,
                    searchable = state.searchablePlanetProperties,
                    planets = state.planets
                )
                updateState { it.copy(filteredPlanets = filteredPlanets) }
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun searchStellarHosts(search: String, searchable: List<StellarHostProperty>, stellarHosts: List<StellarHost>): List<StellarHost> =
        if (search.isNotBlank()) {
            val searchLowercase = search.lowercase()
            stellarHosts.filter { stellarHost ->
                with(receiver = stellarHost) {
                    listOfNotNull(
                        searchable.ifContains(element = StellarHostProperty.SYSTEM_NAME, value = systemName),
                        searchable.ifContains(element = StellarHostProperty.NAME, value = name),
                        searchable.ifContains(element = StellarHostProperty.SPECTRAL_TYPE, value = spectralType),
                        searchable.ifContains(element = StellarHostProperty.TEMPERATURE, value = effectiveTemperature?.toString()),
                        searchable.ifContains(element = StellarHostProperty.RADIUS, value = radius?.toString()),
                        searchable.ifContains(element = StellarHostProperty.MASS, value = mass?.toString()),
                        searchable.ifContains(element = StellarHostProperty.METALLICITY, value = metallicity?.toString()),
                        searchable.ifContains(element = StellarHostProperty.LUMINOSITY, value = luminosity?.toString()),
                        searchable.ifContains(element = StellarHostProperty.GRAVITY, value = gravity?.toString()),
                        searchable.ifContains(element = StellarHostProperty.AGE, value = age?.toString()),
                        searchable.ifContains(element = StellarHostProperty.DENSITY, value = density?.toString()),
                        searchable.ifContains(element = StellarHostProperty.ROTATIONAL_VELOCITY, value = rotationalVelocity?.toString()),
                        searchable.ifContains(element = StellarHostProperty.ROTATIONAL_PERIOD, value = rotationalPeriod?.toString()),
                        searchable.ifContains(element = StellarHostProperty.DISTANCE, value = distance?.toString()),
                        searchable.ifContains(element = StellarHostProperty.RA, value = ra?.toString()),
                        searchable.ifContains(element = StellarHostProperty.DEC, value = dec?.toString()),
                        searchable.ifContains(element = StellarHostProperty.PLANET_COUNT, value = planets.size.toString()),
                        searchable.ifContains(element = StellarHostProperty.SPECTRAL_TYPE_SCORE, value = score?.stellarSpectralTypeScore?.toString()),
                        searchable.ifContains(element = StellarHostProperty.MASS_SCORE, value = score?.stellarMassScore?.toString()),
                        searchable.ifContains(element = StellarHostProperty.AGE_SCORE, value = score?.stellarAgeScore?.toString()),
                        searchable.ifContains(element = StellarHostProperty.ACTIVITY_SCORE, value = score?.stellarActivityScore?.toString()),
                        searchable.ifContains(element = StellarHostProperty.ROTATIONAL_PERIOD_SCORE, value = score?.stellarRotationalPeriodScore?.toString()),
                        searchable.ifContains(element = StellarHostProperty.GRAVITY_SCORE, value = score?.stellarGravityScore?.toString()),
                        searchable.ifContains(element = StellarHostProperty.METALLICITY_SCORE, value = score?.stellarMetallicityScore?.toString()),
                        searchable.ifContains(element = StellarHostProperty.EFFECTIVE_TEMPERATURE_SCORE, value = score?.stellarEffectiveTemperatureScore?.toString()),
                    )
                }.any { it.lowercase().contains(other = searchLowercase) }
            }
        } else stellarHosts

    private fun searchPlanets(search: String, searchable: List<PlanetProperty>, planets: List<Planet>): List<Planet> =
        if (search.isNotBlank()) {
            val searchLowercase = search.lowercase()
            planets.filter { stellarHost ->
                with(receiver = stellarHost) {
                    listOfNotNull(
                        searchable.ifContains(element = PlanetProperty.NAME, value = name),
                        searchable.ifContains(element = PlanetProperty.STATUS, value = status.displayName),
                        searchable.ifContains(element = PlanetProperty.ORBITAL_PERIOD, value = orbitalPeriod?.toString()),
                        searchable.ifContains(element = PlanetProperty.ORBIT_AXIS, value = orbitAxis?.toString()),
                        searchable.ifContains(element = PlanetProperty.RADIUS, value = radius?.toString()),
                        searchable.ifContains(element = PlanetProperty.MASS, value = mass?.toString()),
                        searchable.ifContains(element = PlanetProperty.DENSITY, value = density?.toString()),
                        searchable.ifContains(element = PlanetProperty.ECCENTRICITY, value = eccentricity?.toString()),
                        searchable.ifContains(element = PlanetProperty.INSOLATION_FLUX, value = insolationFlux?.toString()),
                        searchable.ifContains(element = PlanetProperty.TEMPERATURE, value = equilibriumTemperature?.toString()),
                        searchable.ifContains(element = PlanetProperty.OCCULTATION_DEPTH, value = occultationDepth?.toString()),
                        searchable.ifContains(element = PlanetProperty.INCLINATION, value = inclination?.toString()),
                        searchable.ifContains(element = PlanetProperty.OBLIQUITY, value = obliquity?.toString()),
                        searchable.ifContains(element = PlanetProperty.HABITABILITY, value = score?.habitabilityScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.TYPE, value = score?.planetType?.displayName),
                        searchable.ifContains(element = PlanetProperty.ROCHE_SCORE, value = score?.rocheScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.HABITABLE_ZONE_KOPPARAPU_SCORE, value = score?.habitableZoneKopparapuScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.HABITABLE_ZONE_KASTING_SCORE, value = score?.habitableZoneKastingScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.RADIUS_SCORE, value = score?.planetRadiusScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.MASS_SCORE, value = score?.planetMassScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.TELLURICITY_SCORE, value = score?.planetTelluricityScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.ECCENTRICITY_SCORE, value = score?.planetEccentricityScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.TEMPERATURE_SCORE, value = score?.planetTemperatureScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.OBLIQUITY_SCORE, value = score?.planetObliquityScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.ESI_SCORE, value = score?.planetEsiScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.PROTECTION_SCORE, value = score?.planetProtectionScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.TIDAL_LOCKING_SCORE, value = score?.planetTidalLockingScore?.toString()),
                        searchable.ifContains(element = PlanetProperty.CONFIDENCE, value = score?.confidenceScore?.toString()),
                    )
                }.any { it.lowercase().contains(other = searchLowercase) }
            }
        } else planets

    private fun openStellarHost(state: StellarExplorerState, action: StellarExplorerAction.OpenStellarHost): Job = launch {
        if (state.currentContent != Content.LIST_HOSTS) return@launch
        updateState {
            it.copy(
                currentContent = Content.DETAIL_HOSTS,
                selectedStellarHost = action.stellarHost,
                filteredPlanets = action.stellarHost.planets
            )
        }
    }

    private fun openPlanet(state: StellarExplorerState, action: StellarExplorerAction.OpenPlanet): Job = launch {
        if (state.currentContent != Content.LIST_PLANETS) return@launch
        val filteredStellarHosts = state.stellarHosts.filter { stellarHost ->
            stellarHost.id == action.planet.stellarHostId
        }
        updateState {
            it.copy(
                currentContent = Content.DETAIL_PLANETS,
                selectedPlanet = action.planet,
                filteredStellarHosts = filteredStellarHosts
            )
        }
    }

    private fun sortStellarHosts(state: StellarExplorerState, action: StellarExplorerAction.SortStellarHosts): Job = launch {
        val filteredStellarHosts = with(receiver = state.filteredStellarHosts) {
            sortedWith(comparator = getStellarHostComparator(sort = action.sort, ascending = state.sortAscending).thenBy { it.id })
        }
        updateState {
            it.copy(
                filteredStellarHosts = filteredStellarHosts,
                sortStellarHostProperty = action.sort
            )
        }
    }

    private fun sortPlanets(state: StellarExplorerState, action: StellarExplorerAction.SortPlanets): Job = launch {
        val filteredPlanets = with(receiver = state.filteredPlanets) {
            sortedWith(comparator = getPlanetsComparator(sort = action.sort, ascending = state.sortAscending).thenBy { it.id })
        }
        updateState {
            it.copy(
                filteredPlanets = filteredPlanets,
                sortPlanetProperty = action.sort
            )
        }
    }

    private fun changeSortDirection(state: StellarExplorerState): Job = launch {
        updateState { it.copy(sortAscending = !it.sortAscending) }.join()
        when (state.currentContent) {
            Content.LIST_HOSTS -> send(action = StellarExplorerAction.SortStellarHosts(sort = state.sortStellarHostProperty))
            Content.LIST_PLANETS -> send(action = StellarExplorerAction.SortPlanets(sort = state.sortPlanetProperty))
            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun changeStellarHostsVisibility(state: StellarExplorerState, action: StellarExplorerAction.ChangeStellarHostsVisibility): Job = launch {
        val visibleStellarHostProperties = state.visibleStellarHostProperties.plusOrMinus(value = action.property)
        updateState { it.copy(visibleStellarHostProperties = visibleStellarHostProperties) }
    }

    private fun changePlanetVisibility(state: StellarExplorerState, action: StellarExplorerAction.ChangePlanetVisibility): Job = launch {
        val visiblePlanetProperties = state.visiblePlanetProperties.plusOrMinus(value = action.property)
        updateState { it.copy(visiblePlanetProperties = visiblePlanetProperties) }
    }

    private fun changeStellarHostsSearchable(state: StellarExplorerState, action: StellarExplorerAction.ChangeStellarHostsSearchable): Job = launch {
        val searchableStellarHostProperties = state.searchableStellarHostProperties.plusOrMinus(value = action.property)
        updateState { it.copy(searchableStellarHostProperties = searchableStellarHostProperties) }
    }

    private fun changePlanetSearchable(state: StellarExplorerState, action: StellarExplorerAction.ChangePlanetSearchable): Job = launch {
        val searchablePlanetProperties = state.searchablePlanetProperties.plusOrMinus(value = action.property)
        updateState { it.copy(searchablePlanetProperties = searchablePlanetProperties) }
    }

    override fun setBackNavigation(): () -> Unit = {
        when (stateFlow.value.currentContent) {
            Content.LIST_HOSTS,
            Content.LIST_PLANETS -> navigate(
                screen = Screen.MAIN_MENU,
                state = MainMenuState(currentContent = MainMenuContent.LEARN_MENU)
            )

            Content.DETAIL_HOSTS -> updateState {
                it.copy(
                    currentContent = Content.LIST_HOSTS,
                    selectedStellarHost = null
                )
            }

            Content.DETAIL_PLANETS -> updateState {
                it.copy(
                    currentContent = Content.LIST_PLANETS,
                    selectedPlanet = null
                )
            }
        }
    }

    override fun reducer(state: StellarExplorerState, action: StellarExplorerAction) {
        when (action) {
            is StellarExplorerAction.SaveIndex -> updateState { it.copy(listIndex = action.index) }
            StellarExplorerAction.ChangeView -> changeView(state = state)
            is StellarExplorerAction.Search -> search(state = state, action = action)
            is StellarExplorerAction.OpenStellarHost -> openStellarHost(state = state, action = action)
            is StellarExplorerAction.OpenPlanet -> openPlanet(state = state, action = action)
            is StellarExplorerAction.SortStellarHosts -> sortStellarHosts(state = state, action = action)
            is StellarExplorerAction.SortPlanets -> sortPlanets(state = state, action = action)
            StellarExplorerAction.ChangeSortDirection -> changeSortDirection(state = state)
            is StellarExplorerAction.ChangeStellarHostsVisibility -> changeStellarHostsVisibility(state = state, action = action)
            is StellarExplorerAction.ChangePlanetVisibility -> changePlanetVisibility(state = state, action = action)
            is StellarExplorerAction.ChangeStellarHostsSearchable -> changeStellarHostsSearchable(state = state, action = action)
            is StellarExplorerAction.ChangePlanetSearchable -> changePlanetSearchable(state = state, action = action)
        }
    }
}

/**
 * Returns [value] if the [element] is present, otherwise returns null.
 */
internal fun <E, V> Collection<E>.ifContains(element: E, value: V?): V? =
    if (contains(element)) value else null

/**
 * Adds [value] if it is not present, otherwise removes it.
 */
internal fun <V> Iterable<V>.plusOrMinus(value: V): List<V> =
    if (contains(element = value)) minus(element = value) else plus(element = value)
