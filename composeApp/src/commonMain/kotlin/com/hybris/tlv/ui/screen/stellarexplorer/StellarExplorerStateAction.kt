package com.hybris.tlv.ui.screen.stellarexplorer

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.lazy.LazyListState
import com.hybris.tlv.core.resource.ImageResource
import com.hybris.tlv.domain.usecase.space.model.PlanetStatus
import com.hybris.tlv.domain.usecase.space.model.PlanetType
import com.hybris.tlv.domain.usecase.space.model.StellarHost

internal sealed interface StellarExplorerAction {
    data object Back: StellarExplorerAction
    data class SaveListState(val listState: LazyListState): StellarExplorerAction
    data object ChangeView: StellarExplorerAction
    data class Search(val search: String): StellarExplorerAction
    data class OpenStellarHost(val stellarHost: Exoplanets.Host): StellarExplorerAction
    data class OpenPlanet(val planet: Exoplanets.Planet): StellarExplorerAction
    data class Sort(val sort: String): StellarExplorerAction
    data object ChangeSortDirection: StellarExplorerAction
    data class ChangeVisibility(val property: String): StellarExplorerAction
    data class ChangeSearchable(val property: String): StellarExplorerAction
}

internal data class StellarExplorerState(
    val loading: Boolean = true,
    val currentContent: Content = Content.LIST_HOSTS,
    val listState: LazyListState = LazyListState(),
    val exoplanets: Exoplanets = Exoplanets(),
    val search: String = "",
    val properties: ImmutableList<String> = persistentListOf(),
    val sortProperty: String = "",
    val sortAscending: Boolean = true,
    val visibleProperties: ImmutableList<String> = persistentListOf(),
    val searchProperties: ImmutableList<String> = persistentListOf()
)

internal enum class Content {
    LIST_HOSTS,
    DETAIL_HOSTS,
    LIST_PLANETS,
    DETAIL_PLANETS,
}

internal data class Exoplanets(
    val stellarHosts: ImmutableList<Host> = persistentListOf(),
    val planets: ImmutableList<Planet> = persistentListOf()
) {
    data class Host(
        val id: String,
        val name: String?,
        val systemName: String?,
        val spectralType: String?,
        val spectralTypeScore: Double?,
        val effectiveTemperature: Double?,
        val effectiveTemperatureScore: Double?,
        val radius: Double?,
        val mass: Double?,
        val massScore: Double?,
        val metallicity: Double?,
        val metallicityScore: Double?,
        val luminosity: Double?,
        val gravity: Double?,
        val gravityScore: Double?,
        val age: Double?,
        val ageScore: Double?,
        val density: Double?,
        val rotationalVelocity: Double?,
        val activityScore: Double?,
        val rotationalPeriod: Double?,
        val rotationalPeriodScore: Double?,
        val distance: Double?,
        val ra: Double?,
        val dec: Double?,
        val image: ImageResource?,
        val planetCount: Int?,
    )

    data class Planet(
        val id: String,
        val name: String?,
        val stellarHostId: String?,
        val status: String?,
        val orbitalPeriod: Double?,
        val orbitAxis: Double?,
        val radius: Double?,
        val radiusScore: Double?,
        val mass: Double?,
        val massScore: Double?,
        val density: Double?,
        val telluricityScore: Double?,
        val eccentricity: Double?,
        val eccentricityScore: Double?,
        val insolationFlux: Double?,
        val equilibriumTemperature: Double?,
        val temperatureScore: Double?,
        val occultationDepth: Double?,
        val inclination: Double?,
        val obliquity: Double?,
        val obliquityScore: Double?,
        val habitabilityScore: Double?,
        val confidenceScore: Double?,
        val rocheScore: Double?,
        val habitableZoneKopparapuScore: Double?,
        val habitableZoneKastingScore: Double?,
        val esiScore: Double?,
        val protectionScore: Double?,
        val tidalLockingScore: Double?,
        val type: String?,
        val image: ImageResource?
    )
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

internal data class FilterPropertiesCriteria(
    val currentContent: Content
)

internal data class FilterPropertiesCriteriaCombine(
    val criteria: FilterPropertiesCriteria,
    val translations: Map<String, String>
)

internal data class FilterExoplanetsCriteria(
    val currentContent: Content,
    val search: String,
    val sortProperty: String,
    val sortAscending: Boolean,
    val visibleProperties: List<String>,
    val searchProperties: List<String>
)

internal data class FilterExoplanetsCriteriaCombine(
    val criteria: FilterExoplanetsCriteria,
    val stellarHosts: List<StellarHost>
)
