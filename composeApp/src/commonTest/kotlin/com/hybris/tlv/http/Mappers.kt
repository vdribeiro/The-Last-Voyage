package com.hybris.tlv.http

import com.hybris.tlv.usecase.space.lightYearsToParsecs
import com.hybris.tlv.usecase.space.model.ExoplanetJson
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.model.StellarHostJson
import com.hybris.tlv.usecase.space.sunGravityToStellarHostGravity

internal fun StellarHost.toStellarHostJson(): StellarHostJson =
    StellarHostJson(
        stellarHostName = name,
        stellarHostSystemName = systemName,
        stellarHostSpectralType = spectralType,
        stellarHostEffectiveTemperature = effectiveTemperature,
        stellarHostRadius = radius,
        stellarHostMass = mass,
        stellarHostMetallicity = metallicity,
        stellarHostLuminosity = luminosity,
        stellarHostGravity = gravity?.sunGravityToStellarHostGravity(),
        stellarHostAge = age,
        stellarHostDensity = density,
        stellarHostRotationalVelocity = rotationalVelocity,
        stellarHostRotationalPeriod = rotationalPeriod,
        stellarHostDistance = distance?.lightYearsToParsecs(),
        stellarHostRa = ra,
        stellarHostDec = dec
    )

internal fun Planet.toExoplanetJson(stellarHost: StellarHost): ExoplanetJson =
    ExoplanetJson(
        stellarHostName = stellarHost.name,
        stellarHostSpectralType = stellarHost.spectralType,
        stellarHostEffectiveTemperature = stellarHost.effectiveTemperature,
        stellarHostRadius = stellarHost.radius,
        stellarHostMass = stellarHost.mass,
        stellarHostMetallicity = stellarHost.metallicity,
        stellarHostLuminosity = stellarHost.luminosity,
        stellarHostGravity = stellarHost.gravity?.sunGravityToStellarHostGravity(),
        stellarHostAge = stellarHost.age,
        stellarHostDensity = stellarHost.density,
        stellarHostRotationalVelocity = stellarHost.rotationalVelocity,
        stellarHostRotationalPeriod = stellarHost.rotationalPeriod,
        stellarHostDistance = stellarHost.distance?.lightYearsToParsecs(),
        stellarHostRa = stellarHost.ra,
        stellarHostDec = stellarHost.dec,
        planetName = name,
        planetStatus = status.name,
        planetOrbitalPeriod = orbitalPeriod,
        planetOrbitAxis = orbitAxis,
        planetRadius = radius,
        planetMass = mass,
        planetDensity = density,
        planetEccentricity = eccentricity,
        planetInsolationFlux = insolationFlux,
        planetEquilibriumTemperature = equilibriumTemperature,
        planetOccultationDepth = occultationDepth,
        planetInclination = inclination,
        planetObliquity = obliquity,
        planetProjectedObliquity = obliquity
    )