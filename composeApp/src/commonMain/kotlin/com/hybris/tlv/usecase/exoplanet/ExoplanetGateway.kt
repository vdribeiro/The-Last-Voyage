package com.hybris.tlv.usecase.exoplanet

import com.hybris.tlv.usecase.exoplanet.model.Params
import com.hybris.tlv.usecase.exoplanet.model.PlanetType
import com.hybris.tlv.usecase.exoplanet.model.Score
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

internal class ExoplanetGateway(): ExoplanetUseCases {

    companion object Companion {
        //region Foundational Astronomical Constants
        // Effective temperature of the Sun in Kelvin
        private const val SUN_EFFECTIVE_TEMPERATURE = 5780.0
        // Radius of the Sun in Earth radii in meters
        private const val SUN_RADIUS_IN_EARTH_RADII = 109.2
        // Sun's radius in Astronomical Units
        private const val SUN_RADIUS_IN_AU = 0.00465
        // Inner and outer boundaries of the habitable zone
        private const val SUN_INNER_BOUNDARY = 0.95
        private const val SUN_OUTER_BOUNDARY = 1.37

        // Jupiter mass
        private const val JUPITER_MASS_IN_EARTHS = 317.8

        // Earth's average density in g/cm^3
        private const val EARTH_AVERAGE_DENSITY = 5.51
        //endregion

        //region Coefficients for the HZ boundaries from the 2013 erratum (ApJ, 771, 82).
        // Runaway Greenhouse Limit
        private const val S_EFF_SUN_RG = 1.1066
        private const val A_RG = 1.3323e-4
        private const val B_RG = 1.5796e-8
        private const val C_RG = -8.3079e-12
        private const val D_RG = -1.9310e-15

        // Maximum Greenhouse Limit
        private const val S_EFF_SUN_MG = 0.3562
        private const val A_MG = 6.1706e-5
        private const val B_MG = 1.6980e-9
        private const val C_MG = -3.1979e-12
        private const val D_MG = -5.6372e-16

        // Recent Venus Limit
        private const val S_EFF_SUN_RV = 1.7753
        private const val A_RV = 1.4316e-4
        private const val B_RV = 2.9875e-9
        private const val C_RV = -7.5702e-12
        private const val D_RV = -1.1635e-15

        // Early Mars Limit
        private const val S_EFF_SUN_EM = 0.3204
        private const val A_EM = 5.5471e-5
        private const val B_EM = 1.5258e-9
        private const val C_EM = -2.8735e-12
        private const val D_EM = -5.0782e-16
        //endregion

        //region Earth Similarity Index (ESI) constants from the Planetary Habitability Laboratory (PHL)
        private const val EARTH_RADIUS_REFERENCE = 1.0
        private const val EARTH_RADIUS_WEIGHT = 0.57
        private const val EARTH_DENSITY_REFERENCE = 1.0
        private const val EARTH_DENSITY_WEIGHT = 1.07
        private const val EARTH_ESCAPE_VELOCITY_REFERENCE = 1.0
        private const val EARTH_ESCAPE_VELOCITY_WEIGHT = 0.70
        private const val EARTH_SURFACE_TEMPERATURE_REFERENCE = 288.0 // 15°C
        private const val EARTH_SURFACE_TEMPERATURE_WEIGHT = 5.58
        private const val EARTH_INSOLATION_REFERENCE = 1.0
        private const val EARTH_INSOLATION_WEIGHT = 5.58
        //endregion

        //region Scoring Weights -> subjective but used to reflect the relative importance of each factor
        // Tier 1: Is the planet in a stable orbit in the right location?
        const val ROCHE_WEIGHT = 100.0 // Critical factor: planet must exist.
        const val HABITABLE_ZONE_WEIGHT = 25.0

        // Tier 2: Does the planet have the right intrinsic properties?
        const val PLANET_RADIUS_WEIGHT = 8.0
        const val PLANET_MASS_WEIGHT = 8.0
        const val PLANET_TELLURICITY_WEIGHT = 10.0
        const val PLANET_ECCENTRICITY_WEIGHT = 8.0
        const val PLANET_TEMPERATURE_WEIGHT = 5.0
        const val PLANET_OBLIQUITY_WEIGHT = 3.0
        const val PLANET_ESI_WEIGHT = 2.0

        // Tier 3: Is the host star a good sun?
        const val STELLAR_SPECTRAL_TYPE_WEIGHT = 5.0
        const val STELLAR_MASS_WEIGHT = 5.0
        const val STELLAR_AGE_WEIGHT = 5.0
        const val STELLAR_ACTIVITY_WEIGHT = 4.0
        const val STELLAR_ROTATIONAL_PERIOD_WEIGHT = 3.0
        const val STELLAR_GRAVITY_WEIGHT = 3.0
        const val STELLAR_METALLICITY_WEIGHT = 2.0
        const val STELLAR_EFFECTIVE_TEMPERATURE_WEIGHT = 2.0

        // Tier 4: Can the planet protect itself?
        const val PLANET_PROTECTION_WEIGHT = 5.0
        const val PLANET_TIDAL_LOCKING_WEIGHT = 5.0
        //endregion

        //region Scoring Ranges and Limits
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
    }

