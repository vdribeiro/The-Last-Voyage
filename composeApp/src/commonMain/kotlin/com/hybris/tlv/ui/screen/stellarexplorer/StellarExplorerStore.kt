package com.hybris.tlv.ui.screen.stellarexplorer

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.component.LazyListIndex
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.stellarexplorer.model.PlanetProperty
import com.hybris.tlv.ui.screen.stellarexplorer.model.StellarHostProperty
import com.hybris.tlv.ui.screen.stellarexplorer.model.searchPlanets
import com.hybris.tlv.ui.screen.stellarexplorer.model.searchStellarHosts
import com.hybris.tlv.ui.screen.stellarexplorer.model.sortPlanets
import com.hybris.tlv.ui.screen.stellarexplorer.model.sortStellarHosts
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
    val search: String = "",
    val sortStellarHostProperty: StellarHostProperty = StellarHostProperty.DISTANCE,
    val sortPlanetProperty: PlanetProperty = PlanetProperty.NAME,
    val sortAscending: Boolean = true,
    val visibleStellarHostProperties: Set<StellarHostProperty> = setOf(
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
    val visiblePlanetProperties: Set<PlanetProperty> = setOf(
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
    val searchableStellarHostProperties: Set<StellarHostProperty> = setOf(StellarHostProperty.NAME),
    val searchablePlanetProperties: Set<PlanetProperty> = setOf(PlanetProperty.NAME)
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
        val formula = Formula()
        val stellarHosts = spaceUseCases.getExoplanets().apply {
            forEach { stellarHost ->
                stellarHost.score = Habitability.calculateScores(
                    stellarHost = stellarHost,
                    planet = null,
                    formula = formula
                )
                stellarHost.planets.forEach { planet ->
                    planet.score = Habitability.calculateScores(
                        stellarHost = stellarHost,
                        planet = planet,
                        formula = formula
                    )
                }
            }
        }
        val planets = stellarHosts.map { it.planets }.flatten()

        updateState {
            it.copy(
                loading = false,
                stellarHosts = stellarHosts,
                planets = planets
            )
        }.join()
        refresh()
    }

    private fun refresh(): Job = launch {
        val state = stateFlow.value
        when (state.currentContent) {
            Content.LIST_HOSTS -> {
                val filteredStellarHosts = state.stellarHosts.searchStellarHosts(
                    search = state.search,
                    searchable = state.searchableStellarHostProperties,
                ).sortStellarHosts(sort = state.sortStellarHostProperty, ascending = state.sortAscending)
                updateState { it.copy(filteredStellarHosts = filteredStellarHosts) }
            }

            Content.LIST_PLANETS -> {
                val filteredPlanets = state.planets.searchPlanets(
                    search = state.search,
                    searchable = state.searchablePlanetProperties
                ).sortPlanets(sort = state.sortPlanetProperty, ascending = state.sortAscending)
                updateState { it.copy(filteredPlanets = filteredPlanets) }
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
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

    private fun changeView(state: StellarExplorerState): Job = launch {
        when (state.currentContent) {
            Content.LIST_HOSTS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        listIndex = LazyListIndex(),
                        search = ""
                    )
                }.join()
                refresh()
            }

            Content.LIST_PLANETS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        listIndex = LazyListIndex(),
                        search = ""
                    )
                }.join()
                refresh()
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun search(state: StellarExplorerState, action: StellarExplorerAction.Search): Job = launch {
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> {
                updateState {
                    it.copy(
                        listIndex = LazyListIndex(),
                        search = action.search
                    )
                }.join()
                refresh()
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun openStellarHost(state: StellarExplorerState, action: StellarExplorerAction.OpenStellarHost): Job = launch {
        if (state.currentContent != Content.LIST_HOSTS) return@launch
        val filteredPlanets = action.stellarHost.planets
        updateState {
            it.copy(
                currentContent = Content.DETAIL_HOSTS,
                selectedStellarHost = action.stellarHost,
                filteredPlanets = filteredPlanets
            )
        }
    }

    private fun openPlanet(state: StellarExplorerState, action: StellarExplorerAction.OpenPlanet): Job = launch {
        if (state.currentContent != Content.LIST_PLANETS) return@launch
        val filteredStellarHosts = state.stellarHosts.filter { stellarHost -> stellarHost.id == action.planet.stellarHostId }
        updateState {
            it.copy(
                currentContent = Content.DETAIL_PLANETS,
                selectedPlanet = action.planet,
                filteredStellarHosts = filteredStellarHosts
            )
        }
    }

    private fun sortStellarHosts(state: StellarExplorerState, action: StellarExplorerAction.SortStellarHosts): Job = launch {
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> {
                updateState { it.copy(sortStellarHostProperty = action.sort) }.join()
                refresh()
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun sortPlanets(state: StellarExplorerState, action: StellarExplorerAction.SortPlanets): Job = launch {
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> {
                updateState { it.copy(sortPlanetProperty = action.sort) }.join()
                refresh()
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
        refresh()
    }

    private fun changeSortDirection(state: StellarExplorerState): Job = launch {
        val ascending = !state.sortAscending
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> {
                updateState { it.copy(sortAscending = ascending) }.join()
                refresh()
            }

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
        updateState {
            it.copy(
                listIndex = LazyListIndex(),
                searchableStellarHostProperties = searchableStellarHostProperties
            )
        }.join()
        refresh()
    }

    private fun changePlanetSearchable(state: StellarExplorerState, action: StellarExplorerAction.ChangePlanetSearchable): Job = launch {
        val searchablePlanetProperties = state.searchablePlanetProperties.plusOrMinus(value = action.property)
        updateState {
            it.copy(
                listIndex = LazyListIndex(),
                searchablePlanetProperties = searchablePlanetProperties

            )
        }.join()
        refresh()
    }

    override fun setBackNavigation(): () -> Unit = {
        when (stateFlow.value.currentContent) {
            Content.LIST_HOSTS,
            Content.LIST_PLANETS -> navigate(
                screen = Screen.MAIN_MENU,
                state = MainMenuState(currentContent = MainMenuContent.LEARN_MENU)
            )

            Content.DETAIL_HOSTS -> launch {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        selectedStellarHost = null
                    )
                }.join()
                refresh()
            }

            Content.DETAIL_PLANETS -> launch {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        selectedPlanet = null
                    )
                }.join()
                refresh()
            }
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
internal fun <V> Iterable<V>.plusOrMinus(value: V): Set<V> =
    (if (contains(element = value)) minus(element = value) else plus(element = value)).toSet()
