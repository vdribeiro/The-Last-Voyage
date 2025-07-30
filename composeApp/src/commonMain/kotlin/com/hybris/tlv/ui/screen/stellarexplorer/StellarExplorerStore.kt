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
    val stellarHosts: List<StellarHost> = emptyList(),
    val listIndex: Int = 0,
    val listScrollOffset: Int = 0,
    val search: String = "",
    val filteredStellarHosts: List<StellarHost> = emptyList(),
    val selectedStellarHost: StellarHost? = null,
    val sortBy: Property = Property.STELLAR_HOST_DISTANCE,
    val sortDirection: SortDirection = SortDirection.ASC,
    val visibleProperties: Set<Property> = Property.entries.toSet()
)

internal enum class Content {
    LIST_HOSTS,
    LIST_PLANETS,
    DETAIL_HOSTS,
    DETAIL_PLANETS
}

internal enum class SortDirection { ASC, DESC }

internal enum class Property(val displayName: String) {
    STELLAR_HOST_SYSTEM_NAME("stellar_host_system_name"),
    STELLAR_HOST_PLANET_COUNT("stellar_host_planet_count"),
    STELLAR_HOST_SPECTRAL_TYPE("stellar_host_spectral_type"),
    STELLAR_HOST_TEMPERATURE("stellar_host_temperature"),
    STELLAR_HOST_RADIUS("stellar_host_spectral_radius"),
    STELLAR_HOST_MASS("stellar_host_spectral_mass"),
    STELLAR_HOST_METALLICITY("stellar_host_spectral_metallicity"),
    STELLAR_HOST_LUMINOSITY("stellar_host_spectral_luminosity"),
    STELLAR_HOST_GRAVITY("stellar_host_spectral_gravity"),
    STELLAR_HOST_AGE("stellar_host_spectral_age"),
    STELLAR_HOST_DENSITY("stellar_host_spectral_density"),
    STELLAR_HOST_ROTATIONAL_VELOCITY("stellar_host_spectral_rotational_velocity"),
    STELLAR_HOST_ROTATIONAL_PERIOD("stellar_host_spectral_rotational_period"),
    STELLAR_HOST_DISTANCE("stellar_host_distance"),
    STELLAR_HOST_RA("stellar_host_spectral_ra"),
    STELLAR_HOST_DEC("stellar_host_spectral_dec"),
    PLANET_STATUS("planet_status"),
    PLANET_STATUS_CONFIRMED("planet_status_confirmed"),
    PLANET_STATUS_CANDIDATE("planet_status_candidate"),
    PLANET_STATUS_FALSE("planet_status_false"),
    PLANET_HABITABILITY("planet_habitability"),
    PLANET_ORBITAL_PERIOD("planet_orbital_period"),
    PLANET_ORBIT_AXIS("planet_orbit_axis"),
    PLANET_RADIUS("planet_radius"),
    PLANET_MASS("planet_mass"),
    PLANET_DENSITY("planet_density"),
    PLANET_ECCENTRICITY("planet_eccentricity"),
    PLANET_INSOLATION_FLUX("planet_insolation_flux"),
    PLANET_TEMPERATURE("planet_temperature"),
    PLANET_OCCULTATION_DEPTH("planet_occultation_depth"),
    PLANET_INCLINATION("planet_inclination"),
    PLANET_OBLIQUITY("planet_obliquity");
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