    /**
     * This score is a weighted average of dozens of planetary and stellar characteristics.
     * This approach handles null scores gracefully by simply omitting them from the final average.
     */
    override fun calculateHabitability(params: Params): Score {
        val weightedScores = mutableListOf<Pair<Double, Double>>()

        // Derive missing data
        // Best available temperature
        val temperature = calculateTemperature(
            stellarHostEffectiveTemperature = params.stellarHost.effectiveTemperature,
            stellarHostRadius = params.stellarHost.radius,
            planetOccultationDepth = params.planet.occultationDepth,
            planetRadius = params.planet.radius,
            planetEquilibriumTemperature = params.planet.equilibriumTemperature
        )

        // Tier 1: Location
        // Is the planet safely outside the star's Roche Limit?
        val rocheScore = calculateRocheScore(
            stellarHostRadius = params.stellarHost.radius,
            stellarHostDensity = params.stellarHost.density,
            planetDensity = params.planet.density,
            planetOrbitAxis = params.planet.orbitAxis
        )?.also { weightedScores.add(it to params.math.rocheWeight) }
        // Is the planet in the right place for liquid water?
        val habitableZoneScore = calculateHabitableZoneScore(
            stellarHostLuminosity = params.stellarHost.luminosity,
            stellarHostEffectiveTemperature = params.stellarHost.effectiveTemperature,
            planetOrbitAxis = params.planet.orbitAxis,
            planetMass = params.planet.mass,
            planetMassLowerLimit = params.math.planetMassLowerLimit,
            planetMassUpperLimit = params.math.planetMassIdealUpperLimit
        )?.also { weightedScores.add(it to params.math.habitableZoneWeight) }

        // Tier 2: Planet's Intrinsic Properties (Composition & Climate)
        // Is the planet the right size to be rocky?
        val planetRadiusScore = calculatePlanetRadiusScore(
            planetRadius = params.planet.radius,
            planetRadiusLowerLimit = params.math.planetRadiusLowerLimit,
            planetRadiusIdealUpperLimit = params.math.planetRadiusIdealUpperLimit,
            planetRadiusMaxUpperLimit = params.math.planetRadiusMaxUpperLimit
        )?.also { weightedScores.add(it to params.math.planetRadiusWeight) }

        // Can it hold an atmosphere and drive geology?
        val planetMassScore = calculatePlanetMassScore(
            planetMass = params.planet.mass,
            planetMassLowerLimit = params.math.planetMassLowerLimit,
            planetMassIdealUpperLimit = params.math.planetMassIdealUpperLimit,
            planetMassMaxUpperLimit = params.math.planetMassMaxUpperLimit
        )?.also { weightedScores.add(it to params.math.planetMassWeight) }

        // Is it rocky based on density?
        val planetTelluricityScore = calculatePlanetTelluricityScore(
            planetDensity = params.planet.density
        )?.also { weightedScores.add(it to params.math.planetTelluricityWeight) }

        // Does it have a stable, circular orbit for stable temperatures?
        val planetEccentricityScore = calculatePlanetEccentricityScore(
            planetEccentricity = params.planet.eccentricity
        )?.also { weightedScores.add(it to params.math.planetEccentricityWeight) }

        // Does it have a reasonable baseline temperature?
        val planetTemperatureScore = calculatePlanetTemperatureScore(
            planetTemperature = temperature
        )?.also { weightedScores.add(it to params.math.planetTemperatureWeight) }

        // Does it have stable seasons?
        val planetObliquityScore = calculatePlanetObliquityScore(
            planetObliquity = params.planet.obliquity
        )?.also { weightedScores.add(it to params.math.planetObliquityWeight) }

        // Overall Earth Similarity as a minor bonus factor.
        val planetEsiScore = calculatePlanetEsiScore(
            planetRadius = params.planet.radius,
            planetDensity = params.planet.density,
            planetMass = params.planet.mass,
            planetTemperature = temperature,
            planetInsolationFlux = params.planet.insolationFlux
        )?.also { weightedScores.add(it to params.math.planetEsiWeight) }

        // Tier 3: Host Star Quality
        // Is the star stable?
        val stellarSpectralTypeScore = calculateStellarSpectralTypeScore(
            stellarHostSpectralType = params.stellarHost.spectralType
        )?.also { weightedScores.add(it to params.math.stellarSpectralTypeWeight) }

        // Does the star have a long, stable lifetime?
        val stellarMassScore = calculateStellarMassScore(
            stellarHostMass = params.stellarHost.mass
        )?.also { weightedScores.add(it to params.math.stellarMassWeight) }

        // Is the star old enough for life, but not too old?
        val stellarAgeScore = calculateStellarAgeScore(
            stellarHostAge = params.stellarHost.age
        )?.also { weightedScores.add(it to params.math.stellarAgeWeight) }

        // Is the star prone to violent flares?
        val stellarActivityScore = calculateStellarActivityScore(
            stellarHostRotationalVelocity = params.stellarHost.rotationalVelocity,
        )?.also { weightedScores.add(it to params.math.stellarActivityWeight) }
        val stellarRotationalPeriodScore = calculateStellarRotationalPeriodScore(
            stellarHostRotationalPeriod = params.stellarHost.rotationalPeriod
        )?.also { if (stellarActivityScore == null) weightedScores.add(it to params.math.stellarRotationalPeriodWeight) }

        // Is it a compact main-sequence star or a giant?
        val stellarGravityScore = calculateStellarGravityScore(
            stellarHostGravity = params.stellarHost.gravity
        )?.also { weightedScores.add(it to params.math.stellarGravityWeight) }

        // Does it have the right materials to form rocky planets?
        val stellarMetallicityScore = calculateStellarMetallicityScore(
            stellarHostMetallicity = params.stellarHost.metallicity
        )?.also { weightedScores.add(it to params.math.stellarMetallicityWeight) }

        // Is its temperature ideal?
        val stellarEffectiveTemperatureScore = calculateStellarEffectiveTemperatureScore(
            stellarHostEffectiveTemperature = params.stellarHost.effectiveTemperature,
            stellarHostEffectiveTemperatureMaxDeviation = params.math.stellarHostEffectiveTemperatureMaxDeviation
        )?.also { weightedScores.add(it to params.math.stellarEffectiveTemperatureWeight) }

        // Tier 4: Planetary Protection
        // Can the planet shield itself?
        val planetProtectionScore = calculatePlanetProtectionScore(
            planetMass = params.planet.mass,
            planetDensity = params.planet.density
        )?.also { weightedScores.add(it to params.math.planetProtectionWeight) }

        // Is the planet free from extreme temperature lock?
        val planetTidalLockingScore = calculatePlanetTidalLockingScore(
            stellarHostSpectralType = params.stellarHost.spectralType,
            planetOrbitalPeriod = params.planet.orbitalPeriod
        )?.also { weightedScores.add(it to params.math.planetTidalLockingWeight) }

        val totalScore = weightedScores.sumOf { it.first * it.second }
        val totalWeight = weightedScores.sumOf { it.second }
        val habitabilityScore = if (totalWeight == 0.0) 0.0 else totalScore / totalWeight

        // Extra: Planet Types
        val planetType = calculatePlanetType(
            stellarHostSpectralType = params.stellarHost.spectralType,
            stellarHostAge = params.stellarHost.age,
            planetMass = params.planet.mass,
            planetRadius = params.planet.radius,
            planetDensity = params.planet.density,
            planetOrbitalPeriod = params.planet.orbitalPeriod,
            planetOrbitAxis = params.planet.orbitAxis,
            planetEquilibriumTemperature = params.planet.equilibriumTemperature,
            planetTidalLockingScore = planetTidalLockingScore,
            rocheScore = rocheScore,
            habitableZoneScore = habitableZoneScore,
            habitabilityScore = habitabilityScore,
        )

        return Score(
            habitabilityScore = habitabilityScore,
            rocheScore = rocheScore,
            habitableZoneScore = habitableZoneScore,
            planetRadiusScore = planetRadiusScore,
            planetMassScore = planetMassScore,
            planetTelluricityScore = planetTelluricityScore,
            planetEccentricityScore = planetEccentricityScore,
            planetTemperatureScore = planetTemperatureScore,
            planetObliquityScore = planetObliquityScore,
            planetEsiScore = planetEsiScore,
            stellarSpectralTypeScore = stellarSpectralTypeScore,
            stellarMassScore = stellarMassScore,
            stellarAgeScore = stellarAgeScore,
            stellarActivityScore = stellarActivityScore,
            stellarRotationalPeriodScore = stellarRotationalPeriodScore,
            stellarGravityScore = stellarGravityScore,
            stellarMetallicityScore = stellarMetallicityScore,
            stellarEffectiveTemperatureScore = stellarEffectiveTemperatureScore,
            planetProtectionScore = planetProtectionScore,
            planetTidalLockingScore = planetTidalLockingScore,
            planetType = planetType
        )
    }

