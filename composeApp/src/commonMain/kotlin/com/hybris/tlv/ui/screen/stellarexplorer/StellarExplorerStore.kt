package com.hybris.tlv.ui.screen.stellarexplorer

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.formula.Habitability
import com.hybris.tlv.usecase.space.model.Formula
import kotlinx.coroutines.Job
import com.hybris.tlv.ui.screen.mainmenu.Content as MainMenuContent

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
    override fun setup(state: StellarExplorerState): Job = launch {
        val loading = state.loading ?: false
        val currentContent = state.currentContent ?: Content.LIST_HOSTS
        val listIndex = state.listIndex ?: LazyListIndex()
        val filteredStellarHosts = state.filteredStellarHosts ?: emptyList()
        val filteredPlanets = state.filteredPlanets ?: emptyList()
        val selectedStellarHost = state.selectedStellarHost
        val selectedPlanet = state.selectedPlanet
        val search = state.search ?: ""
        val sortStellarHostProperty = state.sortStellarHostProperty ?: StellarHostProperty.DISTANCE
        val sortPlanetProperty = state.sortPlanetProperty ?: PlanetProperty.HABITABILITY
        val sortAscending = state.sortAscending ?: true
        val visibleStellarHostProperties = state.visibleStellarHostProperties ?: setOf(
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
        )
        val visiblePlanetProperties = state.visiblePlanetProperties ?: setOf(
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
        )
        val searchableStellarHostProperties = state.searchableStellarHostProperties ?: setOf(StellarHostProperty.NAME)
        val searchablePlanetProperties = state.searchablePlanetProperties ?: setOf(PlanetProperty.NAME)

        val stellarHosts = state.stellarHosts ?: spaceUseCases.getExoplanets().apply {
            val formula = Formula()
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
        val planets = state.planets ?: stellarHosts.map { it.planets }.flatten()

        updateState {
            it.copy(
                loading = loading,
                currentContent = currentContent,
                stellarHosts = stellarHosts,
                planets = planets,
                listIndex = listIndex,
                filteredStellarHosts = filteredStellarHosts,
                filteredPlanets = filteredPlanets,
                selectedStellarHost = selectedStellarHost,
                selectedPlanet = selectedPlanet,
                search = search,
                sortStellarHostProperty = sortStellarHostProperty,
                sortPlanetProperty = sortPlanetProperty,
                sortAscending = sortAscending,
                visibleStellarHostProperties = visibleStellarHostProperties,
                visiblePlanetProperties = visiblePlanetProperties,
                searchableStellarHostProperties = searchableStellarHostProperties,
                searchablePlanetProperties = searchablePlanetProperties
            )
        }.join()
        refresh()
    }

    /**
     * Apply filters to the data and refresh the state.
     */
    private fun refresh() {
        val state = stateFlow.value
        when (state.currentContent) {
            Content.LIST_HOSTS -> {
                val filteredStellarHosts = state.stellarHosts.orEmpty().searchStellarHosts(
                    search = state.search.orEmpty(),
                    searchable = state.searchableStellarHostProperties.orEmpty(),
                ).sortStellarHosts(
                    sort = state.sortStellarHostProperty ?: StellarHostProperty.DISTANCE,
                    ascending = state.sortAscending ?: true
                )
                updateState { it.copy(filteredStellarHosts = filteredStellarHosts) }
            }

            Content.LIST_PLANETS -> {
                val filteredPlanets = state.planets.orEmpty().searchPlanets(
                    search = state.search.orEmpty(),
                    searchable = state.searchablePlanetProperties.orEmpty()
                ).sortPlanets(
                    sort = state.sortPlanetProperty ?: PlanetProperty.HABITABILITY,
                    ascending = state.sortAscending ?: true
                )
                updateState { it.copy(filteredPlanets = filteredPlanets) }
            }

            null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
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

            null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
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

            null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
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
        val filteredStellarHosts = state.stellarHosts.orEmpty().filter { stellarHost -> stellarHost.id == action.planet.stellarHostId }
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

            null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun sortPlanets(state: StellarExplorerState, action: StellarExplorerAction.SortPlanets): Job = launch {
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> {
                updateState { it.copy(sortPlanetProperty = action.sort) }.join()
                refresh()
            }

            null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
        refresh()
    }

    private fun changeSortDirection(state: StellarExplorerState): Job = launch {
        val ascending = !(state.sortAscending ?: true)
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> {
                updateState { it.copy(sortAscending = ascending) }.join()
                refresh()
            }

            null, Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun changeStellarHostsVisibility(state: StellarExplorerState, action: StellarExplorerAction.ChangeStellarHostsVisibility): Job = launch {
        val visibleStellarHostProperties = state.visibleStellarHostProperties.orEmpty().plusOrMinus(element = action.property)
        updateState { it.copy(visibleStellarHostProperties = visibleStellarHostProperties) }
    }

    private fun changePlanetVisibility(state: StellarExplorerState, action: StellarExplorerAction.ChangePlanetVisibility): Job = launch {
        val visiblePlanetProperties = state.visiblePlanetProperties.orEmpty().plusOrMinus(element = action.property)
        updateState { it.copy(visiblePlanetProperties = visiblePlanetProperties) }
    }

    private fun changeStellarHostsSearchable(state: StellarExplorerState, action: StellarExplorerAction.ChangeStellarHostsSearchable): Job = launch {
        val searchableStellarHostProperties = state.searchableStellarHostProperties.orEmpty().plusOrMinus(element = action.property)
        updateState {
            it.copy(
                listIndex = LazyListIndex(),
                searchableStellarHostProperties = searchableStellarHostProperties
            )
        }.join()
        refresh()
    }

    private fun changePlanetSearchable(state: StellarExplorerState, action: StellarExplorerAction.ChangePlanetSearchable): Job = launch {
        val searchablePlanetProperties = state.searchablePlanetProperties.orEmpty().plusOrMinus(element = action.property)
        updateState {
            it.copy(
                listIndex = LazyListIndex(),
                searchablePlanetProperties = searchablePlanetProperties

            )
        }.join()
        refresh()
    }

    override fun back(state: StellarExplorerState): () -> Unit = {
        when (state.currentContent) {
            null,
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
 * Adds [element] if it is not present, otherwise removes it.
 */
internal fun <V> Iterable<V>.plusOrMinus(element: V): Set<V> =
    (if (contains(element = element)) minus(element = element) else plus(element = element)).toSet()
