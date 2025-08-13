package com.hybris.tlv.usecase.space.formula

import com.hybris.tlv.usecase.space.formula.Constants.EARTH_AVERAGE_DENSITY
import com.hybris.tlv.usecase.space.formula.Constants.EARTH_BOND_ALBEDO
import com.hybris.tlv.usecase.space.formula.Constants.EARTH_DAY_IN_SECONDS
import com.hybris.tlv.usecase.space.formula.Constants.EARTH_ORBITAL_PERIOD_IN_DAYS
import com.hybris.tlv.usecase.space.formula.Constants.EARTH_RADIUS_IN_SUNS
import com.hybris.tlv.usecase.space.formula.Constants.GRAVITATIONAL_CONSTANT
import com.hybris.tlv.usecase.space.formula.Constants.SUN_EFFECTIVE_TEMPERATURE
import com.hybris.tlv.usecase.space.formula.Constants.SUN_EFFECTIVE_TEMPERATURE_1AU
import com.hybris.tlv.usecase.space.formula.Constants.SUN_MASS_IN_KG
import com.hybris.tlv.usecase.space.formula.Constants.SUN_RADIUS_IN_AU
import com.hybris.tlv.usecase.space.formula.Constants.SUN_RADIUS_IN_EARTH_RADII
import com.hybris.tlv.usecase.space.formula.Constants.SUN_RADIUS_IN_METERS
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

internal object DerivedData {

    /**
     * Derive missing data.
     */
    fun derive(stellarHosts: List<StellarHost>): List<StellarHost> = stellarHosts.map {
        deriveStellarHost(stellarHost = it).apply {
            planets.addAll(elements = it.planets.map { planet ->
                derivePlanet(stellarHost = this, planet = planet)
            })
        }
    }

    /**
     * Iteratively runs all derivation functions until no new data can be found.
     */
    private fun deriveStellarHost(stellarHost: StellarHost): StellarHost {
        var dataWasDerived: Boolean
        var derivedStellarHost = stellarHost.copy()

        do {
            dataWasDerived = false

            if (derivedStellarHost.density == null) {
                calculateStellarHostDensity(
                    stellarHostMass = derivedStellarHost.mass,
                    stellarHostRadius = derivedStellarHost.radius
                )?.let {
                    derivedStellarHost = derivedStellarHost.copy(density = it)
                    dataWasDerived = true
                }
            }

            if (derivedStellarHost.luminosity == null) {
                calculateStellarHostLuminosity(
                    stellarHostRadius = derivedStellarHost.radius,
                    stellarHostEffectiveTemperature = derivedStellarHost.effectiveTemperature
                )?.let {
                    derivedStellarHost = derivedStellarHost.copy(luminosity = it)
                    dataWasDerived = true
                }
            }

            if (derivedStellarHost.gravity == null) {
                calculateStellarHostSurfaceGravity(
                    stellarHostMass = derivedStellarHost.mass,
                    stellarHostRadius = derivedStellarHost.radius
                )?.let {
                    derivedStellarHost = derivedStellarHost.copy(gravity = it)
                    dataWasDerived = true
                }
            }

            if (derivedStellarHost.rotationalVelocity == null) {
                calculateStellarHostRotationalVelocity(
                    stellarHostRadius = derivedStellarHost.radius,
                    stellarHostRotationalPeriod = derivedStellarHost.rotationalPeriod
                )?.let {
                    derivedStellarHost = derivedStellarHost.copy(rotationalVelocity = it)
                    dataWasDerived = true
                }
            }

            if (derivedStellarHost.rotationalPeriod == null) {
                calculateStellarHostRotationalPeriod(
                    stellarHostRadius = derivedStellarHost.radius,
                    stellarHostRotationalVelocity = derivedStellarHost.rotationalVelocity
                )?.let {
                    derivedStellarHost = derivedStellarHost.copy(rotationalPeriod = it)
                    dataWasDerived = true
                }
            }
        } while (dataWasDerived)

        return derivedStellarHost
    }

