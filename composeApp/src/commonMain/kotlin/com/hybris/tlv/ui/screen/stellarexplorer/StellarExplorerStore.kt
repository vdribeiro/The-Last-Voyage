package com.hybris.tlv.ui.screen.stellarexplorer

import kotlinx.coroutines.Job
import androidx.compose.foundation.lazy.LazyListState
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.formula.Habitability
import com.hybris.tlv.usecase.space.model.Formula

internal class StellarExplorerStore(
    private val spaceUseCases: SpaceUseCases,
): Store<StellarExplorerState, StellarExplorerAction>(
    initialState = StellarExplorerState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val stellarHosts = spaceUseCases.getExoplanets().apply {
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
        val planets = stellarHosts.map { it.planets }.flatten()

        updateState {
            it.copy(
                loading = false,
                stellarHosts = stellarHosts,
                planets = planets,
            )
        }.join()
        refresh()
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    /**
     * Apply filters to the data and refresh the state.
     */
    private fun refresh() {
        val state = stateFlow.value
        when (state.currentContent) {
            Content.LIST_HOSTS -> {
                val filteredStellarHosts = state.stellarHosts.searchStellarHosts(
                    search = state.search,
                    searchable = state.searchableStellarHostProperties,
                ).sortStellarHosts(
                    sort = state.sortStellarHostProperty,
                    ascending = state.sortAscending
                )
                updateState { it.copy(filteredStellarHosts = filteredStellarHosts) }
            }

            Content.LIST_PLANETS -> {
                val filteredPlanets = state.planets.searchPlanets(
                    search = state.search,
                    searchable = state.searchablePlanetProperties
                ).sortPlanets(
                    sort = state.sortPlanetProperty,
                    ascending = state.sortAscending
                )
                updateState { it.copy(filteredPlanets = filteredPlanets) }
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun changeView(state: StellarExplorerState): Job = launch {
        Telemetry.info(tag = TAG, message = "Changed view")
        when (state.currentContent) {
            Content.LIST_HOSTS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        listState = LazyListState(),
                        search = ""
                    )
                }.join()
                refresh()
            }

            Content.LIST_PLANETS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        listState = LazyListState(),
                        search = ""
                    )
                }.join()
                refresh()
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
        Telemetry.info(tag = TAG, message = "New view: ${state.currentContent}")
    }

    private fun search(state: StellarExplorerState, action: StellarExplorerAction.Search): Job = launch {
        Telemetry.info(tag = TAG, message = "Searching for ${action.search}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> {
                updateState {
                    it.copy(
                        listState = LazyListState(),
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
        Telemetry.info(tag = TAG, message = "Opening stellar host ${action.stellarHost}")
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
        Telemetry.info(tag = TAG, message = "Opening planet ${action.planet}")
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
        Telemetry.info(tag = TAG, message = "Sorting stellar hosts by ${action.sort}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> {
                updateState { it.copy(sortStellarHostProperty = action.sort) }.join()
                refresh()
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun sortPlanets(state: StellarExplorerState, action: StellarExplorerAction.SortPlanets): Job = launch {
        Telemetry.info(tag = TAG, message = "Sorting planets by ${action.sort}")
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
        Telemetry.info(tag = TAG, message = "Changed sort direction to ${if (ascending) "ascending" else "descending"}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> {
                updateState { it.copy(sortAscending = ascending) }.join()
                refresh()
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun changeStellarHostsVisibility(state: StellarExplorerState, action: StellarExplorerAction.ChangeStellarHostsVisibility): Job = launch {
        Telemetry.info(tag = TAG, message = "Changing stellar host visibility for ${action.property}")
        val visibleStellarHostProperties = state.visibleStellarHostProperties.plusOrMinus(element = action.property)
        updateState { it.copy(visibleStellarHostProperties = visibleStellarHostProperties) }
    }

    private fun changePlanetVisibility(state: StellarExplorerState, action: StellarExplorerAction.ChangePlanetVisibility): Job = launch {
        Telemetry.info(tag = TAG, message = "Changing stellar host visibility for ${action.property}")
        val visiblePlanetProperties = state.visiblePlanetProperties.plusOrMinus(element = action.property)
        updateState { it.copy(visiblePlanetProperties = visiblePlanetProperties) }
    }

    private fun changeStellarHostsSearchable(state: StellarExplorerState, action: StellarExplorerAction.ChangeStellarHostsSearchable): Job = launch {
        Telemetry.info(tag = TAG, message = "Changing stellar host searchable property ${action.property}")
        val searchableStellarHostProperties = state.searchableStellarHostProperties.plusOrMinus(element = action.property)
        updateState {
            it.copy(
                listState = LazyListState(),
                searchableStellarHostProperties = searchableStellarHostProperties
            )
        }.join()
        refresh()
    }

    private fun changePlanetSearchable(state: StellarExplorerState, action: StellarExplorerAction.ChangePlanetSearchable): Job = launch {
        Telemetry.info(tag = TAG, message = "Changing planet searchable property ${action.property}")
        val searchablePlanetProperties = state.searchablePlanetProperties.plusOrMinus(element = action.property)
        updateState {
            it.copy(
                listState = LazyListState(),
                searchablePlanetProperties = searchablePlanetProperties

            )
        }.join()
        refresh()
    }

    override fun back(state: StellarExplorerState) {
        when (state.currentContent) {
            Content.LIST_HOSTS,
            Content.LIST_PLANETS -> super.back(state = state)

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

    override fun reducer(state: StellarExplorerState, action: StellarExplorerAction) {
        when (action) {
            is StellarExplorerAction.SaveListState -> updateState { it.copy(listState = action.listState) }
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

    /**
     * Adds [element] if it is not present, otherwise removes it.
     */
    private fun <V> Iterable<V>.plusOrMinus(element: V): Set<V> =
        (if (contains(element = element)) minus(element = element) else plus(element = element)).toSet()

    companion object {
        private const val TAG = "StellarExplorerStore"
    }
}
