package com.hybris.tlv.usecase.gamesession

import com.hybris.tlv.database.FormulaSchema
import com.hybris.tlv.database.GameSessionSchema
import com.hybris.tlv.database.ShipSchema
import com.hybris.tlv.locale.now
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Formula

internal fun GameSessionPrototype.toGameSession(): GameSession {
    val id = generateUuid()
    return GameSession(
        id = id,
        utc = now(),
        currentStellarHostId = null,
        visitedStellarHosts = emptySet(),
        launchedEvents = emptySet(),
        settledPlanetId = null,
        settledPlanetName = null,
        finalHabitability = null,
        score = null,
        ship = Ship(
            id = id,
            engine = engine,
            assignedPoints = ship.assignedPoints,
            yearsTraveled = 0.0,
            sensorRange = ship.sensorRange,
            integrity = 100,
            fuel = ship.fuel,
            materials = ship.materials,
            cryopods = ship.cryopods,
        ),
        formula = formula.copy(id = id)
    )
}

internal fun GameSession.toGameSessionSchema(): GameSessionSchema =
    GameSessionSchema(
        id = id,
        utc = utc,
        currentStellarHostId = currentStellarHostId,
        visitedStellarHosts = visitedStellarHosts,
        launchedEvents = launchedEvents,
        settledPlanetId = settledPlanetId,
        settledPlanetName = settledPlanetName,
        finalHabitability = finalHabitability,
        score = score,
    )

internal fun Ship.toShipSchema(): ShipSchema =
    ShipSchema(
        id = id,
        engineId = engine.id,
        assignedPoints = assignedPoints,
        yearsTraveled = yearsTraveled,
        sensorRange = sensorRange,
        integrity = integrity,
        fuel = fuel,
        materials = materials,
        cryopods = cryopods,
    )

internal fun Formula.toFormulaSchema(): FormulaSchema =
    FormulaSchema(
        id = id,
        rocheWeight = rocheWeight,
        habitableZoneKopparapuWeight = habitableZoneKopparapuWeight,
        habitableZoneKastingWeight = habitableZoneKastingWeight,
        planetRadiusWeight = planetRadiusWeight,
        planetMassWeight = planetMassWeight,
        planetTelluricityWeight = planetTelluricityWeight,
        planetEccentricityWeight = planetEccentricityWeight,
        planetTemperatureWeight = planetTemperatureWeight,
        planetObliquityWeight = planetObliquityWeight,
        planetEsiWeight = planetEsiWeight,
        stellarSpectralTypeWeight = stellarSpectralTypeWeight,
        stellarMassWeight = stellarMassWeight,
        stellarAgeWeight = stellarAgeWeight,
        stellarActivityWeight = stellarActivityWeight,
        stellarRotationalPeriodWeight = stellarRotationalPeriodWeight,
        stellarGravityWeight = stellarGravityWeight,
        stellarMetallicityWeight = stellarMetallicityWeight,
        stellarEffectiveTemperatureWeight = stellarEffectiveTemperatureWeight,
        planetProtectionWeight = planetProtectionWeight,
        planetTidalLockingWeight = planetTidalLockingWeight,
        planetMassLowerLimit = planetMassLowerLimit,
        planetMassIdealUpperLimit = planetMassIdealUpperLimit,
        planetMassMaxUpperLimit = planetMassMaxUpperLimit,
        planetRadiusLowerLimit = planetRadiusLowerLimit,
        planetRadiusIdealUpperLimit = planetRadiusIdealUpperLimit,
        planetRadiusMaxUpperLimit = planetRadiusMaxUpperLimit,
        stellarHostEffectiveTemperatureMaxDeviation = stellarHostEffectiveTemperatureMaxDeviation
    )

