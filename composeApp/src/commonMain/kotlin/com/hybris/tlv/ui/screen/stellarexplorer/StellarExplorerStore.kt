package com.hybris.tlv.ui.screen.stellarexplorer

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.component.LazyListIndex
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.exoplanet.ExoplanetUseCases
import com.hybris.tlv.usecase.exoplanet.model.Params
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

internal sealed interface StellarExplorerAction {
    data object Back: StellarExplorerAction
    data object ChangeView: StellarExplorerAction
    data class SaveIndex(val index: LazyListIndex): StellarExplorerAction
    data class OpenStellarHost(val stellarHost: StellarHost): StellarExplorerAction
    data class OpenPlanet(val planet: Planet): StellarExplorerAction
    data class Search(val search: String): StellarExplorerAction
    data class Sort(val sort: String): StellarExplorerAction
    data object ChangeSortDirection: StellarExplorerAction
    data class ChangeVisibility(val property: String): StellarExplorerAction
}

internal data class StellarExplorerState(
    val currentContent: Content? = null,
    val stellarHosts: List<StellarHost> = emptyList(),
    val listIndex: LazyListIndex = LazyListIndex(),
    val search: String = "",
    val filteredStellarHosts: List<StellarHost> = emptyList(),
    val filteredPlanets: List<Planet> = emptyList(),
    val selectedStellarHost: StellarHost? = null,
    val selectedPlanet: Planet? = null,
    val stellarHostProperties: List<StellarHostProperty> = StellarHostProperty.entries,
    val planetProperties: List<PlanetProperty> = PlanetProperty.entries,
    val sortStellarHostProperty: StellarHostProperty = StellarHostProperty.DISTANCE,
    val sortPlanetProperty: PlanetProperty = PlanetProperty.NAME,
    val sortAscending: Boolean = true,
    val visibleProperties: List<String> = emptyList(),
)

internal enum class Content {
    LIST_HOSTS,
    DETAIL_HOSTS,
    LIST_PLANETS,
    DETAIL_PLANETS,
}

internal enum class StellarHostProperty {
    NAME,
    SYSTEM_NAME,
    PLANET_COUNT,
    SPECTRAL_TYPE,
    TEMPERATURE,
    RADIUS,
    MASS,
    METALLICITY,
    LUMINOSITY,
    GRAVITY,
    AGE,
    DENSITY,
    ROTATIONAL_VELOCITY,
    ROTATIONAL_PERIOD,
    DISTANCE,
    RA,
    DEC
}

internal enum class PlanetProperty {
    NAME,
    STATUS,
    HABITABILITY,
    ORBITAL_PERIOD,
    ORBIT_AXIS,
    RADIUS,
    MASS,
    DENSITY,
    ECCENTRICITY,
    INSOLATION_FLUX,
    TEMPERATURE,
    OCCULTATION_DEPTH,
    INCLINATION,
    OBLIQUITY
}