    /**
     * Iteratively runs all derivation functions until no new data can be found.
     */
    fun derivePlanet(stellarHost: StellarHost, planet: Planet): Planet {
        var dataWasDerived: Boolean
        var derivedPlanet = planet.copy()

        do {
            dataWasDerived = false

            if (derivedPlanet.radius == null) {
                calculatePlanetRadius(
                    stellarHostRadius = stellarHost.radius,
                    planetOccultationDepth = derivedPlanet.occultationDepth
                )?.let {
                    derivedPlanet = derivedPlanet.copy(radius = it)
                    dataWasDerived = true
                }
            }

            if (derivedPlanet.density == null) {
                calculatePlanetDensity(
                    planetMass = derivedPlanet.mass,
                    planetRadius = derivedPlanet.radius
                )?.let {
                    derivedPlanet = derivedPlanet.copy(density = it)
                    dataWasDerived = true
                }
            }

            if (derivedPlanet.equilibriumTemperature == null) {
                calculatePlanetTemperature(
                    stellarHostEffectiveTemperature = stellarHost.effectiveTemperature,
                    stellarHostRadius = stellarHost.radius,
                    stellarHostLuminosity = stellarHost.luminosity,
                    planetOrbitAxis = derivedPlanet.orbitAxis,
                    planetOccultationDepth = derivedPlanet.occultationDepth,
                    planetRadius = derivedPlanet.radius
                )?.let {
                    derivedPlanet = derivedPlanet.copy(equilibriumTemperature = it)
                    dataWasDerived = true
                }
            }

            if (derivedPlanet.insolationFlux == null) {
                calculatePlanetInsolationFlux(
                    stellarHostLuminosity = stellarHost.luminosity,
                    planetOrbitAxis = derivedPlanet.orbitAxis
                )?.let {
                    derivedPlanet = derivedPlanet.copy(insolationFlux = it)
                    dataWasDerived = true
                }
            }

            if (derivedPlanet.orbitAxis == null) {
                calculatePlanetOrbitAxis(
                    stellarHostMass = stellarHost.mass,
                    planetOrbitalPeriod = derivedPlanet.orbitalPeriod
                )?.let {
                    derivedPlanet = derivedPlanet.copy(orbitAxis = it)
                    dataWasDerived = true
                }
            }

            if (derivedPlanet.orbitalPeriod == null) {
                calculatePlanetOrbitalPeriod(
                    stellarHostMass = stellarHost.mass,
                    planetOrbitAxis = derivedPlanet.orbitAxis
                )?.let {
                    derivedPlanet = derivedPlanet.copy(orbitalPeriod = it)
                    dataWasDerived = true
                }
            }
        } while (dataWasDerived)

        return derivedPlanet
    }

    /**
     * Calculates the star's average density in g/cm³ assuming the volume of a sphere.
     */
    private fun calculateStellarHostDensity(
        stellarHostMass: Double?,
        stellarHostRadius: Double?
    ): Double? {
        if (stellarHostMass == null || stellarHostRadius == null) return null
        val massKg = stellarHostMass * SUN_MASS_IN_KG
        val radiusM = stellarHostRadius * SUN_RADIUS_IN_METERS
        val volumeM3 = (4.0 / 3.0) * PI * radiusM.pow(n = 3)
        val densityKgM3 = massKg / volumeM3
        return densityKgM3 * 0.001
    }

    /**
     * Calculates the star's luminosity in Solar luminosities using the Stefan-Boltzmann Law.
     */
    private fun calculateStellarHostLuminosity(
        stellarHostRadius: Double?,
        stellarHostEffectiveTemperature: Double?
    ): Double? {
        if (stellarHostRadius == null || stellarHostEffectiveTemperature == null) return null
        return stellarHostRadius.pow(n = 2) * (stellarHostEffectiveTemperature / SUN_EFFECTIVE_TEMPERATURE).pow(n = 4)
    }

    /**
     * Calculates the star's surface gravity as log(g) in cgs units (cm/s²).
     */
    private fun calculateStellarHostSurfaceGravity(
        stellarHostMass: Double?,
        stellarHostRadius: Double?
    ): Double? {
        if (stellarHostMass == null || stellarHostRadius == null) return null
        val massKg = stellarHostMass * SUN_MASS_IN_KG
        val radiusM = stellarHostRadius * SUN_RADIUS_IN_METERS
        return (GRAVITATIONAL_CONSTANT * massKg) / radiusM.pow(n = 2)
    }

    /**
     * Calculates the star's equatorial rotational velocity in km/s.
     */
    fun calculateStellarHostRotationalVelocity(
        stellarHostRadius: Double?,
        stellarHostRotationalPeriod: Double?
    ): Double? {
        if (stellarHostRadius == null || stellarHostRotationalPeriod == null) return null
        val radiusM = stellarHostRadius * SUN_RADIUS_IN_METERS
        val periodSeconds = stellarHostRotationalPeriod * EARTH_DAY_IN_SECONDS
        val circumferenceM = 2 * PI * radiusM
        val velocityMps = circumferenceM / periodSeconds
        return velocityMps / 1000.0
    }

    /**
     * Calculates the star's rotational period in Earth days.
     */
    fun calculateStellarHostRotationalPeriod(
        stellarHostRadius: Double?,
        stellarHostRotationalVelocity: Double?,
    ): Double? {
        if (stellarHostRadius == null || stellarHostRotationalVelocity == null) return null
        val radiusM = stellarHostRadius * SUN_RADIUS_IN_METERS
        val velocityMps = stellarHostRotationalVelocity * 1000.0
        val circumferenceM = 2 * PI * radiusM
        val periodSeconds = circumferenceM / velocityMps
        return periodSeconds / EARTH_DAY_IN_SECONDS
    }

