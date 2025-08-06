package com.hybris.tlv.mock

import com.hybris.tlv.datetime.now
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.model.Precondition
import com.hybris.tlv.usecase.credits.model.Credits
import com.hybris.tlv.usecase.credits.model.CreditsType
import com.hybris.tlv.usecase.earth.model.Catastrophe
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.HABITABLE_ZONE_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_ECCENTRICITY_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_ESI_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_MASS_IDEAL_UPPER_LIMIT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_MASS_LOWER_LIMIT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_MASS_MAX_UPPER_LIMIT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_MASS_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_OBLIQUITY_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_PROTECTION_WEIGHT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_RADIUS_IDEAL_UPPER_LIMIT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_RADIUS_LOWER_LIMIT
import com.hybris.tlv.usecase.exoplanet.ExoplanetGateway.Companion.PLANET_RADIUS_MAX_UPPER_LIMIT
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
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.model.TravelOutcome

internal val catastrophes = listOf(
    Catastrophe(
        id = "asteroid_impact",
        name = "catastrophe__asteroid_impact",
        description = "catastrophe__asteroid_impact_description",
    ),
    Catastrophe(
        id = "nuclear_war",
        name = "catastrophe__nuclear_war",
        description = "catastrophe__nuclear_war_description",
    ),
)

internal val engines = listOf(
    Engine(
        id = "alcubierre_drive",
        name = "engine__alcubierre_drive",
        description = "engine__alcubierre_drive_description",
        velocity = 1.0,
    ),
    Engine(
        id = "liquid_fuel_rocket",
        name = "engine__liquid_fuel_rocket",
        description = "engine__liquid_fuel_rocket_description",
        velocity = 0.000014677,
    ),
    Engine(
        id = "solar_sail",
        name = "engine__solar_sail",
        description = "engine__solar_sail_description",
        velocity = 0.5,
    ),
    Engine(
        id = "wormhole_generator",
        name = "engine__wormhole_generator",
        description = "engine__wormhole_generator_description",
        velocity = 299000000.0,
    )
)

internal val stellarHosts = listOf(
    StellarHost(
        id = "sol",
        systemName = "Sol",
        name = "Sol",
        spectralType = "G2V",
        effectiveTemperature = 5778.0,
        radius = 1.0,
        mass = 1.0,
        metallicity = 0.0,
        luminosity = 1.0,
        gravity = 1.0,
        age = 4.6,
        density = 1.410,
        rotationalVelocity = 2.0,
        rotationalPeriod = 25.05,
        distance = 0.0,
        ra = 0.0,
        dec = 0.0
    ),
    StellarHost(
        id = "proxima_cen",
        systemName = "Alpha Centauri",
        name = "Proxima Centauri",
        spectralType = "M5.5 V",
        effectiveTemperature = 2900.0,
        radius = 0.141,
        mass = 0.1221,
        metallicity = null,
        luminosity = -2.8,
        gravity = 5.3201025,
        age = null,
        density = 48.7626491,
        rotationalVelocity = null,
        rotationalPeriod = 90.0,
        distance = 4.2439092564,
        ra = 217.3934657,
        dec = -62.6761821
    ),
    StellarHost(
        id = "alf_cen_b",
        systemName = "Alpha Centauri",
        name = "Alpha Centauri B",
        spectralType = "K0V",
        effectiveTemperature = 5178.0,
        radius = 0.88,
        mass = 0.88,
        metallicity = 0.23,
        luminosity = -0.284,
        gravity = 1.2083702,
        age = 8.0,
        density = null,
        rotationalVelocity = 0.9,
        rotationalPeriod = 42.0,
        distance = 4.39492274596,
        ra = 219.9141283,
        dec = -60.8394714
    ),
    StellarHost(
        id = "alf_cen_a",
        systemName = "Alpha Centauri",
        name = "Alpha Centauri A",
        spectralType = "G2.0V",
        effectiveTemperature = 5801.0,
        radius = 1.25,
        mass = 1.06,
        metallicity = 0.21,
        luminosity = 0.207,
        gravity = 0.7281150,
        age = 7.84,
        density = null,
        rotationalVelocity = 2.3,
        rotationalPeriod = 29.0,
        distance = 4.39492274596,
        ra = 219.9204103,
        dec = -60.8351471
    ),
)

