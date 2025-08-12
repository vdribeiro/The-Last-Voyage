package com.hybris.tlv.usecase.space.model

import com.hybris.tlv.usecase.space.formula.Formula

/**
 * Holds the mathematical weights and limits used in scoring to allow for easy tuning of the model.
 */
internal data class Math(
    // Weights
    val rocheWeight: Double = Formula.ROCHE_WEIGHT,
    val habitableZoneWeight: Double = Formula.HABITABLE_ZONE_WEIGHT,
    val planetRadiusWeight: Double = Formula.PLANET_RADIUS_WEIGHT,
    val planetMassWeight: Double = Formula.PLANET_MASS_WEIGHT,
    val planetTelluricityWeight: Double = Formula.PLANET_TELLURICITY_WEIGHT,
    val planetEccentricityWeight: Double = Formula.PLANET_ECCENTRICITY_WEIGHT,
    val planetTemperatureWeight: Double = Formula.PLANET_TEMPERATURE_WEIGHT,
    val planetObliquityWeight: Double = Formula.PLANET_OBLIQUITY_WEIGHT,
    val planetEsiWeight: Double = Formula.PLANET_ESI_WEIGHT,
    val stellarSpectralTypeWeight: Double = Formula.STELLAR_SPECTRAL_TYPE_WEIGHT,
    val stellarMassWeight: Double = Formula.STELLAR_MASS_WEIGHT,
    val stellarAgeWeight: Double = Formula.STELLAR_AGE_WEIGHT,
    val stellarActivityWeight: Double = Formula.STELLAR_ACTIVITY_WEIGHT,
    val stellarRotationalPeriodWeight: Double = Formula.STELLAR_ROTATIONAL_PERIOD_WEIGHT,
    val stellarGravityWeight: Double = Formula.STELLAR_GRAVITY_WEIGHT,
    val stellarMetallicityWeight: Double = Formula.STELLAR_METALLICITY_WEIGHT,
    val stellarEffectiveTemperatureWeight: Double = Formula.STELLAR_EFFECTIVE_TEMPERATURE_WEIGHT,
    val planetProtectionWeight: Double = Formula.PLANET_PROTECTION_WEIGHT,
    val planetTidalLockingWeight: Double = Formula.PLANET_TIDAL_LOCKING_WEIGHT,

    // Limits
    val planetMassLowerLimit: Double = Formula.PLANET_MASS_LOWER_LIMIT,
    val planetMassIdealUpperLimit: Double = Formula.PLANET_MASS_IDEAL_UPPER_LIMIT,
    val planetMassMaxUpperLimit: Double = Formula.PLANET_MASS_MAX_UPPER_LIMIT,
    val planetRadiusLowerLimit: Double = Formula.PLANET_RADIUS_LOWER_LIMIT,
    val planetRadiusIdealUpperLimit: Double = Formula.PLANET_RADIUS_IDEAL_UPPER_LIMIT,
    val planetRadiusMaxUpperLimit: Double = Formula.PLANET_RADIUS_MAX_UPPER_LIMIT,
    val stellarHostEffectiveTemperatureMaxDeviation: Double = Formula.STELLAR_HOST_EFFECTIVE_TEMPERATURE_MAX_DEVIATION
)