    /**
     * Calculates the planet's radius in Earth radii based on transit data.
     */
    private fun calculatePlanetRadius(
        stellarHostRadius: Double?,
        planetOccultationDepth: Double?
    ): Double? {
        if (stellarHostRadius == null || planetOccultationDepth == null) return null
        return stellarHostRadius * sqrt(x = planetOccultationDepth) * EARTH_RADIUS_IN_SUNS
    }

    /**
     * Calculates the planet's approximate density in g/cm^3 from mass and radius assuming the volume of a sphere.
     */
    private fun calculatePlanetDensity(planetMass: Double?, planetRadius: Double?): Double? {
        if (planetMass == null || planetRadius == null) return null
        val volumeInEarths = (4.0 / 3.0) * PI * planetRadius.pow(n = 3)
        return (planetMass / volumeInEarths) * EARTH_AVERAGE_DENSITY
    }

    /**
     * Calculate a planet's measured day-side temperature in Kelvin from occultation depth (the depth of the secondary eclipse).
     * This is done by measuring the drop in brightness when the planet passes behind its star and assumes the occultation was measured
     * in the infrared where the planet's thermal emission is dominant.
     * This calculation is based on the Stefan-Boltzmann Law, which states that the total energy radiated by a black body is proportional
     * to the fourth power of its temperature.
     * If occultation data is unavailable, it falls back to a calculation based on the planet semi-major axis, and if that also fails,
     * it uses stellar luminosity.
     */
    private fun calculatePlanetTemperature(
        stellarHostEffectiveTemperature: Double?,
        stellarHostRadius: Double?,
        stellarHostLuminosity: Double?,
        planetOrbitAxis: Double?,
        planetOccultationDepth: Double?,
        planetRadius: Double?,
    ): Double? {
        if (stellarHostEffectiveTemperature != null && stellarHostRadius != null && planetOccultationDepth != null && planetRadius != null) {
            val radiusRatio = planetRadius / (stellarHostRadius * SUN_RADIUS_IN_EARTH_RADII)
            val radiusRatioSq = radiusRatio.pow(n = 2)
            val ratio = planetOccultationDepth / radiusRatioSq
            return stellarHostEffectiveTemperature * ratio.pow(x = 0.25)
        }

        if (stellarHostEffectiveTemperature != null && stellarHostRadius != null && planetOrbitAxis != null) {
            val stellarRadiusInAu = stellarHostRadius * SUN_RADIUS_IN_AU
            val radiusRatioFactor = sqrt(x = stellarRadiusInAu / (2 * planetOrbitAxis))
            val albedoFactor = (1 - EARTH_BOND_ALBEDO).pow(x = 0.25)
            return stellarHostEffectiveTemperature * radiusRatioFactor * albedoFactor
        }

        if (stellarHostLuminosity == null || planetOrbitAxis == null) return null
        return SUN_EFFECTIVE_TEMPERATURE_1AU * (((1 - EARTH_BOND_ALBEDO) * stellarHostLuminosity).pow(x = 0.25) / sqrt(x = planetOrbitAxis))
    }

    /**
     * Calculate the planet's insolation flux relative to Earth.
     */
    private fun calculatePlanetInsolationFlux(
        stellarHostLuminosity: Double?,
        planetOrbitAxis: Double?
    ): Double? {
        if (stellarHostLuminosity == null || planetOrbitAxis == null) return null
        return stellarHostLuminosity / planetOrbitAxis.pow(n = 2)
    }

    /**
     * Calculates the planet's semi-major axis in AU using Kepler's Third Law.
     */
    private fun calculatePlanetOrbitAxis(
        stellarHostMass: Double?,
        planetOrbitalPeriod: Double?
    ): Double? {
        if (stellarHostMass == null || planetOrbitalPeriod == null) return null
        val periodInYears = planetOrbitalPeriod / EARTH_ORBITAL_PERIOD_IN_DAYS
        return (periodInYears.pow(n = 2) * stellarHostMass).pow(x = 1.0 / 3.0)
    }

    /**
     * Calculate the planet's orbital period in Earth days using Kepler's Third Law.
     */
    private fun calculatePlanetOrbitalPeriod(
        stellarHostMass: Double?,
        planetOrbitAxis: Double?,
    ): Double? {
        if (stellarHostMass == null || planetOrbitAxis == null) return null
        val periodInYears = sqrt(x = planetOrbitAxis.pow(n = 3) / stellarHostMass)
        return periodInYears * EARTH_ORBITAL_PERIOD_IN_DAYS
    }
}