internal val planets = listOf(
    Planet(
        id = "1mercury",
        name = "Mercury",
        stellarHostId = "sol",
        status = PlanetStatus.CONFIRMED,
        orbitalPeriod = 88.0,
        orbitAxis = 0.387,
        radius = 0.383,
        mass = 0.055,
        density = 5.429,
        eccentricity = 0.206,
        insolationFlux = 6.674,
        equilibriumTemperature = 440.0,
        occultationDepth = 0.000012,
        inclination = 7.01,
        obliquity = 0.034,
    ),
    Planet(
        id = "2venus",
        name = "Venus",
        stellarHostId = "sol",
        status = PlanetStatus.CONFIRMED,
        orbitalPeriod = 224.7,
        orbitAxis = 0.723,
        radius = 0.950,
        mass = 0.815,
        density = 5.243,
        eccentricity = 0.007,
        insolationFlux = 1.911,
        equilibriumTemperature = 228.0,
        occultationDepth = 0.000076,
        inclination = 3.39,
        obliquity = 177.4,
    ),
    Planet(
        id = "3earth",
        name = "Earth",
        stellarHostId = "sol",
        status = PlanetStatus.CONFIRMED,
        orbitalPeriod = 365.2,
        orbitAxis = 1.000,
        radius = 1.0,
        mass = 1.0,
        density = 5.514,
        eccentricity = 0.017,
        insolationFlux = 1.000,
        equilibriumTemperature = 255.0,
        occultationDepth = 0.000084,
        inclination = 0.0,
        obliquity = 23.4,
    ),
    Planet(
        id = "4mars",
        name = "Mars",
        stellarHostId = "sol",
        status = PlanetStatus.CONFIRMED,
        orbitalPeriod = 687.0,
        orbitAxis = 1.524,
        radius = 0.532,
        mass = 0.107,
        density = 3.934,
        eccentricity = 0.094,
        insolationFlux = 0.430,
        equilibriumTemperature = 210.0,
        occultationDepth = 0.000024,
        inclination = 1.85,
        obliquity = 25.2,
    ),
    Planet(
        id = "5jupiter",
        name = "Jupiter",
        stellarHostId = "sol",
        status = PlanetStatus.CONFIRMED,
        orbitalPeriod = 4331.0,
        orbitAxis = 5.204,
        radius = 11.209,
        mass = 317.83,
        density = 1.326,
        eccentricity = 0.049,
        insolationFlux = 0.037,
        equilibriumTemperature = 110.0,
        occultationDepth = 0.010531,
        inclination = 1.31,
        obliquity = 3.1,
    ),
    Planet(
        id = "6saturn",
        name = "Saturn",
        stellarHostId = "sol",
        status = PlanetStatus.CONFIRMED,
        orbitalPeriod = 10747.0,
        orbitAxis = 9.582,
        radius = 9.449,
        mass = 95.16,
        density = 0.687,
        eccentricity = 0.052,
        insolationFlux = 0.011,
        equilibriumTemperature = 81.0,
        occultationDepth = 0.007515,
        inclination = 2.49,
        obliquity = 26.7,
    ),
    Planet(
        id = "7uranus",
        name = "Uranus",
        stellarHostId = "sol",
        status = PlanetStatus.CONFIRMED,
        orbitalPeriod = 30589.0,
        orbitAxis = 19.189,
        radius = 4.007,
        mass = 14.54,
        density = 1.270,
        eccentricity = 0.047,
        insolationFlux = 0.003,
        equilibriumTemperature = 58.0,
        occultationDepth = 0.001347,
        inclination = 0.77,
        obliquity = 97.8,
    ),
    Planet(
        id = "8neptune",
        name = "Neptune",
        stellarHostId = "sol",
        status = PlanetStatus.CONFIRMED,
        orbitalPeriod = 59800.0,
        orbitAxis = 30.070,
        radius = 3.883,
        mass = 17.15,
        density = 1.638,
        eccentricity = 0.010,
        insolationFlux = 0.001,
        equilibriumTemperature = 46.0,
        occultationDepth = 0.001264,
        inclination = 1.77,
        obliquity = 28.3,
    ),
    Planet(
        id = "proxima_cen_b",
        name = "Proxima Centauri b",
        stellarHostId = "proxima_cen",
        status = PlanetStatus.CONFIRMED,
        orbitalPeriod = 11.1868,
        orbitAxis = 0.04856,
        radius = 1.03,
        mass = 1.07,
        density = 5.38,
        eccentricity = 0.02,
        insolationFlux = 0.65,
        equilibriumTemperature = 234.0,
        occultationDepth = null,
        inclination = null,
        obliquity = null,
    ),
    Planet(
        id = "proxima_cen_c",
        name = "Proxima Centauri c",
        stellarHostId = "proxima_cen",
        status = PlanetStatus.CANDIDATE,
        orbitalPeriod = 1900.0,
        orbitAxis = 1.48,
        radius = null,
        mass = 5.8,
        density = null,
        eccentricity = 0.0,
        insolationFlux = null,
        equilibriumTemperature = 39.0,
        occultationDepth = null,
        inclination = null,
        obliquity = null,
    ),
    Planet(
        id = "proxima_cen_d",
        name = "Proxima Centauri d",
        stellarHostId = "proxima_cen",
        status = PlanetStatus.CANDIDATE,
        orbitalPeriod = 5.122,
        orbitAxis = 0.02885,
        radius = null,
        mass = 0.26,
        density = null,
        eccentricity = 0.040,
        insolationFlux = null,
        equilibriumTemperature = null,
        occultationDepth = null,
        inclination = null,
        obliquity = null,
    ),
    Planet(
        id = "alf_cen_b_b",
        name = "Alpha Centauri B b",
        stellarHostId = "alf_cen_b",
        status = PlanetStatus.FALSE,
        orbitalPeriod = 3.2357,
        orbitAxis = null,
        radius = null,
        mass = 1.13,
        density = null,
        eccentricity = 0.0,
        insolationFlux = null,
        equilibriumTemperature = null,
        occultationDepth = null,
        inclination = null,
        obliquity = null,
    ),
)

