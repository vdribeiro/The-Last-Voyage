package com.hybris.tlv.ui.screen.stellarexplorer

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.exoplanet.ExoplanetUseCases
import com.hybris.tlv.usecase.exoplanet.model.Params
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.model.StellarHost

internal sealed interface StellarExplorerAction {
    data object Back: StellarExplorerAction
    data class SaveIndex(val index: Int, val scrollOffset: Int): StellarExplorerAction
    data class Open(val stellarHost: StellarHost): StellarExplorerAction
    data class Search(val search: String): StellarExplorerAction
}

internal data class StellarExplorerState(
    val currentContent: Content? = null,
    val listIndex: Int = 0,
    val listScrollOffset: Int = 0,
    val search: String = "",
    val stellarHosts: List<StellarHost> = emptyList(),
    val filteredStellarHosts: List<StellarHost> = emptyList(),
    val selectedStellarHost: StellarHost? = null
)

internal enum class Content {
    LIST,
    DETAIL
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
                currentContent = Content.LIST,
                stellarHosts = stellarHosts,
                filteredStellarHosts = stellarHosts
            )
        }
    }

    override fun reducer(state: StellarExplorerState, action: StellarExplorerAction) {
        when (action) {
            StellarExplorerAction.Back -> when (state.currentContent) {
                null, Content.LIST -> navigate(screen = Navigation.Screen.EXPLORE)
                Content.DETAIL -> updateState {
                    it.copy(
                        currentContent = Content.LIST,
                        selectedStellarHost = null
                    )
                }
            }

            is StellarExplorerAction.SaveIndex -> updateState {
                it.copy(
                    listIndex = action.index,
                    listScrollOffset = action.scrollOffset
                )
            }

            is StellarExplorerAction.Open -> launch {
                val selectedStellarHost = action.stellarHost.apply {
                    planets.forEach { planet ->
                        planet.habitability = exoplanetUseCases.calculateHabitability(
                            Params(
                                stellarHost = Params.StellarHost(
                                    spectralType = action.stellarHost.spectralType,
                                    effectiveTemperature = action.stellarHost.effectiveTemperature,
                                    radius = action.stellarHost.radius,
                                    mass = action.stellarHost.mass,
                                    metallicity = action.stellarHost.metallicity,
                                    luminosity = action.stellarHost.luminosity,
                                    gravity = action.stellarHost.gravity,
                                    age = action.stellarHost.age,
                                    density = action.stellarHost.density,
                                    rotationalVelocity = action.stellarHost.rotationalVelocity,
                                    rotationalPeriod = action.stellarHost.rotationalPeriod
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

                updateState {
                    it.copy(
                        currentContent = Content.DETAIL,
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
