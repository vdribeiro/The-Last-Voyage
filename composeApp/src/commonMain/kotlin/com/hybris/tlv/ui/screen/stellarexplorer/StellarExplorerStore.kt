package com.hybris.tlv.ui.screen.stellarexplorer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.domain.usecase.space.SpaceUseCases
import com.hybris.tlv.domain.usecase.space.formula.Habitability
import com.hybris.tlv.domain.usecase.space.model.Formula
import com.hybris.tlv.domain.usecase.space.model.StellarHost

internal class StellarExplorerStore(
    private val spaceUseCases: SpaceUseCases,
): Store<StellarExplorerState, StellarExplorerAction>(
    initialState = StellarExplorerState()
) {
    @get:VisibleForTesting
    internal var formula: Formula = Formula()
    @get:VisibleForTesting
    internal var stellarHostsFlow: MutableStateFlow<List<StellarHost>> = MutableStateFlow(value = emptyList())

    init {
        setup()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun setup() {
        Telemetry.info(tag = TAG, message = "Setup")

        observeExoplanets()
        combine(
            flow = stateFlow,
            flow2 = stellarHostsFlow
        ) { state, stellarHosts ->
            FilterCriteria(
                currentContent = state.currentContent,
                search = state.search,
                sortStellarHostProperty = state.sortStellarHostProperty,
                sortPlanetProperty = state.sortPlanetProperty,
                sortAscending = state.sortAscending,
                searchableStellarHostProperties = state.searchableStellarHostProperties,
                searchablePlanetProperties = state.searchablePlanetProperties,
                stellarHosts = stellarHosts
            )
        }
            .distinctUntilChanged()
            .mapLatest { criteria ->
                with(receiver = criteria) {
                    Pair(
                        first = if (currentContent == Content.LIST_HOSTS) stellarHosts.searchAndSortStellarHosts(
                            search = search,
                            searchable = searchableStellarHostProperties,
                            sort = sortStellarHostProperty,
                            ascending = sortAscending
                        ) else null,
                        second = if (currentContent == Content.LIST_PLANETS) stellarHosts.flatMap { it.planets }.searchAndSortPlanets(
                            search = search,
                            searchable = searchablePlanetProperties,
                            sort = sortPlanetProperty,
                            ascending = sortAscending
                        ) else null
                    )
                }
            }
            .flowOn(context = Dispatcher.Default)
            .onEach { (stellarHosts, planets) ->
                updateState {
                    it.copy(
                        stellarHosts = stellarHosts ?: it.stellarHosts,
                        planets = planets ?: it.planets
                    )
                }
            }
            .launchIn(scope = viewModelScope)

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun observeExoplanets(): Job = spaceUseCases.observeExoplanets().map { stellarHosts ->
        stellarHosts.apply {
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
    }
        .flowOn(context = Dispatcher.Default)
        .observe { stellarHosts ->
            stellarHostsFlow.value = stellarHosts
            updateState { it.copy(loading = false) }
        }

    private fun changeView(state: StellarExplorerState) {
        Telemetry.info(tag = TAG, message = "Changed view")
        when (state.currentContent) {
            Content.LIST_HOSTS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        listState = LazyListState(),
                        search = ""
                    )
                }
            }

            Content.LIST_PLANETS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        listState = LazyListState(),
                        search = ""
                    )
                }
            }

            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
        Telemetry.info(tag = TAG, message = "New view: ${state.currentContent}")
    }

    private fun search(state: StellarExplorerState, action: StellarExplorerAction.Search) {
        Telemetry.info(tag = TAG, message = "Searching for ${action.search}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> updateState { it.copy(listState = LazyListState(), search = action.search) }
            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun openStellarHost(state: StellarExplorerState, action: StellarExplorerAction.OpenStellarHost) {
        if (state.currentContent != Content.LIST_HOSTS) return
        Telemetry.info(tag = TAG, message = "Opening stellar host ${action.stellarHost}")
        val filteredPlanets = action.stellarHost.planets
        updateState {
            it.copy(
                currentContent = Content.DETAIL_HOSTS,
                selectedStellarHost = action.stellarHost,
                planets = filteredPlanets
            )
        }
    }

    private fun openPlanet(state: StellarExplorerState, action: StellarExplorerAction.OpenPlanet): Job = launch(id = "openPlanet", replace = true) {
        if (state.currentContent != Content.LIST_PLANETS) return@launch
        Telemetry.info(tag = TAG, message = "Opening planet ${action.planet}")
        val filteredStellarHosts = stellarHostsFlow.value.filter { stellarHost -> stellarHost.id == action.planet.stellarHostId }
        updateState {
            it.copy(
                currentContent = Content.DETAIL_PLANETS,
                selectedPlanet = action.planet,
                stellarHosts = filteredStellarHosts
            )
        }
    }

    private fun sortStellarHosts(state: StellarExplorerState, action: StellarExplorerAction.SortStellarHosts) {
        Telemetry.info(tag = TAG, message = "Sorting stellar hosts by ${action.sort}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> updateState { it.copy(sortStellarHostProperty = action.sort) }
            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun sortPlanets(state: StellarExplorerState, action: StellarExplorerAction.SortPlanets) {
        Telemetry.info(tag = TAG, message = "Sorting planets by ${action.sort}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> updateState { it.copy(sortPlanetProperty = action.sort) }
            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun changeSortDirection(state: StellarExplorerState) {
        val ascending = !state.sortAscending
        Telemetry.info(tag = TAG, message = "Changed sort direction to ${if (ascending) "ascending" else "descending"}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.LIST_PLANETS -> updateState { it.copy(sortAscending = ascending) }
            Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
        }
    }

    private fun changeStellarHostsVisibility(
        state: StellarExplorerState,
        action: StellarExplorerAction.ChangeStellarHostsVisibility
    ): Job = launch(id = "changeStellarHostsVisibility", replace = true) {
        Telemetry.info(tag = TAG, message = "Changing stellar host visibility for ${action.property}")
        val visibleStellarHostProperties = state.visibleStellarHostProperties.plusOrMinus(element = action.property)
        updateState { it.copy(visibleStellarHostProperties = visibleStellarHostProperties) }
    }

    private fun changePlanetVisibility(
        state: StellarExplorerState,
        action: StellarExplorerAction.ChangePlanetVisibility
    ): Job = launch(id = "changePlanetVisibility", replace = true) {
        Telemetry.info(tag = TAG, message = "Changing stellar host visibility for ${action.property}")
        val visiblePlanetProperties = state.visiblePlanetProperties.plusOrMinus(element = action.property)
        updateState { it.copy(visiblePlanetProperties = visiblePlanetProperties) }
    }

    private fun changeStellarHostsSearchable(
        state: StellarExplorerState,
        action: StellarExplorerAction.ChangeStellarHostsSearchable
    ): Job = launch(id = "changeStellarHostsSearchable", replace = true) {
        Telemetry.info(tag = TAG, message = "Changing stellar host searchable property ${action.property}")
        val searchableStellarHostProperties = state.searchableStellarHostProperties.plusOrMinus(element = action.property)
        updateState { it.copy(listState = LazyListState(), searchableStellarHostProperties = searchableStellarHostProperties) }
    }

    private fun changePlanetSearchable(
        state: StellarExplorerState,
        action: StellarExplorerAction.ChangePlanetSearchable
    ): Job = launch(id = "changePlanetSearchable", replace = true) {
        Telemetry.info(tag = TAG, message = "Changing planet searchable property ${action.property}")
        val searchablePlanetProperties = state.searchablePlanetProperties.plusOrMinus(element = action.property)
        updateState { it.copy(listState = LazyListState(), searchablePlanetProperties = searchablePlanetProperties) }
    }

    override fun back(state: StellarExplorerState) {
        when (state.currentContent) {
            Content.LIST_HOSTS,
            Content.LIST_PLANETS -> super.back(state = state)

            Content.DETAIL_HOSTS -> launch {
                updateState { it.copy(currentContent = Content.LIST_HOSTS, selectedStellarHost = null) }
            }

            Content.DETAIL_PLANETS -> launch {
                updateState { it.copy(currentContent = Content.LIST_PLANETS, selectedPlanet = null) }
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
