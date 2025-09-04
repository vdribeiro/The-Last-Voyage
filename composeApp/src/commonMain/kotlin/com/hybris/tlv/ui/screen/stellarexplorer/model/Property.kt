package com.hybris.tlv.ui.screen.stellarexplorer.model

import com.hybris.tlv.ui.screen.stellarexplorer.ifContains
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

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

internal fun List<StellarHost>.searchStellarHosts(search: String, searchable: Set<StellarHostProperty>): List<StellarHost> =
    if (search.isNotBlank()) {
        val searchLowercase = search.lowercase()
        filter { stellarHost ->
            with(receiver = stellarHost) {
                listOfNotNull(
                    searchable.ifContains(
                        element = StellarHostProperty.SYSTEM_NAME,
                        value = systemName
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.NAME,
                        value = name
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.SPECTRAL_TYPE,
                        value = spectralType
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.TEMPERATURE,
                        value = effectiveTemperature?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.RADIUS,
                        value = radius?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.MASS,
                        value = mass?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.METALLICITY,
                        value = metallicity?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.LUMINOSITY,
                        value = luminosity?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.GRAVITY,
                        value = gravity?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.AGE,
                        value = age?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.DENSITY,
                        value = density?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.ROTATIONAL_VELOCITY,
                        value = rotationalVelocity?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.ROTATIONAL_PERIOD,
                        value = rotationalPeriod?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.DISTANCE,
                        value = distance?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.RA,
                        value = ra?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.DEC,
                        value = dec?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.PLANET_COUNT,
                        value = planets.size.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.SPECTRAL_TYPE_SCORE,
                        value = score?.stellarSpectralTypeScore?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.MASS_SCORE,
                        value = score?.stellarMassScore?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.AGE_SCORE,
                        value = score?.stellarAgeScore?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.ACTIVITY_SCORE,
                        value = score?.stellarActivityScore?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.ROTATIONAL_PERIOD_SCORE,
                        value = score?.stellarRotationalPeriodScore?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.GRAVITY_SCORE,
                        value = score?.stellarGravityScore?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.METALLICITY_SCORE,
                        value = score?.stellarMetallicityScore?.toString()
                    ),
                    searchable.ifContains(
                        element = StellarHostProperty.EFFECTIVE_TEMPERATURE_SCORE,
                        value = score?.stellarEffectiveTemperatureScore?.toString()
                    ),
                )
            }.any { it.lowercase().contains(other = searchLowercase) }
        }
    } else this

internal fun List<Planet>.searchPlanets(search: String, searchable: Set<PlanetProperty>): List<Planet> =
    if (search.isNotBlank()) {
        val searchLowercase = search.lowercase()
        filter { planet ->
            with(receiver = planet) {
                listOfNotNull(
                    searchable.ifContains(
                        element = PlanetProperty.NAME,
                        value = name
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.STATUS,
                        value = status.displayName
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.ORBITAL_PERIOD,
                        value = orbitalPeriod?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.ORBIT_AXIS,
                        value = orbitAxis?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.RADIUS,
                        value = radius?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.MASS,
                        value = mass?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.DENSITY,
                        value = density?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.ECCENTRICITY,
                        value = eccentricity?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.INSOLATION_FLUX,
                        value = insolationFlux?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.TEMPERATURE,
                        value = equilibriumTemperature?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.OCCULTATION_DEPTH,
                        value = occultationDepth?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.INCLINATION,
                        value = inclination?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.OBLIQUITY,
                        value = obliquity?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.HABITABILITY,
                        value = score?.habitabilityScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.TYPE,
                        value = score?.planetType?.displayName
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.ROCHE_SCORE,
                        value = score?.rocheScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.HABITABLE_ZONE_KOPPARAPU_SCORE,
                        value = score?.habitableZoneKopparapuScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.HABITABLE_ZONE_KASTING_SCORE,
                        value = score?.habitableZoneKastingScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.RADIUS_SCORE,
                        value = score?.planetRadiusScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.MASS_SCORE,
                        value = score?.planetMassScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.TELLURICITY_SCORE,
                        value = score?.planetTelluricityScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.ECCENTRICITY_SCORE,
                        value = score?.planetEccentricityScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.TEMPERATURE_SCORE,
                        value = score?.planetTemperatureScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.OBLIQUITY_SCORE,
                        value = score?.planetObliquityScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.ESI_SCORE,
                        value = score?.planetEsiScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.PROTECTION_SCORE,
                        value = score?.planetProtectionScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.TIDAL_LOCKING_SCORE,
                        value = score?.planetTidalLockingScore?.toString()
                    ),
                    searchable.ifContains(
                        element = PlanetProperty.CONFIDENCE,
                        value = score?.confidenceScore?.toString()
                    ),
                )
            }.any { it.lowercase().contains(other = searchLowercase) }
        }
    } else this