    /**
     * Calculates a score based on whether the planet is safely outside the star's Roche Limit.
     * It uses the simplified formula for a rigid satellite: d = R_star * (2 * rho_star / rho_planet)^(1/3).
     * A score of 0.0 means the planet would be tidally destroyed.
     */
    private fun calculateRocheScore(
        stellarHostRadius: Double?,
        stellarHostDensity: Double?,
        planetDensity: Double?,
        planetOrbitAxis: Double?
    ): Double? {
        if (stellarHostRadius == null ||
            stellarHostDensity == null ||
            planetDensity == null ||
            planetOrbitAxis == null
        ) return null
        val rocheLimitInStellarRadii = (2 * stellarHostDensity / planetDensity).pow(x = 1.0 / 3.0)
        val planetOrbitInStellarRadii = planetOrbitAxis / (stellarHostRadius * SUN_RADIUS_IN_AU)
        return when {
            planetOrbitInStellarRadii < rocheLimitInStellarRadii -> 0.0 // Planet would be destroyed
            else -> 1.0                                                 // Planet is structurally stable
        }
    }

    /**
     * Calculate a planet's measured day-side temperature in Kelvin from occultation depth (the depth of the secondary eclipse).
     * This assumes the occultation was measured in the infrared where the planet's thermal emission is dominant.
     * If occultation data is unavailable, it falls back to the planet's equilibrium temperature.
     */
    private fun calculateTemperature(
        stellarHostEffectiveTemperature: Double?,
        stellarHostRadius: Double?,
        planetOccultationDepth: Double?,
        planetRadius: Double?,
        planetEquilibriumTemperature: Double?
    ): Double? {
        if (planetOccultationDepth == null ||
            stellarHostEffectiveTemperature == null ||
            stellarHostRadius == null ||
            planetRadius == null
        ) return planetEquilibriumTemperature
        val radiusRatio = planetRadius / (stellarHostRadius * SUN_RADIUS_IN_EARTH_RADII)
        if (radiusRatio == 0.0) return planetEquilibriumTemperature
        val radiusRatioSq = radiusRatio.pow(n = 2)
        if (radiusRatioSq == 0.0) return planetEquilibriumTemperature
        val tempRatio = planetOccultationDepth / radiusRatioSq
        if (tempRatio < 0) return planetEquilibriumTemperature
        return stellarHostEffectiveTemperature * tempRatio.pow(x = 0.25)
    }

    /**
     * Calculate the Circumstellar Habitable Zone (CHZ) score.
     * The CHZ is the the region around a star where liquid water could exist on a planet's surface.
     * For the Kopparapu model, the score has a flat plateau of 1.0 across the entire conservative zone and then slopes down smoothly
     * through the optimistic zones, as a simple gradient peaked at the center unfairly penalizes planets like Earth,
     * which are perfectly habitable but located near the inner edge of the Sun's conservative zone.
     * If the more sophisticated Kopparapu model cannot be applied, if falls back to the simpler Kasting model.
     */
    private fun calculateHabitableZoneScore(
        stellarHostLuminosity: Double?,
        stellarHostEffectiveTemperature: Double?,
        planetOrbitAxis: Double?,
        planetMass: Double?,
        planetMassLowerLimit: Double,
        planetMassUpperLimit: Double
    ): Double? =
        when {
            planetOrbitAxis != null && stellarHostLuminosity != null && stellarHostEffectiveTemperature != null -> calculateHabitableZoneScoreKopparapu(
                stellarHostEffectiveTemperature = stellarHostEffectiveTemperature,
                stellarHostLuminosity = stellarHostLuminosity,
                planetOrbitAxis = planetOrbitAxis,
                planetMass = planetMass,
                planetMassLowerLimit = planetMassLowerLimit,
                planetMassUpperLimit = planetMassUpperLimit
            )

            planetOrbitAxis != null && stellarHostLuminosity != null -> calculateHabitableZoneScoreKasting(
                stellarHostLuminosity = stellarHostLuminosity,
                planetOrbitAxis = planetOrbitAxis
            )

            else -> null
        }

