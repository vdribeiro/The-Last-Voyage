package com.hybris.tlv.ui.screen.stellarexplorer

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import androidx.compose.foundation.lazy.LazyListState
import com.hybris.tlv.domain.usecase.space.model.Planet
import com.hybris.tlv.domain.usecase.space.model.StellarHost

internal sealed interface StellarExplorerAction {
    data object Back: StellarExplorerAction
    data class SaveListState(val listState: LazyListState): StellarExplorerAction
    data object ChangeView: StellarExplorerAction
    data class Search(val search: String): StellarExplorerAction
    data class OpenStellarHost(val stellarHost: StellarHost): StellarExplorerAction
    data class OpenPlanet(val planet: Planet): StellarExplorerAction
    data class SortStellarHosts(val sort: StellarHostProperty): StellarExplorerAction
    data class SortPlanets(val sort: PlanetProperty): StellarExplorerAction
    data object ChangeSortDirection: StellarExplorerAction
    data class ChangeStellarHostsVisibility(val property: StellarHostProperty): StellarExplorerAction
    data class ChangePlanetVisibility(val property: PlanetProperty): StellarExplorerAction
    data class ChangeStellarHostsSearchable(val property: StellarHostProperty): StellarExplorerAction
    data class ChangePlanetSearchable(val property: PlanetProperty): StellarExplorerAction
}

internal data class StellarExplorerState(
    val loading: Boolean = true,
    val currentContent: Content = Content.LIST_HOSTS,
    val listState: LazyListState = LazyListState(),
    val stellarHosts: ImmutableList<StellarHost> = persistentListOf(),
    val planets: ImmutableList<Planet> = persistentListOf(),
    val selectedStellarHost: StellarHost? = null,
    val selectedPlanet: Planet? = null,
    val search: String = "",
    val sortStellarHostProperty: StellarHostProperty = StellarHostProperty.DISTANCE,
    val sortPlanetProperty: PlanetProperty = PlanetProperty.HABITABILITY,
    val sortAscending: Boolean = true,
    val visibleStellarHostProperties: ImmutableSet<StellarHostProperty> = persistentSetOf(
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
        StellarHostProperty.DEC,
    ),
    val visiblePlanetProperties: ImmutableSet<PlanetProperty> = persistentSetOf(
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
        PlanetProperty.OBLIQUITY,
    ),
    val searchableStellarHostProperties: ImmutableSet<StellarHostProperty> = persistentSetOf(StellarHostProperty.NAME),
    val searchablePlanetProperties: ImmutableSet<PlanetProperty> = persistentSetOf(PlanetProperty.NAME),
)

internal enum class Content {
    LIST_HOSTS,
    DETAIL_HOSTS,
    LIST_PLANETS,
    DETAIL_PLANETS,
}

internal enum class StellarHostProperty(val displayName: String) {
    NAME(displayName = "stellar_host_name"),
    SYSTEM_NAME(displayName = "stellar_host_system_name"),
    PLANET_COUNT(displayName = "stellar_host_planet_count"),
    SPECTRAL_TYPE(displayName = "stellar_host_type"),
    TEMPERATURE(displayName = "stellar_host_temperature"),
    RADIUS(displayName = "stellar_host_radius"),
    MASS(displayName = "stellar_host_mass"),
    METALLICITY(displayName = "stellar_host_metallicity"),
    LUMINOSITY(displayName = "stellar_host_luminosity"),
    GRAVITY(displayName = "stellar_host_gravity"),
    AGE(displayName = "stellar_host_age"),
    DENSITY(displayName = "stellar_host_density"),
    ROTATIONAL_VELOCITY(displayName = "stellar_host_rotational_velocity"),
    ROTATIONAL_PERIOD(displayName = "stellar_host_rotational_period"),
    DISTANCE(displayName = "stellar_host_distance"),
    RA(displayName = "stellar_host_ra"),
    DEC(displayName = "stellar_host_dec"),
    SPECTRAL_TYPE_SCORE(displayName = "stellar_host_spectral_type_score"),
    MASS_SCORE(displayName = "stellar_host_mass_score"),
    AGE_SCORE(displayName = "stellar_host_age_score"),
    ACTIVITY_SCORE(displayName = "stellar_host_activity_score"),
    ROTATIONAL_PERIOD_SCORE(displayName = "stellar_host_rotational_period_score"),
    GRAVITY_SCORE(displayName = "stellar_host_gravity_score"),
    METALLICITY_SCORE(displayName = "stellar_host_metallicity_score"),
    EFFECTIVE_TEMPERATURE_SCORE(displayName = "stellar_host_effective_temperature_score")
}

internal enum class PlanetProperty(val displayName: String) {
    NAME(displayName = "planet_name"),
    STATUS(displayName = "planet_status"),
    HABITABILITY(displayName = "planet_habitability"),
    CONFIDENCE(displayName = "planet_confidence"),
    TYPE(displayName = "planet_type"),
    ORBITAL_PERIOD(displayName = "planet_orbital_period"),
    ORBIT_AXIS(displayName = "planet_orbit_axis"),
    RADIUS(displayName = "planet_radius"),
    MASS(displayName = "planet_mass"),
    DENSITY(displayName = "planet_density"),
    ECCENTRICITY(displayName = "planet_eccentricity"),
    INSOLATION_FLUX(displayName = "planet_insolation_flux"),
    TEMPERATURE(displayName = "planet_temperature"),
    OCCULTATION_DEPTH(displayName = "planet_occultation_depth"),
    INCLINATION(displayName = "planet_inclination"),
    OBLIQUITY(displayName = "planet_obliquity"),
    ROCHE_SCORE(displayName = "planet_roche_score"),
    HABITABLE_ZONE_KOPPARAPU_SCORE(displayName = "planet_habitable_zone_kopparapu_score"),
    HABITABLE_ZONE_KASTING_SCORE(displayName = "planet_habitable_zone_kasting_score"),
    RADIUS_SCORE(displayName = "planet_radius_score"),
    MASS_SCORE(displayName = "planet_mass_score"),
    TELLURICITY_SCORE(displayName = "planet_telluricity_score"),
    ECCENTRICITY_SCORE(displayName = "planet_eccentricity_score"),
    TEMPERATURE_SCORE(displayName = "planet_temperature_score"),
    OBLIQUITY_SCORE(displayName = "planet_obliquity_score"),
    ESI_SCORE(displayName = "planet_esi_score"),
    PROTECTION_SCORE(displayName = "planet_protection_score"),
    TIDAL_LOCKING_SCORE(displayName = "planet_tidal_locking_score")
}

internal data class FilterCriteria(
    val currentContent: Content,
    val search: String,
    val sortStellarHostProperty: StellarHostProperty,
    val sortPlanetProperty: PlanetProperty,
    val sortAscending: Boolean,
    val searchableStellarHostProperties: Set<StellarHostProperty>,
    val searchablePlanetProperties: Set<PlanetProperty>,
    val stellarHosts: List<StellarHost>
)
