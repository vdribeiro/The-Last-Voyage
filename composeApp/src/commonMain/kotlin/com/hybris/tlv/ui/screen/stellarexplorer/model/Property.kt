package com.hybris.tlv.ui.screen.stellarexplorer.model

import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

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
    DEC,
    SPECTRAL_TYPE_SCORE,
    MASS_SCORE,
    AGE_SCORE,
    ACTIVITY_SCORE,
    ROTATIONAL_PERIOD_SCORE,
    GRAVITY_SCORE,
    METALLICITY_SCORE,
    EFFECTIVE_TEMPERATURE_SCORE
}

internal enum class PlanetProperty {
    NAME,
    STATUS,
    HABITABILITY,
    CONFIDENCE,
    TYPE,
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
    OBLIQUITY,
    ROCHE_SCORE,
    HABITABLE_ZONE_KOPPARAPU_SCORE,
    HABITABLE_ZONE_KASTING_SCORE,
    RADIUS_SCORE,
    MASS_SCORE,
    TELLURICITY_SCORE,
    ECCENTRICITY_SCORE,
    TEMPERATURE_SCORE,
    OBLIQUITY_SCORE,
    ESI_SCORE,
    PROTECTION_SCORE,
    TIDAL_LOCKING_SCORE
}

internal fun getStellarHostComparator(sort: StellarHostProperty, ascending: Boolean): Comparator<StellarHost> = when (sort) {
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

internal fun getPlanetsComparator(sort: PlanetProperty, ascending: Boolean): Comparator<Planet> = when (sort) {
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
