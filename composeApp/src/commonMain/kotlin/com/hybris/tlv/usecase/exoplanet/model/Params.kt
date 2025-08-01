package com.hybris.tlv.usecase.exoplanet.model

import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.HABITABLE_ZONE_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_ECCENTRICITY_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_ESI_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_MASS_LOWER_LIMIT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_MASS_UPPER_LIMIT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_MASS_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_OBLIQUITY_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_PROTECTION_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_RADIUS_LOWER_LIMIT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_RADIUS_UPPER_LIMIT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_RADIUS_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_TELLURICITY_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_TEMPERATURE_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_TIDAL_LOCKING_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.ROCHE_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.STELLAR_ACTIVITY_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.STELLAR_AGE_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.STELLAR_EFFECTIVE_TEMPERATURE_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.STELLAR_GRAVITY_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.STELLAR_HOST_EFFECTIVE_TEMPERATURE_MAX_DEVIATION
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.STELLAR_MASS_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.STELLAR_METALLICITY_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.STELLAR_ROTATIONAL_PERIOD_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.STELLAR_SPECTRAL_TYPE_WEIGHT

internal data class Params(
    val stellarHost: StellarHost,
    val planet: Planet,
    val math: Math = Math()
) {
    data class StellarHost(
        val spectralType: String?,
        val effectiveTemperature: Double?,
        val radius: Double?,
        val mass: Double?,
        val metallicity: Double?,
        val luminosity: Double?,
        val gravity: Double?,
        val age: Double?,
        val density: Double?,
        val rotationalVelocity: Double?,
        val rotationalPeriod: Double?
    )

    data class Planet(
        val orbitalPeriod: Double?,
        val orbitAxis: Double?,
        val radius: Double?,
        val mass: Double?,
        val density: Double?,
        val eccentricity: Double?,
        val insolationFlux: Double?,
        val equilibriumTemperature: Double?,
        val occultationDepth: Double?,
        val inclination: Double?,
        val obliquity: Double?
    )

    data class Math(
        val rocheWeight: Double = ROCHE_WEIGHT,
        val habitableZoneWeight: Double = HABITABLE_ZONE_WEIGHT,
        val planetRadiusWeight: Double = PLANET_RADIUS_WEIGHT,
        val planetMassWeight: Double = PLANET_MASS_WEIGHT,
        val planetTelluricityWeight: Double = PLANET_TELLURICITY_WEIGHT,
        val planetEccentricityWeight: Double = PLANET_ECCENTRICITY_WEIGHT,
        val planetTemperatureWeight: Double = PLANET_TEMPERATURE_WEIGHT,
        val planetObliquityWeight: Double = PLANET_OBLIQUITY_WEIGHT,
        val planetEsiWeight: Double = PLANET_ESI_WEIGHT,
        val stellarSpectralTypeWeight: Double = STELLAR_SPECTRAL_TYPE_WEIGHT,
        val stellarMassWeight: Double = STELLAR_MASS_WEIGHT,
        val stellarAgeWeight: Double = STELLAR_AGE_WEIGHT,
        val stellarActivityWeight: Double = STELLAR_ACTIVITY_WEIGHT,
        val stellarRotationalPeriodWeight: Double = STELLAR_ROTATIONAL_PERIOD_WEIGHT,
        val stellarGravityWeight: Double = STELLAR_GRAVITY_WEIGHT,
        val stellarMetallicityWeight: Double = STELLAR_METALLICITY_WEIGHT,
        val stellarEffectiveTemperatureWeight: Double = STELLAR_EFFECTIVE_TEMPERATURE_WEIGHT,
        val planetProtectionWeight: Double = PLANET_PROTECTION_WEIGHT,
        val planetTidalLockingWeight: Double = PLANET_TIDAL_LOCKING_WEIGHT,
        val planetMassLowerLimit: Double = PLANET_MASS_LOWER_LIMIT,
        val planetMassUpperLimit: Double = PLANET_MASS_UPPER_LIMIT,
        val planetRadiusLowerLimit: Double = PLANET_RADIUS_LOWER_LIMIT,
        val planetRadiusUpperLimit: Double = PLANET_RADIUS_UPPER_LIMIT,
        val stellarHostEffectiveTemperatureMaxDeviation: Double = STELLAR_HOST_EFFECTIVE_TEMPERATURE_MAX_DEVIATION
    )
}