internal val gameSessionProjection = { id: String,
                                       utc: String,
                                       currentStellarHostId: String?,
                                       visitedStellarHosts: Set<String>,
                                       launchedEvents: Set<String>,
                                       settledPlanetId: String?,
                                       settledPlanetName: String?,
                                       finalHabitability: Double?,
                                       score: Double?,
                                       assignedPoints: Int,
                                       yearsTraveled: Double,
                                       sensorRange: Int,
                                       integrity: Int,
                                       fuel: Int,
                                       materials: Int,
                                       cryopods: Int,
                                       engineId: String,
                                       engineDescription: String,
                                       engineVelocity: Double,
                                       engineFuelConsumption: Double,
                                       engineCost: Int,
                                       rocheWeight: Double,
                                       habitableZoneKopparapuWeight: Double,
                                       habitableZoneKastingWeight: Double,
                                       planetRadiusWeight: Double,
                                       planetMassWeight: Double,
                                       planetTelluricityWeight: Double,
                                       planetEccentricityWeight: Double,
                                       planetTemperatureWeight: Double,
                                       planetObliquityWeight: Double,
                                       planetEsiWeight: Double,
                                       stellarSpectralTypeWeight: Double,
                                       stellarMassWeight: Double,
                                       stellarAgeWeight: Double,
                                       stellarActivityWeight: Double,
                                       stellarRotationalPeriodWeight: Double,
                                       stellarGravityWeight: Double,
                                       stellarMetallicityWeight: Double,
                                       stellarEffectiveTemperatureWeight: Double,
                                       planetProtectionWeight: Double,
                                       planetTidalLockingWeight: Double,
                                       planetMassLowerLimit: Double,
                                       planetMassIdealUpperLimit: Double,
                                       planetMassMaxUpperLimit: Double,
                                       planetRadiusLowerLimit: Double,
                                       planetRadiusIdealUpperLimit: Double,
                                       planetRadiusMaxUpperLimit: Double,
                                       stellarHostEffectiveTemperatureMaxDeviation: Double ->
    GameSession(
        id = id,
        utc = utc,
        currentStellarHostId = currentStellarHostId,
        visitedStellarHosts = visitedStellarHosts,
        launchedEvents = launchedEvents,
        settledPlanetId = settledPlanetId,
        settledPlanetName = settledPlanetName,
        finalHabitability = finalHabitability,
        score = score,
        ship = Ship(
            id = id,
            engine = Engine(
                id = engineId,
                description = engineDescription,
                velocity = engineVelocity,
                fuelConsumption = engineFuelConsumption,
                cost = engineCost
            ),
            assignedPoints = assignedPoints,
            yearsTraveled = yearsTraveled,
            sensorRange = sensorRange,
            integrity = integrity,
            fuel = fuel,
            materials = materials,
            cryopods = cryopods,
        ),
        formula = Formula(
            id = id,
            rocheWeight = rocheWeight,
            habitableZoneKopparapuWeight = habitableZoneKopparapuWeight,
            habitableZoneKastingWeight = habitableZoneKastingWeight,
            planetRadiusWeight = planetRadiusWeight,
            planetMassWeight = planetMassWeight,
            planetTelluricityWeight = planetTelluricityWeight,
            planetEccentricityWeight = planetEccentricityWeight,
            planetTemperatureWeight = planetTemperatureWeight,
            planetObliquityWeight = planetObliquityWeight,
            planetEsiWeight = planetEsiWeight,
            stellarSpectralTypeWeight = stellarSpectralTypeWeight,
            stellarMassWeight = stellarMassWeight,
            stellarAgeWeight = stellarAgeWeight,
            stellarActivityWeight = stellarActivityWeight,
            stellarRotationalPeriodWeight = stellarRotationalPeriodWeight,
            stellarGravityWeight = stellarGravityWeight,
            stellarMetallicityWeight = stellarMetallicityWeight,
            stellarEffectiveTemperatureWeight = stellarEffectiveTemperatureWeight,
            planetProtectionWeight = planetProtectionWeight,
            planetTidalLockingWeight = planetTidalLockingWeight,
            planetMassLowerLimit = planetMassLowerLimit,
            planetMassIdealUpperLimit = planetMassIdealUpperLimit,
            planetMassMaxUpperLimit = planetMassMaxUpperLimit,
            planetRadiusLowerLimit = planetRadiusLowerLimit,
            planetRadiusIdealUpperLimit = planetRadiusIdealUpperLimit,
            planetRadiusMaxUpperLimit = planetRadiusMaxUpperLimit,
            stellarHostEffectiveTemperatureMaxDeviation = stellarHostEffectiveTemperatureMaxDeviation
        )
    )
}
