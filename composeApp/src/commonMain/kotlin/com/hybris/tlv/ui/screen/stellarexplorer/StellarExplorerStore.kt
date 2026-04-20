package com.hybris.tlv.ui.screen.stellarexplorer

import kotlin.concurrent.Volatile
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.space.SpaceUseCases
import com.hybris.tlv.domain.usecase.space.formula.Habitability
import com.hybris.tlv.domain.usecase.space.model.Formula
import com.hybris.tlv.data.translation.TranslationCache
import com.hybris.tlv.test.VisibleForTesting
import com.hybris.tlv.ui.screen.Store

internal class StellarExplorerStore(
    private val spaceUseCases: SpaceUseCases,
): Store<StellarExplorerState, StellarExplorerAction>(
    initialState = StellarExplorerState()
) {
    private val formula: Formula = Formula()
    @VisibleForTesting
    @Volatile
    internal var selectedStellarHost: Exoplanets.Host? = null
    @VisibleForTesting
    @Volatile
    internal var selectedPlanet: Exoplanets.Planet? = null

    init {
        setup()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val stellarHostsFlow = spaceUseCases.observeExoplanets()
            .map { stellarHosts ->
                stellarHosts.onEach { stellarHost ->
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
            .flowOn(context = Dispatcher.Default)
            .toStateFlow(initialValue = emptyList())

        val criteriaFlow = stateFlow
            .map { it.toFilterExoplanetsCriteria() }
            .distinctUntilChanged()

        combine(
            flow = criteriaFlow,
            flow2 = stellarHostsFlow,
            flow3 = TranslationCache.cacheState
        ) { criteria, stellarHosts, translations ->
            FilterExoplanetsCriteriaCombine(
                criteria = criteria,
                stellarHosts = stellarHosts,
                translations = translations
            )
        }
            .mapLatest {
                it.toFilterExoplanetsCriteriaResult(
                    selectedStellarHost = selectedStellarHost,
                    selectedPlanet = selectedPlanet
                )
            }
            .flowOn(context = Dispatcher.Default)
            .observe(id = "filterExoplanets") { result ->
                updateState {
                    it.copy(
                        loading = false,
                        exoplanets = result.exoplanets,
                        properties = result.properties,
                    )
                }
            }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun changeView(state: StellarExplorerState): Job = launch(id = "changeView") {
        Telemetry.info(tag = TAG, message = "Changed view")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_HOSTS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        search = "",
                    )
                }
            }

            Content.LIST_PLANETS, Content.DETAIL_PLANETS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        search = "",
                    )
                }
            }
        }
        Telemetry.info(tag = TAG, message = "New view: ${state.currentContent}")
    }

    private fun search(action: StellarExplorerAction.Search) {
        Telemetry.info(tag = TAG, message = "Searching for ${action.search}")
        updateState { it.copy(search = action.search) }
    }

    private fun openStellarHost(action: StellarExplorerAction.OpenStellarHost): Job = launch(id = "openStellarHost") {
        Telemetry.info(tag = TAG, message = "Opening stellar host ${action.stellarHost.id}")
        selectedStellarHost = action.stellarHost
        updateState {
            it.copy(
                currentContent = Content.DETAIL_HOSTS,
                search = "",
            )
        }
    }

    private fun openPlanet(action: StellarExplorerAction.OpenPlanet): Job = launch(id = "openPlanet") {
        Telemetry.info(tag = TAG, message = "Opening planet ${action.planet.id}")
        selectedPlanet = action.planet
        updateState {
            it.copy(
                currentContent = Content.DETAIL_PLANETS,
                search = "",
            )
        }
    }

    private fun sort(state: StellarExplorerState, action: StellarExplorerAction.Sort) {
        Telemetry.info(tag = TAG, message = "Sorting by ${action.sort}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_PLANETS -> updateState { it.copy(sortStellarHostProperty = action.sort) }
            Content.LIST_PLANETS, Content.DETAIL_HOSTS -> updateState { it.copy(sortPlanetProperty = action.sort) }
        }
    }

    private fun changeSortDirection(state: StellarExplorerState) {
        Telemetry.info(tag = TAG, message = "Changed sort direction")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_PLANETS -> updateState { it.copy(sortStellarHostAscending = !it.sortStellarHostAscending) }
            Content.LIST_PLANETS, Content.DETAIL_HOSTS -> updateState { it.copy(sortPlanetAscending = !it.sortPlanetAscending) }
        }
    }

    private fun changeVisibility(state: StellarExplorerState, action: StellarExplorerAction.ChangeVisibility): Job = launch(id = "changeVisibility") {
        Telemetry.info(tag = TAG, message = "Changing visibility for ${action.property}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_PLANETS -> {
                val visibleProperties = state.visibleStellarHostProperties.plusOrMinus(element = action.property).toPersistentList()
                updateState { it.copy(visibleStellarHostProperties = visibleProperties) }
            }

            Content.LIST_PLANETS, Content.DETAIL_HOSTS -> {
                val visibleProperties = state.visiblePlanetProperties.plusOrMinus(element = action.property).toPersistentList()
                updateState { it.copy(visiblePlanetProperties = visibleProperties) }
            }
        }
    }

    private fun changeSearchable(state: StellarExplorerState, action: StellarExplorerAction.ChangeSearchable): Job = launch(id = "changeSearchable") {
        Telemetry.info(tag = TAG, message = "Changing searchable property ${action.property}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_PLANETS -> {
                val searchableProperties = state.searchableStellarHostProperties.plusOrMinus(element = action.property).toPersistentList()
                updateState { it.copy(searchableStellarHostProperties = searchableProperties) }
            }

            Content.LIST_PLANETS, Content.DETAIL_HOSTS -> {
                val searchableProperties = state.searchablePlanetProperties.plusOrMinus(element = action.property).toPersistentList()
                updateState { it.copy(searchablePlanetProperties = searchableProperties) }
            }
        }
    }

    private fun navigateBack(state: StellarExplorerState) {
        when (state.currentContent) {
            Content.LIST_HOSTS,
            Content.LIST_PLANETS -> navigateBack()

            Content.DETAIL_HOSTS -> {
                selectedStellarHost = null
                updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        search = "",
                    )
                }
            }

            Content.DETAIL_PLANETS -> {
                selectedPlanet = null
                updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        search = "",
                    )
                }
            }
        }
    }

    override fun reducer(state: StellarExplorerState, action: StellarExplorerAction) {
        when (action) {
            StellarExplorerAction.Back -> navigateBack(state = state)
            StellarExplorerAction.ChangeView -> changeView(state = state)
            is StellarExplorerAction.Search -> search(action = action)
            is StellarExplorerAction.OpenStellarHost -> openStellarHost(action = action)
            is StellarExplorerAction.OpenPlanet -> openPlanet(action = action)
            is StellarExplorerAction.Sort -> sort(state = state, action = action)
            StellarExplorerAction.ChangeSortDirection -> changeSortDirection(state = state)
            is StellarExplorerAction.ChangeVisibility -> changeVisibility(state = state, action = action)
            is StellarExplorerAction.ChangeSearchable -> changeSearchable(state = state, action = action)
        }
    }

    /**
     * Adds [element] if it is not present, otherwise removes it.
     */
    private fun <V> Iterable<V>.plusOrMinus(element: V): Collection<V> =
        if (contains(element = element)) minus(element = element) else plus(element = element)

    companion object {
        private const val TAG = "StellarExplorerStore"
    }
}