    /**
     * Calculates the Habitable Zone score using the Kopparapu model (2013, 2014),
     * from Kopparapu et al. 2013 (ApJ, 765, 131) and its erratum (2013, ApJ, 771, 82), and Kopparapu et al. 2014 (ApJ, 787, L29).
     */
    private fun calculateHabitableZoneScoreKopparapu(
        stellarHostEffectiveTemperature: Double,
        stellarHostLuminosity: Double,
        planetOrbitAxis: Double,
        planetMass: Double?,
        planetMassLowerLimit: Double,
        planetMassUpperLimit: Double
    ): Double {
        val tStar = stellarHostEffectiveTemperature - SUN_EFFECTIVE_TEMPERATURE

        // Calculate the stellar flux for each boundary condition
        // These are polynomials of the form S_eff(T_star) = S_eff_sun + a*T_star + b*T_star^2 + c*T_star^3 + d*T_star^4
        var runawayGreenhouseFlux = S_EFF_SUN_RG + A_RG * tStar + B_RG * tStar.pow(n = 2) + C_RG * tStar.pow(n = 3) + D_RG * tStar.pow(n = 4)
        val maxGreenhouseFlux = S_EFF_SUN_MG + A_MG * tStar + B_MG * tStar.pow(n = 2) + C_MG * tStar.pow(n = 3) + D_MG * tStar.pow(n = 4)
        val recentVenusFlux = S_EFF_SUN_RV + A_RV * tStar + B_RV * tStar.pow(n = 2) + C_RV * tStar.pow(n = 3) + D_RV * tStar.pow(n = 4)
        val earlyMarsFlux = S_EFF_SUN_EM + A_EM * tStar + B_EM * tStar.pow(n = 2) + C_EM * tStar.pow(n = 3) + D_EM * tStar.pow(n = 4)

        // Apply mass-dependent correction if mass is provided
        if (planetMass != null && planetMass in planetMassLowerLimit..planetMassUpperLimit) {
            // Inner edge flux correction from Kopparapu et al. 2014
            val fluxCorrection = 0.9875 - 2.7619e-5 * planetMass.pow(n = 2) + 1.2539e-4 * planetMass
            runawayGreenhouseFlux *= fluxCorrection
        }

        val conservativeInnerAu = sqrt(x = stellarHostLuminosity / runawayGreenhouseFlux)
        val conservativeOuterAu = sqrt(x = stellarHostLuminosity / maxGreenhouseFlux)
        val optimisticInnerAu = sqrt(x = stellarHostLuminosity / recentVenusFlux)
        val optimisticOuterAu = sqrt(x = stellarHostLuminosity / earlyMarsFlux)

        return when (planetOrbitAxis) {
            // Fully within the conservative zone
            in conservativeInnerAu..conservativeOuterAu -> 1.0

            // In the "hot" optimistic zone (between recent Venus and conservative inner edge)
            in optimisticInnerAu..conservativeInnerAu -> {
                (planetOrbitAxis - optimisticInnerAu) / (conservativeInnerAu - optimisticInnerAu)
            }

            // In the "cold" optimistic zone (between conservative outer edge and early Mars)
            in conservativeOuterAu..optimisticOuterAu -> {
                1.0 - ((planetOrbitAxis - conservativeOuterAu) / (optimisticOuterAu - conservativeOuterAu))
            }

            // Outside all defined zones
            else -> 0.0
        }.coerceIn(minimumValue = 0.0, maximumValue = 1.0)
    }

    /**
     * Calculates the Habitable Zone score using the Kasting simple luminosity model (fallback).
     */
    private fun calculateHabitableZoneScoreKasting(
        stellarHostLuminosity: Double,
        planetOrbitAxis: Double
    ): Double {
        val conservativeInnerAu = SUN_INNER_BOUNDARY * sqrt(x = stellarHostLuminosity)
        val conservativeOuterAu = SUN_OUTER_BOUNDARY * sqrt(x = stellarHostLuminosity)
        val center = (conservativeInnerAu + conservativeOuterAu) / 2

        return when (planetOrbitAxis) {
            // Score is 1.0 at the center, decreasing to 0.0 at the edges
            in conservativeInnerAu..conservativeOuterAu -> {
                1.0 - abs(planetOrbitAxis - center) / (center - conservativeInnerAu)
            }

            // Outside the habitable zone
            else -> 0.0
        }.coerceIn(minimumValue = 0.0, maximumValue = 1.0)
    }

    /**
     * Calculates the Earth Similarity Index (ESI) score.
     * The ESI is a scale used to quantify how similar a planet is to Earth.
     */
    private fun calculatePlanetEsiScore(
        planetRadius: Double?,
        planetDensity: Double?,
        planetMass: Double?,
        planetTemperature: Double?,
        planetInsolationFlux: Double?
    ): Double? {
        var parameterCount = 0

        val esiRadius = if (planetRadius != null) {
            parameterCount++
            calculateEsiComponent(
                value = planetRadius,
                reference = EARTH_RADIUS_REFERENCE,
                weight = EARTH_RADIUS_WEIGHT
            )
        } else 1.0

        val esiDensity = if (planetDensity != null) {
            parameterCount++
            calculateEsiComponent(
                value = planetDensity / EARTH_AVERAGE_DENSITY, // density in Earth units
                reference = EARTH_DENSITY_REFERENCE,
                weight = EARTH_DENSITY_WEIGHT
            )
        } else 1.0

        val esiEscapeVelocity = if (planetMass != null && planetRadius != null) {
            parameterCount++
            calculateEsiComponent(
                value = sqrt(x = planetMass / planetRadius), // v_esc = sqrt(2GM/R), relative to Earth, this becomes sqrt(mass/radius)
                reference = EARTH_ESCAPE_VELOCITY_REFERENCE,
                weight = EARTH_ESCAPE_VELOCITY_WEIGHT
            )
        } else 1.0

        // Equilibrium temperature is used as a proxy for surface temperature
        val esiTemperature = if (planetTemperature != null) {
            parameterCount++
            calculateEsiComponent(
                value = planetTemperature,
                reference = EARTH_SURFACE_TEMPERATURE_REFERENCE,
                weight = EARTH_SURFACE_TEMPERATURE_WEIGHT
            )
        } else if (planetInsolationFlux != null) {
            // Otherwise insolation flux is used as a proxy for surface temperature
            parameterCount++
            calculateEsiComponent(
                value = planetInsolationFlux,
                reference = EARTH_INSOLATION_REFERENCE,
                weight = EARTH_INSOLATION_WEIGHT
            )
        } else 1.0

        return if (parameterCount == 0) null
        else (esiRadius * esiDensity * esiEscapeVelocity * esiTemperature).pow(x = 1.0 / parameterCount)
    }