internal val events = listOf(
    Event(
        id = "engine_misfire",
        name = "event__engine_misfire",
        description = "event__engine_misfire_description",
        parentId = null,
        outcome = TravelOutcome(
            materials = -10,
            fuel = -3,
        ),
    ),
    Event(
        id = "solar_flare",
        name = "event__solar_flare",
        description = "event__solar_flare_description",
        parentId = null,
        outcome = TravelOutcome(
            materials = -5,
            fuel = -3,
        ),
    ),
    Event(
        id = "a_close_pass",
        name = "event__a_close_pass",
        description = "event__a_close_pass_description",
        parentId = null,
        outcome = null
    ),
    Event(
        id = "a_close_pass_for_science",
        name = "event__a_close_pass_for_science",
        description = "event__a_close_pass_for_science_description",
        parentId = "a_close_pass",
        outcome = TravelOutcome(
            cryopods = -15,
            fuel = +10
        )
    ),
    Event(
        id = "a_close_pass_ignore",
        name = "event__a_close_pass_ignore",
        description = "event__a_close_pass_ignore_description",
        parentId = "a_close_pass",
        outcome = null
    )
)

internal val gameSession = GameSession(
    id = "1",
    utc = now(),
    assignedPoints = 10,
    yearsTraveled = 100.0,
    sensorRange = 5,
    integrity = 80,
    fuel = 100,
    materials = 90,
    cryopods = 150,
    currentStellarHostId = stellarHosts.first().id,
    visitedStellarHosts = emptySet(),
    launchedEvents = emptySet(),
    settledPlanetId = null,
    finalHabitability = null,
    score = null,
    rocheWeight = ROCHE_WEIGHT,
    habitableZoneWeight = HABITABLE_ZONE_WEIGHT,
    planetRadiusWeight = PLANET_RADIUS_WEIGHT,
    planetMassWeight = PLANET_MASS_WEIGHT,
    planetTelluricityWeight = PLANET_TELLURICITY_WEIGHT,
    planetEccentricityWeight = PLANET_ECCENTRICITY_WEIGHT,
    planetTemperatureWeight = PLANET_TEMPERATURE_WEIGHT,
    planetObliquityWeight = PLANET_OBLIQUITY_WEIGHT,
    planetEsiWeight = PLANET_ESI_WEIGHT,
    stellarSpectralTypeWeight = STELLAR_SPECTRAL_TYPE_WEIGHT,
    stellarMassWeight = STELLAR_MASS_WEIGHT,
    stellarAgeWeight = STELLAR_AGE_WEIGHT,
    stellarActivityWeight = STELLAR_ACTIVITY_WEIGHT,
    stellarRotationalPeriodWeight = STELLAR_ROTATIONAL_PERIOD_WEIGHT,
    stellarGravityWeight = STELLAR_GRAVITY_WEIGHT,
    stellarMetallicityWeight = STELLAR_METALLICITY_WEIGHT,
    stellarEffectiveTemperatureWeight = STELLAR_EFFECTIVE_TEMPERATURE_WEIGHT,
    planetProtectionWeight = PLANET_PROTECTION_WEIGHT,
    planetTidalLockingWeight = PLANET_TIDAL_LOCKING_WEIGHT,
    planetMassLowerLimit = PLANET_MASS_LOWER_LIMIT,
    planetMassIdealUpperLimit = PLANET_MASS_IDEAL_UPPER_LIMIT,
    planetMassMaxUpperLimit = PLANET_MASS_MAX_UPPER_LIMIT,
    planetRadiusLowerLimit = PLANET_RADIUS_LOWER_LIMIT,
    planetRadiusIdealUpperLimit = PLANET_RADIUS_IDEAL_UPPER_LIMIT,
    planetRadiusMaxUpperLimit = PLANET_RADIUS_MAX_UPPER_LIMIT,
    stellarHostEffectiveTemperatureMaxDeviation = STELLAR_HOST_EFFECTIVE_TEMPERATURE_MAX_DEVIATION
)

internal val achievements = listOf(
    Achievement(
        id = "engsoneca",
        name = "Earth",
        description = "Settle on Earth",
        preconditions = Precondition(
            settledPlanetId = "earth"
        ),
        status = false
    )
)

internal val credits = listOf(
    Credits(
        id = "engsoneca",
        link = "https://www.patreon.com/c/engsoneca",
        type = CreditsType.CREATOR,
    ),
    Credits(
        id = "You",
        link = null,
        type = CreditsType.SUPPORTER,
    ),
    Credits(
        id = "NASA Exoplanet Archive DOIs 10.26133/NEA13 and 10.26133/NEA40",
        link = "https://exoplanetarchive.ipac.caltech.edu/",
        type = CreditsType.SOURCE,
    ),
    Credits(
        id = "OpenGameArt",
        link = "https://opengameart.org/",
        type = CreditsType.MUSIC,
    ),
)