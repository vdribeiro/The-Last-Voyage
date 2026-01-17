package com.hybris.tlv.domain.usecase.space.model

import com.hybris.tlv.core.security.generateUuid
import com.hybris.tlv.domain.usecase.space.formula.Constants

/**
 * Holds the mathematical weights and limits used in scoring to allow for easy tuning of the model.
 */
internal data class Formula(
    val id: String = generateUuid(),
    // Weights
    val rocheWeight: Double = Constants.ROCHE_WEIGHT,
    val habitableZoneKopparapuWeight: Double = Constants.HABITABLE_ZONE_KOPPARAPU_WEIGHT,
    val habitableZoneKastingWeight: Double = Constants.HABITABLE_ZONE_KASTING_WEIGHT,
    val planetRadiusWeight: Double = Constants.PLANET_RADIUS_WEIGHT,
    val planetMassWeight: Double = Constants.PLANET_MASS_WEIGHT,
    val planetTelluricityWeight: Double = Constants.PLANET_TELLURICITY_WEIGHT,
    val planetEccentricityWeight: Double = Constants.PLANET_ECCENTRICITY_WEIGHT,
    val planetTemperatureWeight: Double = Constants.PLANET_TEMPERATURE_WEIGHT,
    val planetObliquityWeight: Double = Constants.PLANET_OBLIQUITY_WEIGHT,
    val planetEsiWeight: Double = Constants.PLANET_ESI_WEIGHT,
    val stellarSpectralTypeWeight: Double = Constants.STELLAR_SPECTRAL_TYPE_WEIGHT,
    val stellarMassWeight: Double = Constants.STELLAR_MASS_WEIGHT,
    val stellarAgeWeight: Double = Constants.STELLAR_AGE_WEIGHT,
    val stellarActivityWeight: Double = Constants.STELLAR_ACTIVITY_WEIGHT,
    val stellarRotationalPeriodWeight: Double = Constants.STELLAR_ROTATIONAL_PERIOD_WEIGHT,
    val stellarGravityWeight: Double = Constants.STELLAR_GRAVITY_WEIGHT,
    val stellarMetallicityWeight: Double = Constants.STELLAR_METALLICITY_WEIGHT,
    val stellarEffectiveTemperatureWeight: Double = Constants.STELLAR_EFFECTIVE_TEMPERATURE_WEIGHT,
    val planetProtectionWeight: Double = Constants.PLANET_PROTECTION_WEIGHT,
    val planetTidalLockingWeight: Double = Constants.PLANET_TIDAL_LOCKING_WEIGHT,

    // Limits
    val planetMassLowerLimit: Double = Constants.PLANET_MASS_LOWER_LIMIT,
    val planetMassIdealUpperLimit: Double = Constants.PLANET_MASS_IDEAL_UPPER_LIMIT,
    val planetMassMaxUpperLimit: Double = Constants.PLANET_MASS_MAX_UPPER_LIMIT,
    val planetRadiusLowerLimit: Double = Constants.PLANET_RADIUS_LOWER_LIMIT,
    val planetRadiusIdealUpperLimit: Double = Constants.PLANET_RADIUS_IDEAL_UPPER_LIMIT,
    val planetRadiusMaxUpperLimit: Double = Constants.PLANET_RADIUS_MAX_UPPER_LIMIT,
    val stellarHostEffectiveTemperatureMaxDeviation: Double = Constants.STELLAR_HOST_EFFECTIVE_TEMPERATURE_MAX_DEVIATION
)