    private fun calculateEsiComponent(value: Double, reference: Double, weight: Double): Double =
        (1.0 - abs(x = (value - reference) / (value + reference))).pow(x = weight)

    /**
     * Calculates a score based on the risk of tidal locking, factoring in the star's spectral type.
     * The risk is much higher for smaller stars as their habitable zones are closer.
     */
    private fun calculatePlanetTidalLockingScore(
        stellarHostSpectralType: String?,
        planetOrbitalPeriod: Double?,
    ): Double? {
        if (planetOrbitalPeriod == null) return null

        return when (stellarHostSpectralType?.firstOrNull()) {
            // We use a more general approach for stars with no spectral type.
            null -> when {
                planetOrbitalPeriod > 100 -> 1.0 // Very low risk of being tidally locked
                planetOrbitalPeriod > 50 -> 0.9  // Low risk
                planetOrbitalPeriod > 25 -> 0.6  // Moderate risk, especially for smaller stars
                planetOrbitalPeriod > 10 -> 0.3  // High risk, very likely tidally locked
                else -> 0.1                      // Almost certainly tidally locked, severe penalty
            }
            // White Dwarfs - The HZ is so close, locking is almost guaranteed.
            'D' -> {
                when {
                    planetOrbitalPeriod > 5 -> 0.05 // Any period over a few days is impossible in the HZ.
                    else -> 0.0                     // Locked.
                }
            }
            // Brown Dwarfs: Extreme tidal locking risk is almost a certainty.
            'L', 'T', 'Y' -> {
                when {
                    planetOrbitalPeriod > 10 -> 0.1 // Any long period is still extremely likely to be locked.
                    else -> 0.05                    // Essentially guaranteed to be tidally locked.
                }
            }
            // M-dwarfs: Very high risk. Planets in the HZ are almost always locked.
            'M' -> when {
                planetOrbitalPeriod > 80 -> 1.0 // Low risk, although unlikely to be in the HZ
                planetOrbitalPeriod > 40 -> 0.5 // Moderate-to-high risk
                planetOrbitalPeriod > 20 -> 0.2 // Very high risk
                else -> 0.1                     // Almost certainly tidally locked
            }
            // K-dwarfs: High risk, but less severe than for M-dwarfs.
            'K' -> when {
                planetOrbitalPeriod > 100 -> 1.0 // Low risk
                planetOrbitalPeriod > 50 -> 0.7  // Moderate risk
                planetOrbitalPeriod > 25 -> 0.4  // High risk
                else -> 0.2                      // Probably tidally locked
            }
            // G, F, A, B, O, C, S, W etc. - The HZ is far out, low risk.
            else -> when {
                planetOrbitalPeriod > 150 -> 1.0 // Low risk
                planetOrbitalPeriod > 75 -> 0.9  // Very low risk
                planetOrbitalPeriod > 30 -> 0.5  // Increasing risk, and also likely too hot
                else -> 0.3                      // Probably tidally locked
            }
        }
    }

    /**
     * Calculates a score based on the radius of the planet with a graded falloff.
     * The radius is closely tied to the lower mass limit. A small planet is unlikely to have enough mass to sustain the geological activity
     * and atmospheric pressure needed for surface liquid water, whilst a large planet is very likely to be a mini-Neptune, possessing a thick,
     * crushing gas envelope that makes them uninhabitable on the surface.
     */
    private fun calculatePlanetRadiusScore(
        planetRadius: Double?,
        planetRadiusLowerLimit: Double,
        planetRadiusIdealUpperLimit: Double,
        planetRadiusMaxUpperLimit: Double
    ): Double? {
        if (planetRadius == null) return null
        return when {
            planetRadius < planetRadiusLowerLimit -> 0.0
            planetRadius in planetRadiusLowerLimit..planetRadiusIdealUpperLimit -> 1.0
            planetRadius in planetRadiusIdealUpperLimit..planetRadiusMaxUpperLimit -> {
                1.0 - (planetRadius - planetRadiusIdealUpperLimit) / (planetRadiusMaxUpperLimit - planetRadiusIdealUpperLimit)
            }

            else -> 0.0
        }.coerceIn(minimumValue = 0.0, maximumValue = 1.0)
    }

    /**
     * Calculates a score based on the mass of the planet with a graded falloff.
     * Mass is needed for gravity to hold onto a substantial atmosphere over billions of years, protecting it from being stripped away
     * by stellar winds, and to retain enough internal heat to power long-term geological activity like plate tectonics,
     * which is vital for cycling chemicals and nutrients.
     * Mars is a classic example of a body that lost most of its early atmosphere.
     * Conversely, a planet with a very strong gravity will likely hold onto a very thick hydrogen and helium atmosphere from its formation,
     * turning it into a gas-dominated mini-Neptune with no solid surface.
     */
    private fun calculatePlanetMassScore(
        planetMass: Double?,
        planetMassLowerLimit: Double,
        planetMassIdealUpperLimit: Double,
        planetMassMaxUpperLimit: Double
    ): Double? {
        if (planetMass == null) return null
        return when {
            planetMass < planetMassLowerLimit -> 0.0
            planetMass in planetMassLowerLimit..planetMassIdealUpperLimit -> 1.0
            planetMass in planetMassIdealUpperLimit..planetMassMaxUpperLimit -> {
                1.0 - (planetMass - planetMassIdealUpperLimit) / (planetMassMaxUpperLimit - planetMassIdealUpperLimit)
            }

            else -> 0.0
        }.coerceIn(minimumValue = 0.0, maximumValue = 1.0)
    }

    /**
     * Calculates a telluricity score based on density to determine if a planet is rocky.
     * Earth's density is ~5.51 g/cm^3. Rocky planets are typically > 3 g/cm^3.
     */
    private fun calculatePlanetTelluricityScore(planetDensity: Double?): Double? =
        when {
            planetDensity == null -> null // Unknown density
            planetDensity < 2.5 -> 0.0    // Very low density: Almost certainly a gas dwarf or ocean world. Not habitable on a surface.
            planetDensity < 3.5 -> 0.4    // Low density: Unlikely to be a standard rocky planet.
            planetDensity <= 7.0 -> 1.0   // High density: This is the sweet spot for terrestrial worlds.
            else -> 0.8                   // Very high density: Could be a super-Mercury. Still viable but less ideal.
        }