internal fun List<StellarHost>.sortStellarHosts(sort: StellarHostProperty, ascending: Boolean): List<StellarHost> =
    sortedWith(comparator = getStellarHostComparator(sort = sort, ascending = ascending).thenBy { it.id })

internal fun List<Planet>.sortPlanets(sort: PlanetProperty, ascending: Boolean): List<Planet> =
    sortedWith(comparator = getPlanetsComparator(sort = sort, ascending = ascending).thenBy { it.id })

private inline fun <T, K> compare(
    ascending: Boolean,
    comparator: Comparator<in K>,
    crossinline selector: (T) -> K
): Comparator<T> =
    if (ascending) compareBy(
        comparator = comparator,
        selector = selector
    ) else compareByDescending(
        comparator = comparator,
        selector = selector
    )

private fun getStellarHostComparator(sort: StellarHostProperty, ascending: Boolean): Comparator<StellarHost> = when (sort) {
    StellarHostProperty.NAME -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.name }

    StellarHostProperty.SYSTEM_NAME -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.systemName }

    StellarHostProperty.PLANET_COUNT -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.planets.size }

    StellarHostProperty.SPECTRAL_TYPE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.spectralType }

    StellarHostProperty.TEMPERATURE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.effectiveTemperature }

    StellarHostProperty.RADIUS -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.radius }

    StellarHostProperty.MASS -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.mass }

    StellarHostProperty.METALLICITY -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.metallicity }

    StellarHostProperty.LUMINOSITY -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.luminosity }

    StellarHostProperty.GRAVITY -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.gravity }

    StellarHostProperty.AGE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.age }

    StellarHostProperty.DENSITY -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.density }

    StellarHostProperty.ROTATIONAL_VELOCITY -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.rotationalVelocity }

    StellarHostProperty.ROTATIONAL_PERIOD -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.rotationalPeriod }

    StellarHostProperty.DISTANCE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.distance }

    StellarHostProperty.RA -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.ra }

    StellarHostProperty.DEC -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.dec }

    StellarHostProperty.SPECTRAL_TYPE_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.stellarSpectralTypeScore }

    StellarHostProperty.MASS_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.stellarMassScore }

    StellarHostProperty.AGE_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.stellarAgeScore }

    StellarHostProperty.ACTIVITY_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.stellarActivityScore }

    StellarHostProperty.ROTATIONAL_PERIOD_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.stellarRotationalPeriodScore }

    StellarHostProperty.GRAVITY_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.stellarGravityScore }

    StellarHostProperty.METALLICITY_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.stellarMetallicityScore }

    StellarHostProperty.EFFECTIVE_TEMPERATURE_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.stellarEffectiveTemperatureScore }
}

private fun getPlanetsComparator(sort: PlanetProperty, ascending: Boolean): Comparator<Planet> = when (sort) {
    PlanetProperty.NAME -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.name }

    PlanetProperty.STATUS -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.status.displayName }

    PlanetProperty.HABITABILITY -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.habitabilityScore }

    PlanetProperty.CONFIDENCE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.confidenceScore }

    PlanetProperty.TYPE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.planetType?.displayName }

    PlanetProperty.ORBITAL_PERIOD -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.orbitalPeriod }

    PlanetProperty.ORBIT_AXIS -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.orbitAxis }

    PlanetProperty.RADIUS -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.radius }

    PlanetProperty.MASS -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.mass }

    PlanetProperty.DENSITY -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.density }

    PlanetProperty.ECCENTRICITY -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.eccentricity }

    PlanetProperty.INSOLATION_FLUX -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.insolationFlux }

    PlanetProperty.TEMPERATURE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.equilibriumTemperature }

    PlanetProperty.OCCULTATION_DEPTH -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.occultationDepth }

    PlanetProperty.INCLINATION -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.inclination }

    PlanetProperty.OBLIQUITY -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.obliquity }

    PlanetProperty.ROCHE_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.rocheScore }

    PlanetProperty.HABITABLE_ZONE_KOPPARAPU_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.habitableZoneKopparapuScore }

    PlanetProperty.HABITABLE_ZONE_KASTING_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.habitableZoneKastingScore }

    PlanetProperty.RADIUS_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.planetRadiusScore }

    PlanetProperty.MASS_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.planetMassScore }

    PlanetProperty.TELLURICITY_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.planetTelluricityScore }

    PlanetProperty.ECCENTRICITY_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.planetEccentricityScore }

    PlanetProperty.TEMPERATURE_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.planetTemperatureScore }

    PlanetProperty.OBLIQUITY_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.planetObliquityScore }

    PlanetProperty.ESI_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.planetEsiScore }

    PlanetProperty.PROTECTION_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.planetProtectionScore }

    PlanetProperty.TIDAL_LOCKING_SCORE -> compare(
        ascending = ascending,
        comparator = if (ascending) nullsLast() else nullsFirst()
    ) { it.score?.planetTidalLockingScore }
}
