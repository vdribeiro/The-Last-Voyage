package com.hybris.tlv.domain.usecase.space.formula

internal object Constants {

    //region Astronomical Constants
    const val GRAVITATIONAL_CONSTANT = 6.67430e-11 // m^3 kg^-1 s^-2

    // Effective temperature of the Sun in Kelvin
    const val SUN_EFFECTIVE_TEMPERATURE = 5780.0
    // Effective temperature of the Sun at 1AU
    const val SUN_EFFECTIVE_TEMPERATURE_1AU = 278.5
    // Radius of the Sun in Earth radii in meters
    const val SUN_RADIUS_IN_EARTH_RADII = 109.2
    // Sun's radius in Astronomical Units
    const val SUN_RADIUS_IN_AU = 0.00465
    // Solar radius in meters
    const val SUN_RADIUS_IN_METERS = 6.957e8
    // Solar mass in kilograms
    const val SUN_MASS_IN_KG = 1.98847e30
    // Sun's approximate surface gravity in cm/s^2
    const val SUN_SURFACE_GRAVITY = 4.4378
    // Inner and outer boundaries of the habitable zone
    const val SUN_INNER_BOUNDARY = 0.95
    const val SUN_OUTER_BOUNDARY = 1.37

    // Jupiter mass
    const val JUPITER_MASS_IN_EARTHS = 317.8

    // Earth's average density in g/cm^3
    const val EARTH_AVERAGE_DENSITY = 5.51
    // Earth's radius in solar radius
    const val EARTH_RADIUS_IN_SUNS = 109.2
    // Days in a year
    const val EARTH_ORBITAL_PERIOD_IN_DAYS = 365.25
    // Seconds in a day
    const val EARTH_DAY_IN_SECONDS = 86400
    // Approximate average Bond albedo of Earth
    const val EARTH_BOND_ALBEDO = 0.3

    // A Parsec in light-years
    const val PARSEC = 3.26156
    //endregion

    //region Ranges and Limits
    // Planet mass limits in Earth masses for a terrestrial planet
    const val PLANET_MASS_LOWER_LIMIT = 0.1
    const val PLANET_MASS_IDEAL_UPPER_LIMIT = 5.0
    const val PLANET_MASS_MAX_UPPER_LIMIT = 10.0 // Beyond this, likely a gas giant.

    // Planet radius limits in Earth radii
    const val PLANET_RADIUS_LOWER_LIMIT = 0.5
    const val PLANET_RADIUS_IDEAL_UPPER_LIMIT = 1.5
    const val PLANET_RADIUS_MAX_UPPER_LIMIT = 2.0 // "Radius Valley" transition to mini-Neptunes

    // Deviation for stellar temperature scoring
    const val STELLAR_HOST_EFFECTIVE_TEMPERATURE_MAX_DEVIATION = 4000.0
    //endregion

    //region Coefficients for the HZ boundaries from the 2013 erratum (ApJ, 771, 82).
    // Runaway Greenhouse Limit
    const val S_EFF_SUN_RG = 1.1066
    const val A_RG = 1.3323e-4
    const val B_RG = 1.5796e-8
    const val C_RG = -8.3079e-12
    const val D_RG = -1.9310e-15

    // Maximum Greenhouse Limit
    const val S_EFF_SUN_MG = 0.3562
    const val A_MG = 6.1706e-5
    const val B_MG = 1.6980e-9
    const val C_MG = -3.1979e-12
    const val D_MG = -5.6372e-16

    // Recent Venus Limit
    const val S_EFF_SUN_RV = 1.7753
    const val A_RV = 1.4316e-4
    const val B_RV = 2.9875e-9
    const val C_RV = -7.5702e-12
    const val D_RV = -1.1635e-15

    // Early Mars Limit
    const val S_EFF_SUN_EM = 0.3204
    const val A_EM = 5.5471e-5
    const val B_EM = 1.5258e-9
    const val C_EM = -2.8735e-12
    const val D_EM = -5.0782e-16
    //endregion

    //region Earth Similarity Index (ESI) constants from the Planetary Habitability Laboratory (PHL)
    const val EARTH_RADIUS_REFERENCE = 1.0
    const val EARTH_RADIUS_WEIGHT = 0.57
    const val EARTH_DENSITY_REFERENCE = 1.0
    const val EARTH_DENSITY_WEIGHT = 1.07
    const val EARTH_ESCAPE_VELOCITY_REFERENCE = 1.0
    const val EARTH_ESCAPE_VELOCITY_WEIGHT = 0.70
    const val EARTH_SURFACE_TEMPERATURE_REFERENCE = 288.0 // 15°C
    const val EARTH_SURFACE_TEMPERATURE_WEIGHT = 5.58
    const val EARTH_INSOLATION_REFERENCE = 1.0
    const val EARTH_INSOLATION_WEIGHT = 5.58
    //endregion

    //region Scoring Weights -> subjective but used to reflect the relative importance of each factor
    // Tier 1: Is the planet in a stable orbit in the right location?
    const val ROCHE_WEIGHT = 100.0 // Critical factor: planet must exist
    const val HABITABLE_ZONE_KOPPARAPU_WEIGHT = 30.0
    const val HABITABLE_ZONE_KASTING_WEIGHT = 20.0

    // Tier 2: Does the planet have the right intrinsic properties?
    const val PLANET_RADIUS_WEIGHT = 6.0
    const val PLANET_MASS_WEIGHT = 6.0
    const val PLANET_TELLURICITY_WEIGHT = 18.0
    const val PLANET_ECCENTRICITY_WEIGHT = 6.0
    const val PLANET_TEMPERATURE_WEIGHT = 1.0
    const val PLANET_OBLIQUITY_WEIGHT = 2.0
    const val PLANET_ESI_WEIGHT = 1.0

    // Tier 3: Is the host star a good sun?
    const val STELLAR_SPECTRAL_TYPE_WEIGHT = 8.0
    const val STELLAR_MASS_WEIGHT = 5.0
    const val STELLAR_AGE_WEIGHT = 6.0
    const val STELLAR_ACTIVITY_WEIGHT = 15.0
    const val STELLAR_ROTATIONAL_PERIOD_WEIGHT = 7.0
    const val STELLAR_GRAVITY_WEIGHT = 3.0
    const val STELLAR_METALLICITY_WEIGHT = 4.0
    const val STELLAR_EFFECTIVE_TEMPERATURE_WEIGHT = 1.0

    // Tier 4: Can the planet protect itself?
    const val PLANET_PROTECTION_WEIGHT = 20.0
    const val PLANET_TIDAL_LOCKING_WEIGHT = 1.0
    //endregion
}