    /**
     * Calculates an eccentricity score.
     * A score of 1.0 is a perfect circular orbit. The score decreases as the orbit becomes more elliptical.
     * High eccentricity can lead to extreme temperature variations, making a planet less habitable.
     */
    private fun calculatePlanetEccentricityScore(planetEccentricity: Double?): Double? =
        when {
            planetEccentricity == null -> null // Unknown eccentricity
            planetEccentricity < 0.1 -> 1.0    // Nearly circular orbit, no penalty
            planetEccentricity < 0.3 -> 0.9    // Mildly elliptical, small penalty
            planetEccentricity < 0.5 -> 0.7    // Moderately elliptical, significant penalty
            planetEccentricity < 0.8 -> 0.3    // Highly elliptical, severe penalty
            else -> 0.0                        // Extreme orbit, likely uninhabitable
        }

    /**
     * Calculates a score based on whether the planet's temperature falls within a range that could support
     * liquid water with a plausible atmosphere.
     */
    private fun calculatePlanetTemperatureScore(planetTemperature: Double?): Double? =
        when (planetTemperature) {
            null -> null                 // Unknown temperature
            in 230.0..280.0 -> 1.0 // Ideal range
            in 180.0..330.0 -> 0.5 // Optimistic range
            else -> 0.0                  // Likely too cold or too hot
        }

    /**
     * Calculates a score based on the planet's axial tilt (obliquity).
     * A moderate tilt like Earth's is considered ideal for stable seasons.
     */
    private fun calculatePlanetObliquityScore(planetObliquity: Double?): Double? =
        when {
            planetObliquity == null -> null // Unknown obliquity
            planetObliquity > 60 -> 0.1     // Extreme seasons, like Uranus
            planetObliquity > 35 -> 0.7     // More pronounced seasons than Earth
            planetObliquity >= 15 -> 1.0    // Ideal range for stable seasons
            planetObliquity > 5 -> 0.8      // Less pronounced seasons
            else -> 0.4                     // Very few seasonal variations, less climate diversity
        }

    /**
     * Calculates a score of a planet's ability to protect itself, primarily via a magnetic field from 0.0 (unprotected) to 1.0 (well-protected).
     */
    private fun calculatePlanetProtectionScore(
        planetMass: Double?,
        planetDensity: Double?
    ): Double? {
        // Mass and density are used as a proxy for a large, molten iron core capable of generating a magnetosphere.
        if (planetMass == null || planetDensity == null) return null

        // Score based on mass (higher mass helps maintain a molten core)
        val massScore = when {
            planetMass < 0.5 -> 0.2
            planetMass < 1.0 -> 0.8
            else -> 1.0
        }

        // Score based on density (higher density suggests a large iron core)
        val densityScore = when {
            planetDensity < 3.0 -> 0.1 // Likely not rocky or a small core
            planetDensity < 5.0 -> 0.8 // Good indication of a significant core
            else -> 1.0                // Very dense, strong indication of a large iron core
        }

        return (massScore + densityScore) / 2.0
    }

    /**
     * Calculates a score based on the stellar spectral type.
     */
    private fun calculateStellarSpectralTypeScore(stellarHostSpectralType: String?): Double? {
        if (stellarHostSpectralType == null) return null

        val mainType = stellarHostSpectralType.firstOrNull()
        var spectralScore = when (mainType) {
            null -> return null   // Unknown spectral type
            'G' -> 1.0            // Ideal, like the Sun
            'K' -> 0.9            // Long-lived and stable, less UV radiation
            'F' -> 0.7            // Good candidates, but more UV radiation than the Sun
            'M' -> 0.3            // Very long-lived, prone to flaring
            'A' -> 0.4            // Short-lived and significant UV radiation
            'B' -> 0.2            // Very short-lived and high UV radiation
            'O' -> 0.1            // Extremely short-lived and high UV radiation
            'L', 'T', 'Y' -> 0.1  // Brown Dwarfs: Unstable HZ
            'D' -> 0.1            // White Dwarfs: Remnant cores, harsh radiation
            'C', 'S' -> 0.2       // Carbon/S-type stars: Evolved, unstable giants
            'W', 'Q', 'P' -> 0.05 // Catastrophically hostile or not stable stars
            else -> 0.3           // Other rare types
        }

        // A small penalty is applied for subtypes farther away from 5.
        // For the most habitable classes, stars in the middle of the range are often considered more stable than those at the extremes.
        val subtype = stellarHostSpectralType.getOrNull(index = 1)?.digitToIntOrNull()
        if (subtype != null && mainType in listOf('G', 'K', 'F')) {
            spectralScore -= abs(n = subtype - 5) * 0.02
        }

        // Apply penalty for non-main-sequence stars.
        if (stellarHostSpectralType.contains(other = "IV") ||
            stellarHostSpectralType.contains(other = "III") ||
            stellarHostSpectralType.contains(other = "II") ||
            stellarHostSpectralType.contains(other = "I")
        ) spectralScore *= 0.1

        return spectralScore
    }

    /**
     * Calculates a score based on the stellar effective temperature.
     * The score peaks at the Sun's temperature and decreases for hotter or cooler stars.
     */
    private fun calculateStellarEffectiveTemperatureScore(
        stellarHostEffectiveTemperature: Double?,
        stellarHostEffectiveTemperatureMaxDeviation: Double
    ): Double? {
        if (stellarHostEffectiveTemperature == null) return null // Unknown temperature
        val idealTemperature = 5780.0
        val difference = abs(x = stellarHostEffectiveTemperature - idealTemperature)
        val score = 1.0 - (difference / stellarHostEffectiveTemperatureMaxDeviation)
        return score.coerceIn(minimumValue = 0.1, maximumValue = 1.0)
    }

