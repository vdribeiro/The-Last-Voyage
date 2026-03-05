package com.hybris.tlv.ui.screen.stellarexplorer

import kotlin.concurrent.Volatile
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import androidx.compose.foundation.lazy.LazyListState
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.space.SpaceUseCases
import com.hybris.tlv.domain.usecase.space.formula.Habitability
import com.hybris.tlv.domain.usecase.space.model.Formula
import com.hybris.tlv.domain.usecase.space.model.StellarHost
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.domain.usecase.translation.TranslationCache.getTranslation
import com.hybris.tlv.test.VisibleForTesting
import com.hybris.tlv.ui.screen.Store

internal class StellarExplorerStore(
    private val spaceUseCases: SpaceUseCases,
): Store<StellarExplorerState, StellarExplorerAction>(
    initialState = StellarExplorerState()
) {
    private val formula: Formula = Formula()
    private val stellarHostsFlow: MutableStateFlow<List<StellarHost>> = MutableStateFlow(value = emptyList())
    @VisibleForTesting
    @Volatile
    internal var selectedStellarHost: Exoplanets.Host? = null
    @VisibleForTesting
    @Volatile
    internal var selectedPlanet: Exoplanets.Planet? = null
    private val sortStellarHostPropertyDefault = StellarHostProperty.DISTANCE
    @VisibleForTesting
    @Volatile
    internal var sortStellarHostProperty: String = sortStellarHostPropertyDefault.name
    private val sortPlanetPropertyDefault = PlanetProperty.HABITABILITY
    @VisibleForTesting
    @Volatile
    internal var sortPlanetProperty: String = sortPlanetPropertyDefault.name
    private val visibleStellarHostPropertiesDefault = listOf(
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
    private val visiblePlanetPropertiesDefault = listOf(
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
    private val searchableStellarHostPropertiesDefault = listOf(StellarHostProperty.NAME)
    @VisibleForTesting
    @Volatile
    internal var searchableStellarHostProperties: List<String> = searchableStellarHostPropertiesDefault.map { it.name }
    private val searchablePlanetPropertiesDefault = listOf(PlanetProperty.NAME)
    @VisibleForTesting
    @Volatile
    internal var searchablePlanetProperties: List<String> = searchablePlanetPropertiesDefault.map { it.name }

    init {
        setup()
    }

    private fun setup() {
        Telemetry.info(tag = TAG, message = "Setup")

        observeExoplanets()

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeExoplanets() {
        spaceUseCases.observeExoplanets()
            .map { stellarHosts ->
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
            .observe(id = "observeExoplanets") { stellarHosts ->
                stellarHostsFlow.value = stellarHosts
                updateState { it.copy(loading = false) }
            }

        val criteriaFlow = stateFlow
            .map { state ->
                FilterExoplanetsCriteria(
                    currentContent = state.currentContent,
                    search = state.search,
                    sortProperty = state.sortProperty,
                    sortAscending = state.sortAscending,
                    visibleProperties = state.visibleProperties,
                    searchableProperties = state.searchableProperties
                )
            }
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
            .mapLatest { criteriaCombine ->
                FilterExoplanetsCriteriaResult(
                    exoplanets = when (criteriaCombine.criteria.currentContent) {
                        Content.LIST_HOSTS -> Exoplanets(
                            stellarHosts = criteriaCombine.stellarHosts.searchAndSortStellarHosts(
                                search = criteriaCombine.criteria.search,
                                searchable = criteriaCombine.criteria.searchableProperties.mapNotNull { StellarHostProperty.fromString(name = it) }.ifEmpty { searchableStellarHostPropertiesDefault },
                                sort = StellarHostProperty.fromString(name = criteriaCombine.criteria.sortProperty) ?: sortStellarHostPropertyDefault,
                                ascending = criteriaCombine.criteria.sortAscending,
                                visible = criteriaCombine.criteria.visibleProperties.mapNotNull { StellarHostProperty.fromString(name = it) }.ifEmpty { visibleStellarHostPropertiesDefault }
                            ).toPersistentList(),
                            planets = persistentListOf()
                        )

                        Content.DETAIL_HOSTS -> Exoplanets(
                            stellarHosts = listOfNotNull(element = selectedStellarHost).toPersistentList(),
                            planets = criteriaCombine.stellarHosts.find { it.id == selectedStellarHost?.id }?.planets.orEmpty().searchAndSortPlanets(
                                search = criteriaCombine.criteria.search,
                                searchable = criteriaCombine.criteria.searchableProperties.mapNotNull { PlanetProperty.fromString(name = it) }.ifEmpty { searchablePlanetPropertiesDefault },
                                sort = PlanetProperty.fromString(name = criteriaCombine.criteria.sortProperty) ?: sortPlanetPropertyDefault,
                                ascending = criteriaCombine.criteria.sortAscending,
                                visible = criteriaCombine.criteria.visibleProperties.mapNotNull { PlanetProperty.fromString(name = it) }.ifEmpty { visiblePlanetPropertiesDefault }
                            ).toPersistentList()
                        )

                        Content.LIST_PLANETS -> Exoplanets(
                            stellarHosts = persistentListOf(),
                            planets = criteriaCombine.stellarHosts.flatMap { it.planets }.searchAndSortPlanets(
                                search = criteriaCombine.criteria.search,
                                searchable = criteriaCombine.criteria.searchableProperties.mapNotNull { PlanetProperty.fromString(name = it) }.ifEmpty { searchablePlanetPropertiesDefault },
                                sort = PlanetProperty.fromString(name = criteriaCombine.criteria.sortProperty) ?: sortPlanetPropertyDefault,
                                ascending = criteriaCombine.criteria.sortAscending,
                                visible = criteriaCombine.criteria.visibleProperties.mapNotNull { PlanetProperty.fromString(name = it) }.ifEmpty { visiblePlanetPropertiesDefault }
                            ).toPersistentList()
                        )

                        Content.DETAIL_PLANETS -> Exoplanets(
                            stellarHosts = listOfNotNull(criteriaCombine.stellarHosts.find { it.id == selectedPlanet?.stellarHostId }).searchAndSortStellarHosts(
                                search = criteriaCombine.criteria.search,
                                searchable = criteriaCombine.criteria.searchableProperties.mapNotNull { StellarHostProperty.fromString(name = it) }.ifEmpty { searchableStellarHostPropertiesDefault },
                                sort = StellarHostProperty.fromString(name = criteriaCombine.criteria.sortProperty) ?: sortStellarHostPropertyDefault,
                                ascending = criteriaCombine.criteria.sortAscending,
                                visible = criteriaCombine.criteria.visibleProperties.mapNotNull { StellarHostProperty.fromString(name = it) }.ifEmpty { visibleStellarHostPropertiesDefault }
                            ).toPersistentList(),
                            planets = listOfNotNull(element = selectedPlanet).toPersistentList()
                        )
                    },
                    properties = when (criteriaCombine.criteria.currentContent) {
                        Content.LIST_HOSTS, Content.DETAIL_PLANETS -> StellarHostProperty.entries.map { it.name to criteriaCombine.translations.getTranslation(key = it.displayName) }.toPersistentList()
                        Content.LIST_PLANETS, Content.DETAIL_HOSTS -> PlanetProperty.entries.map { it.name to criteriaCombine.translations.getTranslation(key = it.displayName) }.toPersistentList()
                    },
                    visibleProperties = when (criteriaCombine.criteria.currentContent) {
                        Content.LIST_HOSTS, Content.DETAIL_PLANETS -> visibleStellarHostProperties.toPersistentList()
                        Content.LIST_PLANETS, Content.DETAIL_HOSTS -> visiblePlanetProperties.toPersistentList()
                    },
                    searchableProperties = when (criteriaCombine.criteria.currentContent) {
                        Content.LIST_HOSTS, Content.DETAIL_PLANETS -> visibleStellarHostProperties.toPersistentList()
                        Content.LIST_PLANETS, Content.DETAIL_HOSTS -> searchablePlanetProperties.toPersistentList()
                    }
                )
            }
            .flowOn(context = Dispatcher.Default)
            .observe(id = "filterExoplanets") { result ->
                updateState {
                    it.copy(
                        exoplanets = result.exoplanets,
                        properties = result.properties,
                        sortProperty = sortStellarHostProperty,
                        visibleProperties = result.visibleProperties,
                        searchableProperties = result.searchableProperties
                    )
                }
            }
    }

    private fun changeView(state: StellarExplorerState): Job = launch(id = "changeView") {
        Telemetry.info(tag = TAG, message = "Changed view")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_HOSTS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        listState = LazyListState(),
                        search = "",
                    )
                }
            }

            Content.LIST_PLANETS, Content.DETAIL_PLANETS -> {
                updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        listState = LazyListState(),
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
                listState = action.listState,
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
                listState = action.listState,
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