internal class StellarExplorerStore(
    dispatcher: Dispatcher,
    navigation: Navigation,
    initialState: StellarExplorerState,
    private val spaceUseCases: SpaceUseCases,
    private val exoplanetUseCases: ExoplanetUseCases
): Store<StellarExplorerAction, StellarExplorerState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val stellarHosts = spaceUseCases.getExoplanets().apply {
            forEach { stellarHost ->
                stellarHost.planets.forEach { planet ->
                    planet.habitability = exoplanetUseCases.calculateHabitability(
                        Params(
                            stellarHost = Params.StellarHost(
                                spectralType = stellarHost.spectralType,
                                effectiveTemperature = stellarHost.effectiveTemperature,
                                radius = stellarHost.radius,
                                mass = stellarHost.mass,
                                metallicity = stellarHost.metallicity,
                                luminosity = stellarHost.luminosity,
                                gravity = stellarHost.gravity,
                                age = stellarHost.age,
                                density = stellarHost.density,
                                rotationalVelocity = stellarHost.rotationalVelocity,
                                rotationalPeriod = stellarHost.rotationalPeriod
                            ),
                            planet = Params.Planet(
                                orbitalPeriod = planet.orbitalPeriod,
                                orbitAxis = planet.orbitAxis,
                                radius = planet.radius,
                                mass = planet.mass,
                                density = planet.density,
                                eccentricity = planet.eccentricity,
                                insolationFlux = planet.insolationFlux,
                                equilibriumTemperature = planet.equilibriumTemperature,
                                occultationDepth = planet.occultationDepth,
                                inclination = planet.inclination,
                                obliquity = planet.obliquity
                            )
                        )
                    )
                }
            }
        }
        val planets = stellarHosts.map { it.planets }.flatten()
        updateState {
            it.copy(
                currentContent = Content.LIST_HOSTS,
                stellarHosts = stellarHosts,
                filteredStellarHosts = stellarHosts,
                filteredPlanets = planets
            )
        }
    }

    override fun reducer(state: StellarExplorerState, action: StellarExplorerAction) {
        when (action) {
            StellarExplorerAction.Back -> when (state.currentContent) {
                null,
                Content.LIST_HOSTS,
                Content.LIST_PLANETS -> navigate(screen = Navigation.Screen.EXPLORE)

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

            StellarExplorerAction.ChangeView -> when (state.currentContent) {
                null,
                Content.LIST_HOSTS,
                Content.DETAIL_HOSTS -> updateState {
                    it.copy(
                        currentContent = Content.LIST_PLANETS,
                        listIndex = LazyListIndex()
                    )
                }

                Content.LIST_PLANETS,
                Content.DETAIL_PLANETS -> updateState {
                    it.copy(
                        currentContent = Content.LIST_HOSTS,
                        listIndex = LazyListIndex()
                    )
                }
            }

            is StellarExplorerAction.SaveIndex -> updateState {
                it.copy(listIndex = action.index)
            }

            is StellarExplorerAction.OpenStellarHost -> {
                if (state.currentContent != Content.LIST_HOSTS) return
                updateState {
                    it.copy(
                        currentContent = Content.DETAIL_HOSTS,
                        selectedStellarHost = action.stellarHost
                    )
                }
            }

            is StellarExplorerAction.OpenPlanet -> {
                if (state.currentContent != Content.LIST_PLANETS) return
                updateState {
                    it.copy(
                        currentContent = Content.DETAIL_PLANETS,
                        selectedPlanet = action.planet
                    )
                }
            }

            is StellarExplorerAction.Search -> launch {
                when (state.currentContent) {
                    Content.LIST_HOSTS -> {
                        val filteredStellarHosts = if (action.search.isNotBlank()) {
                            val searchLowercase = action.search.lowercase()
                            state.stellarHosts.filter { stellarHost ->
                                with(receiver = stellarHost) {
                                    listOfNotNull(
                                        id,
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
                                        dec?.toString()
                                    )
                                }.any { it.lowercase().contains(other = searchLowercase) }
                            }
                        } else state.stellarHosts
                        updateState {
                            it.copy(
                                search = action.search,
                                filteredStellarHosts = filteredStellarHosts
                            )
                        }
                    }
                    Content.DETAIL_HOSTS -> TODO()
                    Content.LIST_PLANETS -> TODO()
                    Content.DETAIL_PLANETS -> TODO()
                    null -> TODO()
                }
            }

            is StellarExplorerAction.Sort -> launch {
                //val filteredStellarHosts = with(receiver = state.filteredStellarHosts) {
                //    when (action.sort) {
                //        "id" -> sortedWith(comparator = compareBy<StellarHost, Any?>(comparator = nullsLast()) { it.id })
                //        "name" -> sortedWith(comparator = compareBy<StellarHost, Any?>(comparator = nullsLast()) { it.id })
                //    }.thenBy { it.id })
                //}
                //updateState {
                //    it.copy(
                //        filteredStellarHosts = filteredStellarHosts
                //    )
                //}
            }

            StellarExplorerAction.ChangeSortDirection -> TODO()
            is StellarExplorerAction.ChangeVisibility -> TODO()
        }
    }
}
