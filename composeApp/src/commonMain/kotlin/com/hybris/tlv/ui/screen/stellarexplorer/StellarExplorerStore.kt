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
    @VisibleForTesting
    @Volatile
    internal var sortStellarHostProperty: String = TranslationCache.get(key = StellarHostProperty.DISTANCE.displayName)
    @VisibleForTesting
    @Volatile
    internal var sortPlanetProperty: String = TranslationCache.get(key = PlanetProperty.HABITABILITY.displayName)
    @VisibleForTesting
    @Volatile
    internal var visibleStellarHostProperties: List<String> = listOf(
        TranslationCache.get(key = StellarHostProperty.NAME.displayName),
        TranslationCache.get(key = StellarHostProperty.SYSTEM_NAME.displayName),
        TranslationCache.get(key = StellarHostProperty.PLANET_COUNT.displayName),
        TranslationCache.get(key = StellarHostProperty.SPECTRAL_TYPE.displayName),
        TranslationCache.get(key = StellarHostProperty.TEMPERATURE.displayName),
        TranslationCache.get(key = StellarHostProperty.RADIUS.displayName),
        TranslationCache.get(key = StellarHostProperty.MASS.displayName),
        TranslationCache.get(key = StellarHostProperty.METALLICITY.displayName),
        TranslationCache.get(key = StellarHostProperty.LUMINOSITY.displayName),
        TranslationCache.get(key = StellarHostProperty.GRAVITY.displayName),
        TranslationCache.get(key = StellarHostProperty.AGE.displayName),
        TranslationCache.get(key = StellarHostProperty.DENSITY.displayName),
        TranslationCache.get(key = StellarHostProperty.ROTATIONAL_VELOCITY.displayName),
        TranslationCache.get(key = StellarHostProperty.ROTATIONAL_PERIOD.displayName),
        TranslationCache.get(key = StellarHostProperty.DISTANCE.displayName),
        TranslationCache.get(key = StellarHostProperty.RA.displayName),
        TranslationCache.get(key = StellarHostProperty.DEC.displayName),
    )
    @VisibleForTesting
    @Volatile
    internal var visiblePlanetProperties: List<String> = listOf(
        TranslationCache.get(key = PlanetProperty.NAME.displayName),
        TranslationCache.get(key = PlanetProperty.STATUS.displayName),
        TranslationCache.get(key = PlanetProperty.HABITABILITY.displayName),
        TranslationCache.get(key = PlanetProperty.CONFIDENCE.displayName),
        TranslationCache.get(key = PlanetProperty.TYPE.displayName),
        TranslationCache.get(key = PlanetProperty.ORBITAL_PERIOD.displayName),
        TranslationCache.get(key = PlanetProperty.ORBIT_AXIS.displayName),
        TranslationCache.get(key = PlanetProperty.RADIUS.displayName),
        TranslationCache.get(key = PlanetProperty.MASS.displayName),
        TranslationCache.get(key = PlanetProperty.DENSITY.displayName),
        TranslationCache.get(key = PlanetProperty.ECCENTRICITY.displayName),
        TranslationCache.get(key = PlanetProperty.INSOLATION_FLUX.displayName),
        TranslationCache.get(key = PlanetProperty.TEMPERATURE.displayName),
        TranslationCache.get(key = PlanetProperty.OCCULTATION_DEPTH.displayName),
        TranslationCache.get(key = PlanetProperty.INCLINATION.displayName),
        TranslationCache.get(key = PlanetProperty.OBLIQUITY.displayName),
    )
    @VisibleForTesting
    @Volatile
    internal var searchableStellarHostProperties: List<String> = listOf(TranslationCache.get(key = StellarHostProperty.NAME.displayName))
    @VisibleForTesting
    @Volatile
    internal var searchablePlanetProperties: List<String> = listOf(TranslationCache.get(key = PlanetProperty.NAME.displayName))

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
                    searchProperties = state.searchProperties
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
//                            criteriaCombine.stellarHosts.searchAndSortStellarHosts(
//                                search = criteriaCombine.criteria.search,
//                                searchable = criteriaCombine.criteria.searchProperties,
//                                sort = criteriaCombine.criteria.sortProperty,
//                                ascending = criteriaCombine.criteria.sortAscending,
//                                visible = criteriaCombine.criteria.visibleProperties
//                            )
                            stellarHosts = criteriaCombine.stellarHosts.map { it.toExoplanetsHost() }.toPersistentList(),
                            planets = persistentListOf()
                        )

                        Content.DETAIL_HOSTS -> Exoplanets(
                            stellarHosts = listOfNotNull(element = selectedStellarHost).toPersistentList(),
                            planets = criteriaCombine.stellarHosts.find { it.id == selectedStellarHost?.id }?.planets?.map { it.toExoplanetsPlanet() }.orEmpty().toPersistentList()
                        )

                        Content.LIST_PLANETS -> Exoplanets(
                            stellarHosts = persistentListOf(),
                            planets = criteriaCombine.stellarHosts.flatMap { it.planets }.map { it.toExoplanetsPlanet() }.toPersistentList()
                        )

                        Content.DETAIL_PLANETS -> Exoplanets(
                            stellarHosts = listOfNotNull(element = criteriaCombine.stellarHosts.find { it.id == selectedPlanet?.stellarHostId }?.toExoplanetsHost()).toPersistentList(),
                            planets = listOfNotNull(element = selectedPlanet).toPersistentList()
                        )
                    },
                    properties = when (criteriaCombine.criteria.currentContent) {
                        Content.LIST_HOSTS, Content.DETAIL_PLANETS -> StellarHostProperty.entries.map { criteriaCombine.translations.getTranslation(key = it.displayName) }.toPersistentList()
                        Content.LIST_PLANETS, Content.DETAIL_HOSTS -> PlanetProperty.entries.map { criteriaCombine.translations.getTranslation(key = it.displayName) }.toPersistentList()
                    }
                )
            }
            .flowOn(context = Dispatcher.Default)
            .observe(id = "stellarHosts") { result ->
                updateState {
                    it.copy(
                        exoplanets = result.exoplanets,
                        properties = result.properties,
//                        sortProperty = "TODO",
//                        visibleProperties = persistentListOf(),
//                        searchProperties = persistentListOf()
                    )
                }
            }
    }

    private fun changeView(state: StellarExplorerState): Job = launch(id = "changeView") {
        Telemetry.info(tag = TAG, message = "Changed view")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_HOSTS -> {
                val properties = StellarHostProperty.entries.map { TranslationCache.get(key = it.displayName) }.toPersistentList()
                val visibleProperties = visibleStellarHostProperties.toPersistentList()
                val searchProperties = searchableStellarHostProperties.toPersistentList()
                updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        search = "",
                        properties = properties,
                        sortProperty = sortStellarHostProperty,
                        visibleProperties = visibleProperties,
                        searchProperties = searchProperties
                    )
                }
            }

            Content.LIST_PLANETS, Content.DETAIL_PLANETS -> {
                val properties = PlanetProperty.entries.map { TranslationCache.get(key = it.displayName) }.toPersistentList()
                val visibleProperties = visiblePlanetProperties.toPersistentList()
                val searchProperties = searchablePlanetProperties.toPersistentList()
                updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        search = "",
                        properties = properties,
                        sortProperty = sortPlanetProperty,
                        visibleProperties = visibleProperties,
                        searchProperties = searchProperties
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
        val properties = PlanetProperty.entries.map { TranslationCache.get(key = it.displayName) }.toPersistentList()
        val visibleProperties = visiblePlanetProperties.toPersistentList()
        val searchProperties = searchablePlanetProperties.toPersistentList()
        updateState {
            it.copy(
                currentContent = Content.DETAIL_HOSTS,
                search = "",
                properties = properties,
                sortProperty = sortPlanetProperty,
                visibleProperties = visibleProperties,
                searchProperties = searchProperties
            )
        }
    }

    private fun openPlanet(action: StellarExplorerAction.OpenPlanet): Job = launch(id = "openPlanet") {
        Telemetry.info(tag = TAG, message = "Opening planet ${action.planet.id}")
        selectedPlanet = action.planet
        val properties = StellarHostProperty.entries.map { TranslationCache.get(key = it.displayName) }.toPersistentList()
        val visibleProperties = visibleStellarHostProperties.toPersistentList()
        val searchProperties = searchableStellarHostProperties.toPersistentList()
        updateState {
            it.copy(
                currentContent = Content.DETAIL_PLANETS,
                search = "",
                properties = properties,
                sortProperty = sortStellarHostProperty,
                visibleProperties = visibleProperties,
                searchProperties = searchProperties
            )
        }
    }

    private fun sort(state: StellarExplorerState, action: StellarExplorerAction.Sort) {
        Telemetry.info(tag = TAG, message = "Sorting by ${action.sort}")
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_HOSTS -> sortStellarHostProperty = action.sort
            Content.LIST_PLANETS, Content.DETAIL_PLANETS -> sortPlanetProperty = action.sort
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
            Content.LIST_HOSTS, Content.DETAIL_HOSTS -> visibleStellarHostProperties = visibleProperties
            Content.LIST_PLANETS, Content.DETAIL_PLANETS -> visiblePlanetProperties = visibleProperties
        }
        updateState { it.copy(visibleProperties = visibleProperties) }
    }

    private fun changeSearchable(state: StellarExplorerState, action: StellarExplorerAction.ChangeSearchable): Job = launch(id = "changeSearchable") {
        Telemetry.info(tag = TAG, message = "Changing searchable property ${action.property}")
        val searchProperties = state.searchProperties.plusOrMinus(element = action.property).toPersistentList()
        when (state.currentContent) {
            Content.LIST_HOSTS, Content.DETAIL_HOSTS -> searchableStellarHostProperties = searchProperties
            Content.LIST_PLANETS, Content.DETAIL_PLANETS -> searchablePlanetProperties = searchProperties
        }
        updateState { it.copy(searchProperties = searchProperties) }
    }

    private fun navigateBack(state: StellarExplorerState) {
        when (state.currentContent) {
            Content.LIST_HOSTS,
            Content.LIST_PLANETS -> navigateBack()

            Content.DETAIL_HOSTS -> {
                selectedStellarHost = null
                val properties = StellarHostProperty.entries.map { TranslationCache.get(key = it.displayName) }.toPersistentList()
                val visibleProperties = visibleStellarHostProperties.toPersistentList()
                val searchProperties = searchableStellarHostProperties.toPersistentList()
                updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        search = "",
                        properties = properties,
                        sortProperty = sortStellarHostProperty,
                        visibleProperties = visibleProperties,
                        searchProperties = searchProperties
                    )
                }
            }

            Content.DETAIL_PLANETS -> {
                selectedPlanet = null
                val properties = PlanetProperty.entries.map { TranslationCache.get(key = it.displayName) }.toPersistentList()
                val visibleProperties = visiblePlanetProperties.toPersistentList()
                val searchProperties = searchablePlanetProperties.toPersistentList()
                updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        search = "",
                        properties = properties,
                        sortProperty = sortPlanetProperty,
                        visibleProperties = visibleProperties,
                        searchProperties = searchProperties
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
