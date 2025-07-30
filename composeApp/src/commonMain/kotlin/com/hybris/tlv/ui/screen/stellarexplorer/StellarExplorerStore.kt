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
    data class SaveIndex(val index: LazyListIndex): StellarExplorerAction
    data class Open(val stellarHost: StellarHost): StellarExplorerAction
    data class Search(val search: String): StellarExplorerAction
}

internal data class StellarExplorerState(
    val currentContent: Content? = null,
    val stellarHosts: List<StellarHost> = emptyList(),
    val listIndex: LazyListIndex = LazyListIndex(),
    val search: String = "",
    val filteredStellarHosts: List<StellarHost> = emptyList(),
    val selectedStellarHost: StellarHost? = null,
    val selectedPlanet: Planet? = null,
    val sortBy: String = "",
    val sortAscending: Boolean = true,
)

internal enum class Content {
    LIST_HOSTS,
    LIST_PLANETS,
    DETAIL_HOST,
    DETAIL_PLANET
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
        val stellarHosts = spaceUseCases.getExoplanets()
        updateState {
            it.copy(
                currentContent = Content.LIST_HOSTS,
                stellarHosts = stellarHosts,
                filteredStellarHosts = stellarHosts
            )
        }

        // calculate habitability in parallel
        launch {
            stellarHosts.forEach { stellarHost ->
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
            updateState { it.copy(stellarHosts = stellarHosts) }
        }
    }

    override fun reducer(state: StellarExplorerState, action: StellarExplorerAction) {
        when (action) {
            StellarExplorerAction.Back -> navigate(screen = Navigation.Screen.EXPLORE)
            //    when (state.currentContent) {
            //    null, Content.LIST -> navigate(screen = Navigation.Screen.EXPLORE)
            //    Content.DETAIL -> updateState {
            //        it.copy(
            //            currentContent = Content.LIST,
            //            selectedStellarHost = null
            //        )
            //    }
            //
            //    Content.LIST_HOSTS -> TODO()
            //    Content.LIST_PLANETS -> TODO()
            //    Content.DETAIL_HOSTS -> TODO()
            //    Content.DETAIL_PLANETS -> TODO()
            //}

            is StellarExplorerAction.SaveIndex -> updateState {
                it.copy(listIndex = action.index)
            }

            is StellarExplorerAction.Open -> launch {
                val selectedStellarHost = action.stellarHost.apply {

                }

                updateState {
                    it.copy(
                        currentContent = Content.DETAIL_PLANET,
                        selectedStellarHost = selectedStellarHost
                    )
                }
            }

            is StellarExplorerAction.Search -> launch {
                val filterStellarHosts = if (action.search.isNotBlank()) {
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
                        filteredStellarHosts = filterStellarHosts
                    )
                }
            }
        }
    }
}
