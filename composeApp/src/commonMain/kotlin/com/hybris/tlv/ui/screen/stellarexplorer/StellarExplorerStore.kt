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
    val visibleStellarHostProperties: List<StellarHostProperty> = StellarHostProperty.entries,
    val visiblePlanetProperties: List<PlanetProperty> = PlanetProperty.entries
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

    private fun searchStellarHosts(search: String, stellarHosts: List<StellarHost>): List<StellarHost> =
        if (search.isNotBlank()) {
            val searchLowercase = search.lowercase()
            stellarHosts.filter { stellarHost ->
                with(receiver = stellarHost) {
                    listOfNotNull(
                        id,
                        systemName,
                        name,
                        spectralType,
                        effectiveTemperature?.toString(),
                        radius?.toString(),
                        mass?.toString(),
                        metallicity?.toString(),
                        luminosity?.toString(),
                        gravity?.toString(),
                        age?.toString(),
                        density?.toString(),
                        rotationalVelocity?.toString(),
                        rotationalPeriod?.toString(),
                        distance?.toString(),
                        ra?.toString(),
                        dec?.toString(),
                        planets.size.toString()
                    )
                }.any { it.lowercase().contains(other = searchLowercase) }
            }
        } else stellarHosts

    private fun searchPlanets(search: String, planets: List<Planet>): List<Planet> =
        if (search.isNotBlank()) {
            val searchLowercase = search.lowercase()
            planets.filter { stellarHost ->
                with(receiver = stellarHost) {
                    listOfNotNull(
                        id,
                        name,
                        stellarHostId,
                        status.displayName,
                        orbitalPeriod?.toString(),
                        orbitAxis?.toString(),
                        radius?.toString(),
                        mass?.toString(),
                        density?.toString(),
                        eccentricity?.toString(),
                        insolationFlux?.toString(),
                        equilibriumTemperature?.toString(),
                        occultationDepth?.toString(),
                        inclination?.toString(),
                        obliquity?.toString(),
                        score?.habitabilityScore?.toString(),
                        score?.planetType?.displayName
                    )
                }.any { it.lowercase().contains(other = searchLowercase) }
            }
        } else planets

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
            is StellarExplorerAction.SaveIndex -> updateState {
                it.copy(listIndex = action.index)
            }

            StellarExplorerAction.ChangeView -> launch {
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

            is StellarExplorerAction.Search -> launch {
                when (state.currentContent) {
                    Content.LIST_HOSTS -> {
                        val filteredStellarHosts = searchStellarHosts(
                            search = action.search,
                            stellarHosts = state.stellarHosts
                        )
                        updateState { it.copy(filteredStellarHosts = filteredStellarHosts) }
                    }

                    Content.LIST_PLANETS -> {
                        val filteredPlanets = searchPlanets(
                            search = action.search,
                            planets = state.planets
                        )
                        updateState { it.copy(filteredPlanets = filteredPlanets) }
                    }

                    Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
                }
            }

            is StellarExplorerAction.OpenStellarHost -> {
                if (state.currentContent != Content.LIST_HOSTS) return
                updateState {
                    it.copy(
                        currentContent = Content.DETAIL_HOSTS,
                        selectedStellarHost = action.stellarHost,
                        filteredPlanets = action.stellarHost.planets
                    )
                }
            }

            is StellarExplorerAction.OpenPlanet -> {
                if (state.currentContent != Content.LIST_PLANETS) return
                updateState {
                    it.copy(
                        currentContent = Content.DETAIL_PLANETS,
                        selectedPlanet = action.planet,
                        filteredStellarHosts = state.stellarHosts.filter { stellarHost ->
                            stellarHost.id == action.planet.stellarHostId
                        }
                    )
                }
            }

            is StellarExplorerAction.SortStellarHosts -> launch {
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

            is StellarExplorerAction.SortPlanets -> launch {
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

            StellarExplorerAction.ChangeSortDirection -> launch {
                updateState { it.copy(sortAscending = !it.sortAscending) }.join()
                when (state.currentContent) {
                    Content.LIST_HOSTS -> send(action = StellarExplorerAction.SortStellarHosts(sort = state.sortStellarHostProperty))
                    Content.LIST_PLANETS -> send(action = StellarExplorerAction.SortPlanets(sort = state.sortPlanetProperty))
                    Content.DETAIL_HOSTS, Content.DETAIL_PLANETS -> {}
                }
            }

            is StellarExplorerAction.ChangeStellarHostsVisibility -> launch {
                val visibleStellarHostProperties = if (state.visibleStellarHostProperties.contains(element = action.property)) {
                    state.visibleStellarHostProperties.minus(element = action.property)
                } else state.visibleStellarHostProperties.plus(element = action.property)
                updateState { it.copy(visibleStellarHostProperties = visibleStellarHostProperties) }
            }

            is StellarExplorerAction.ChangePlanetVisibility -> launch {
                val visiblePlanetProperties = if (state.visiblePlanetProperties.contains(element = action.property)) {
                    state.visiblePlanetProperties.minus(element = action.property)
                } else state.visiblePlanetProperties.plus(element = action.property)
                updateState { it.copy(visiblePlanetProperties = visiblePlanetProperties) }
            }
        }
    }
}
