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
import com.hybris.tlv.domain.usecase.translation.TranslationCache
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
    private val sortStellarHostPropertyDefault: StellarHostProperty = StellarHostProperty.DISTANCE
    @VisibleForTesting
    @Volatile
    internal var sortStellarHostProperty: String = sortStellarHostPropertyDefault.name
    private val sortPlanetPropertyDefault: PlanetProperty = PlanetProperty.HABITABILITY
    @VisibleForTesting
    @Volatile
    internal var sortPlanetProperty: String = sortPlanetPropertyDefault.name
    private val visibleStellarHostPropertiesDefault: List<StellarHostProperty> = listOf(
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
        StellarHostProperty.DEC
    )
    @VisibleForTesting
    @Volatile
    internal var visibleStellarHostProperties: List<String> = visibleStellarHostPropertiesDefault.map { it.name }
    private val visiblePlanetPropertiesDefault: List<PlanetProperty> = listOf(
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
        PlanetProperty.OBLIQUITY
    )
    @VisibleForTesting
    @Volatile
    internal var visiblePlanetProperties: List<String> = visiblePlanetPropertiesDefault.map { it.name }
    private val searchableStellarHostPropertiesDefault: List<StellarHostProperty> = listOf(StellarHostProperty.NAME)
    @VisibleForTesting
    @Volatile
    internal var searchableStellarHostProperties: List<String> = searchableStellarHostPropertiesDefault.map { it.name }
    private val searchablePlanetPropertiesDefault: List<PlanetProperty> = listOf(PlanetProperty.NAME)
    @VisibleForTesting
    @Volatile
    internal var searchablePlanetProperties: List<String> = searchablePlanetPropertiesDefault.map { it.name }

    init {
        setup()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val visibleProperties = visibleStellarHostPropertiesDefault.map { it.name }.toPersistentList()
        val searchableProperties = searchableStellarHostPropertiesDefault.map { it.name }.toPersistentList()
        updateState {
            it.copy(
                sortProperty = sortStellarHostPropertyDefault.name,
                visibleProperties = visibleProperties,
                searchableProperties = searchableProperties
            )
        }

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
                    selectedPlanet = selectedPlanet,
                    sortStellarHostPropertyDefault = sortStellarHostPropertyDefault,
                    sortPlanetPropertyDefault = sortPlanetPropertyDefault,
                    visibleStellarHostProperties = visibleStellarHostProperties,
                    visiblePlanetProperties = visiblePlanetProperties,
                    searchableStellarHostProperties = searchableStellarHostProperties,
                    searchablePlanetProperties = searchablePlanetProperties
                )
            }
            .flowOn(context = Dispatcher.Default)
            .observe(id = "filterExoplanets") { result ->
                updateState {
                    it.copy(
                        loading = false,
                        exoplanets = result.exoplanets,
                        properties = result.properties,
                        sortProperty = sortStellarHostProperty,
                        visibleProperties = result.visibleProperties,
                        searchableProperties = result.searchableProperties
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
            Content.LIST_HOSTS, Content.DETAIL_PLANETS -> sortStellarHostProperty = action.sort
            Content.LIST_PLANETS, Content.DETAIL_HOSTS -> sortPlanetProperty = action.sort
        }
        updateState { it.copy(sortProperty = action.sort) }
    }

    private fun changeSortDirection(state: StellarExplorerState) {
        val ascending = !state.sortAscending
        Telemetry.info(tag = TAG, message = "Changed sort direction to ${if (ascending) "ascending" else "descending"}")
        updateState { it.copy(sortAscending = ascending) }
    }

    private fun changeVisibility(state: StellarExplorerState, action: StellarExplorerAction.ChangeVisibility): Job = launch(id = "changeVisibility") {
        Telemetry.info(tag = TAG, message = "Changing visibility for ${action.property}")
        val visibleProperties = state.visibleProperties.plusOrMinus(element = action.property).toPersistentList()
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_PLANETS -> visibleStellarHostProperties = visibleProperties
            Content.LIST_PLANETS, Content.DETAIL_HOSTS -> visiblePlanetProperties = visibleProperties
        }
        updateState { it.copy(visibleProperties = visibleProperties) }
    }

    private fun changeSearchable(state: StellarExplorerState, action: StellarExplorerAction.ChangeSearchable): Job = launch(id = "changeSearchable") {
        Telemetry.info(tag = TAG, message = "Changing searchable property ${action.property}")
        val searchProperties = state.searchableProperties.plusOrMinus(element = action.property).toPersistentList()
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_PLANETS -> searchableStellarHostProperties = searchProperties
            Content.LIST_PLANETS, Content.DETAIL_HOSTS -> searchablePlanetProperties = searchProperties
        }
        updateState { it.copy(searchableProperties = searchProperties) }
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