    /**
     * Calculates a score based on stellar mass, which is the primary
     * determinant of a star's lifetime and stability.
     */
    private fun calculateStellarMassScore(stellarHostMass: Double?): Double? =
        when {
            stellarHostMass == null -> null          // Unknown mass
            stellarHostMass > 2.0 -> 0.1             // Very massive, extremely short lifespan.
            stellarHostMass > 1.4 -> 0.4             // Massive, short lifespan.
            stellarHostMass in 0.8..1.4 -> 1.0 // Ideal mass range for lifetime and stability.
            stellarHostMass in 0.5..<0.8 -> 0.9   // Excellent long lifetime.
            else -> 0.4                              // Very long-lived, but corresponds to M-dwarfs with other issues.
        }

    /**
     * Calculates a score based on the metallicity.
     */
    private fun calculateStellarMetallicityScore(stellarHostMetallicity: Double?): Double? =
        when {
            stellarHostMetallicity == null -> null // Unknown metallicity
            stellarHostMetallicity >= 0.0 -> 1.0   // Metal-rich, good for planet formation
            stellarHostMetallicity < -0.5 -> 0.3   // Very metal-poor, less likely to form rocky planets
            else -> 0.8                            // Metal-poor, but acceptable
        }

    /**
     * Calculates a score based on stellar surface gravity.
     * High gravity indicates a stable main-sequence star, while low gravity indicates an unstable giant.
     */
    private fun calculateStellarGravityScore(stellarHostGravity: Double?): Double? =
        when {
            stellarHostGravity == null -> null // Unknown gravity
            stellarHostGravity >= 4.0 -> 1.0   // Indicates a compact, stable main-sequence star.
            stellarHostGravity > 3.5 -> 0.4    // Borderline, likely an unstable sub-giant.
            else -> 0.1                        // Low gravity, indicates an evolved giant. Unsuitable host.
        }

    /**
     * Calculates a score based on the stellar age.
     */
    private fun calculateStellarAgeScore(stellarHostAge: Double?): Double? =
        when {
            stellarHostAge == null -> null // Unknown age
            stellarHostAge < 0.5 -> 0.1    // Infant, likely unstable
            stellarHostAge < 1.0 -> 0.2    // Too young for complex life to have evolved
            stellarHostAge < 3.0 -> 0.8    // Young, potentially habitable
            stellarHostAge <= 6.0 -> 1.0   // Ideal age range, similar to our sun
            stellarHostAge <= 8.0 -> 0.9   // Still a good candidate
            stellarHostAge <= 10.0 -> 0.7  // Old, potential for instability
            else -> 0.2                    // Very old, likely past its stable phase
        }

    /**
     * Calculates a score based on a star's rotational velocity.
     * Fast rotation implies high stellar activity (flares) and youth, which is less habitable.
     */
    private fun calculateStellarActivityScore(stellarHostRotationalVelocity: Double?): Double? =
        when {
            stellarHostRotationalVelocity == null -> null // Unknown rotational velocity
            stellarHostRotationalVelocity < 4.0 -> 1.0    // Slow rotator, likely older and stable.
            stellarHostRotationalVelocity < 10.0 -> 0.7   // Moderately active.
            stellarHostRotationalVelocity < 25.0 -> 0.3   // Fast rotator, likely young and very active.
            else -> 0.1                                   // Extremely fast rotator, very hostile.
        }

    /**
     * Calculates a score based on a star's rotational period.
     */
    private fun calculateStellarRotationalPeriodScore(stellarHostRotationalPeriod: Double?): Double? =
        when {
            stellarHostRotationalPeriod == null -> null // Unknown rotational period
            stellarHostRotationalPeriod < 10.0 -> 0.4   // Very fast rotation, possible high activity/flares
            stellarHostRotationalPeriod < 25.0 -> 0.8   // Moderately fast rotation
            else -> 1.0                                 // Slower rotation, possibly more stability
        }

    /**
     * Calculate the possible planet type.
     */
    private fun calculatePlanetType(
        stellarHostSpectralType: String?,
        stellarHostAge: Double?,
        planetMass: Double?,
        planetRadius: Double?,
        planetDensity: Double?,
        planetOrbitalPeriod: Double?,
        planetOrbitAxis: Double?,
        planetEquilibriumTemperature: Double?,
        planetTidalLockingScore: Double?,
        rocheScore: Double?,
        habitableZoneScore: Double?,
        habitabilityScore: Double,
    ): PlanetType? {
        val foundationalType = when {
            // Gas Giants are the most massive planets, where runaway gas accretion has occurred.
            // This requires high mass (>80 M_Earth), large radius (>7 R_Earth), and low density.
            planetMass != null && planetMass > 80.0 && planetRadius != null && planetRadius > 7.0 && planetDensity != null && planetDensity < 2.0 -> {
                PlanetType.GAS_GIANT
            }

            // Ice Giants are compositionally distinct, less massive than Gas Giants but still large.
            // Defined by a mass of 10-80 M_Earth, a radius of 3.9-7 R_Earth, and higher density than Gas Giants.
            planetMass != null && planetMass in 10.0..80.0 && planetRadius != null && planetRadius in 3.8..7.0 && planetDensity != null && planetDensity >= 1.0 -> {
                PlanetType.ICE_GIANT
            }

            // Mini-Neptunes (Gas Dwarfs) have a significant gas envelope, defining them by their radius.
            // The "radius valley" (~1.7 R_Earth) separates them from rocky Super-Earths.
            // They have radii of 1.7-3.9 R_Earth and are not massive enough to be true giants (<20 M_Earth).
            planetMass != null && planetMass in 1.0..20.0 && planetRadius != null && planetRadius in 1.7..3.9 -> {
                PlanetType.MINI_NEPTUNE
            }

            // If a planet doesn't have a vast gas envelope, it's terrestrial.
            // This is defined by a high density, which indicates a rocky/iron composition even if the planet is massive.
            (planetRadius != null && planetRadius < 1.7 && planetDensity != null && planetDensity >= 1.5) || planetDensity != null && planetDensity > 3.0 -> {
                PlanetType.TERRESTRIAL_PLANET
            }

            else -> null
        }

        // Transient or evolutionary states
        if (stellarHostAge != null && stellarHostAge < 0.01) return PlanetType.PROTOPLANET
        if (foundationalType == PlanetType.TERRESTRIAL_PLANET && planetDensity != null && planetDensity > 7.0 && planetEquilibriumTemperature != null && planetEquilibriumTemperature > 1500.0) {
            return PlanetType.CHTHONIAN_PLANET
        }

        // Habitability assessment
        if (foundationalType == PlanetType.TERRESTRIAL_PLANET && habitableZoneScore != null && habitableZoneScore > 0.5) {
            when {
                planetMass != null && planetMass in 1.5..3.0 && planetRadius != null && planetRadius in 1.2..1.5 &&
                        habitabilityScore > 0.9 && habitableZoneScore > 0.7 && stellarHostSpectralType?.startsWith(prefix = "K") == true &&
                        stellarHostAge != null && stellarHostAge in 5.0..8.0 -> return PlanetType.SUPERHABITABLE_PLANET

                planetMass != null && planetMass in 0.8..1.2 && planetRadius != null && planetRadius in 0.9..1.1 &&
                        habitabilityScore > 0.8 && habitableZoneScore > 0.6 && stellarHostSpectralType?.startsWith(prefix = "G") == true -> return PlanetType.EARTH_ANALOG_PLANET

                habitabilityScore > 0.7 && habitableZoneScore > 0.5 -> return PlanetType.EARTH_LIKE_PLANET
                habitabilityScore < 0.2 -> return PlanetType.BARREN_PLANET
            }
        }

        // Specific states for Gas Giants
        if (foundationalType == PlanetType.GAS_GIANT && planetEquilibriumTemperature != null) {
            when {
                planetEquilibriumTemperature > 2200.0 -> return PlanetType.ULTRA_HOT_JUPITER
                planetEquilibriumTemperature > 1000.0 -> return PlanetType.HOT_JUPITER
                planetEquilibriumTemperature < 150.0 -> return PlanetType.AMMONIA_CLOUDS_GAS_GIANT
                planetEquilibriumTemperature in 150.0..250.0 -> return PlanetType.WATER_CLOUDS_GAS_GIANT
                planetEquilibriumTemperature in 350.0..800.0 -> return PlanetType.CLOUDLESS_GAS_GIANT
                planetEquilibriumTemperature in 900.0..1400.0 -> return PlanetType.ALKALI_METAL_CLOUDS_GAS_GIANT
                planetEquilibriumTemperature > 1400.0 -> return PlanetType.SILICATE_CLOUDS_GAS_GIANT
            }
        }

        // Specific states for Ice Giants
        if (planetEquilibriumTemperature != null && (foundationalType == PlanetType.ICE_GIANT || foundationalType == PlanetType.MINI_NEPTUNE)) {
            if (planetEquilibriumTemperature > 1200) return PlanetType.ULTRA_HOT_NEPTUNE
            if (planetEquilibriumTemperature > 1000) return PlanetType.HOT_NEPTUNE
        }

        // Specific states for Terrestrial Planets
        if (foundationalType == PlanetType.TERRESTRIAL_PLANET && planetEquilibriumTemperature != null) {
            // Tidally locked states are more specific than general thermal states
            if (planetTidalLockingScore != null && planetTidalLockingScore < 0.2) {
                return when {
                    planetEquilibriumTemperature > 350.0 -> PlanetType.HOT_EYEBALL_PLANET
                    planetEquilibriumTemperature < 200.0 -> PlanetType.COLD_EYEBALL_PLANET
                    else -> PlanetType.EYEBALL_PLANET
                }
            }
            // General thermal states if not tidally locked
            when {
                planetEquilibriumTemperature > 1800.0 -> return PlanetType.LAVA_PLANET
                planetEquilibriumTemperature < 200.0 -> return PlanetType.ICE_PLANET
                planetEquilibriumTemperature in 350.0..500.0 -> return PlanetType.DESERT_PLANET
            }
        }

        // Composition based
        if (planetDensity != null) {
            if (planetMass != null && planetMass > 1.0) {
                when {
                    planetDensity < 0.1 && planetRadius != null && planetRadius > 4.0 -> return PlanetType.SUPER_PUFF_PLANET
                    planetDensity < 0.7 -> return PlanetType.PUFFY_PLANET
                }
            }

            if (foundationalType == PlanetType.TERRESTRIAL_PLANET && planetDensity > 7.5) return PlanetType.IRON_PLANET

            if ((foundationalType == PlanetType.TERRESTRIAL_PLANET || foundationalType == PlanetType.MINI_NEPTUNE) && planetDensity in 1.5..3.0) {
                if (planetEquilibriumTemperature != null && planetEquilibriumTemperature < 273) return PlanetType.SUBSURFACE_OCEAN_PLANET
                return PlanetType.OCEAN_PLANET
            }
        }

        if (planetOrbitalPeriod != null && planetOrbitalPeriod < 1.0) return PlanetType.ULTRA_SHORT_PERIOD_PLANET

        // Size subtypes
        if (planetMass != null) {
            when (foundationalType) {
                PlanetType.GAS_GIANT -> if (planetMass > JUPITER_MASS_IN_EARTHS) return PlanetType.SUPER_JUPITER
                PlanetType.ICE_GIANT -> if (planetMass in 20.0..80.0) return PlanetType.SUPER_NEPTUNE
                PlanetType.TERRESTRIAL_PLANET -> {
                    when {
                        planetMass < 0.1 -> return PlanetType.CRATER_PLANET
                        planetMass <= 1.0 -> return PlanetType.SUB_EARTH
                        planetMass > 10.0 -> return PlanetType.MEGA_EARTH
                        planetMass > 1.0 -> return PlanetType.SUPER_EARTH
                    }
                }

                else -> {}
            }
        }

        // Non-habitable planets
        if (rocheScore == 0.0) return PlanetType.DISRUPTED_PLANET
        if (planetOrbitAxis != null && planetOrbitAxis < 0.02) return PlanetType.ELLIPSOID_PLANET

        return foundationalType
    }

    /**
     * Calculates the approximate density in g/cm^3 from mass and radius assuming the volume of a sphere = 4/3 * pi * r^3
     */
    private fun calculateDensity(planetMass: Double?, planetRadius: Double?): Double? {
        if (planetMass == null || planetRadius == null) return null
        val volumeInEarths = (4.0 / 3.0) * PI * planetRadius.pow(n = 3)
        return (planetMass / volumeInEarths) * EARTH_AVERAGE_DENSITY
    }
}
