package com.hybris.tlv.usecase.gamesession

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.database.FormulaSchema
import com.hybris.tlv.database.GameSessionSchema
import com.hybris.tlv.database.ShipSchema
import com.hybris.tlv.locale.now
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import database.AppDatabase
import kotlin.math.ceil

internal class GameSessionGateway(
    database: AppDatabase,
): GameSessionUseCases {

    private val gameSessionDao = database.gameSessionQueries
    private val shipDao = database.shipQueries
    private val formulaDao = database.formulaQueries

    override suspend fun startGame(gameSessionPrototype: GameSessionPrototype): GameSession {
        val gameSession = gameSessionPrototype.toGameSession()
        updateGameSession(gameSession = gameSession)
        return gameSession
    }

    override suspend fun getGameSessions(): List<GameSession> =
        gameSessionDao.getGameSessions(mapper = gameSessionProjection).executeAsList()

    override suspend fun getLatestGameSession(): GameSession? =
        gameSessionDao.getLatestGameSession(mapper = gameSessionProjection).executeAsOneOrNull()

    override suspend fun isGameSessionOngoing(): Boolean {
        val gameSession = getLatestGameSession()
        return gameSession != null &&
                gameSession.settledPlanetId == null &&
                gameSession.finalHabitability == null &&
                gameSession.ship.integrity > 0 &&
                gameSession.ship.fuel > 0
    }

    private fun upsertGameSession(gameSession: GameSession) {
        gameSessionDao.upsertGameSession(GameSession = gameSession.toGameSessionSchema())
    }

    override suspend fun updateGameSession(gameSession: GameSession) {
        upsertGameSession(gameSession = gameSession)
        shipDao.upsertShip(Ship = gameSession.ship.toShipSchema())
        formulaDao.upsertFormula(Formula = gameSession.formula.toFormulaSchema())
    }

    override suspend fun launchEvent(gameSession: GameSession, event: Event): GameSession {
        val integrity = gameSession.ship.integrity + (event.outcome?.integrity ?: 0)
        val materials = gameSession.ship.materials + (event.outcome?.materials ?: 0)
        val fuel = gameSession.ship.fuel + (event.outcome?.fuel ?: 0)
        val cryopods = gameSession.ship.cryopods + (event.outcome?.cryopods ?: 0)
        val updatedGameSession = gameSession.copy(
            ship = gameSession.ship.copy(
                integrity = integrity,
                materials = materials,
                fuel = fuel,
                cryopods = cryopods
            ),
            launchedEvents = gameSession.launchedEvents + event.id
        )
        updateGameSession(gameSession = updatedGameSession)
        return updatedGameSession
    }

    override suspend fun travel(gameSession: GameSession, stellarHost: StellarHost): GameSession {
        val distance = ceil(x = stellarHost.distance ?: 1.0).toInt()
        val speed = gameSession.ship.engine.velocity
        val yearsTraveled = gameSession.ship.yearsTraveled + (distance / speed)
        val fuel = gameSession.ship.fuel - distance
        val integrity = gameSession.ship.integrity - 1

        val updatedGameSession = gameSession.copy(
            ship = gameSession.ship.copy(
                yearsTraveled = yearsTraveled,
                integrity = integrity,
                fuel = fuel,
            ),
            currentStellarHostId = stellarHost.id,
            visitedStellarHosts = gameSession.visitedStellarHosts + stellarHost.id
        )
        updateGameSession(gameSession = updatedGameSession)
        return updatedGameSession
    }

    override suspend fun settle(gameSession: GameSession, planet: Planet): GameSession {
        val updatedGameSession = gameSession.copy(
            settledPlanetId = planet.id,
            finalHabitability = planet.score?.habitabilityScore?.times(other = 100.0)
        )
        updateGameSession(gameSession = updatedGameSession)
        return updatedGameSession
    }

    override suspend fun score(gameSession: GameSession, gameOver: GameOver): GameSession {
        val ship = gameSession.ship

        // Base Score = Cryopod Score + Resource Score + Journey Score
        val cryopodScore = ship.cryopods * 100
        val resourceScore = ship.materials * 2 + ship.fuel * 1
        val journeyScore = ship.yearsTraveled * 5
        val baseScore = cryopodScore + resourceScore + journeyScore

        // Challenge Multiplier
        val challengeMultiplier = (1.0 + (15 - ship.assignedPoints) + 0.05).coerceIn(minimumValue = 0.01, maximumValue = 10.0)

        // Game Over Multiplier
        val gameOverMultiplier = getGameOverMultiplier(gameOver = gameOver)

        // Final Score = Base Score * Challenge Multiplier * Game Over Multiplier
        val score = baseScore * challengeMultiplier * gameOverMultiplier

        val updatedGameSession = gameSession.copy(score = score)
        updateGameSession(gameSession = updatedGameSession)
        return updatedGameSession
    }

    @VisibleForTesting
    internal fun getGameOverMultiplier(gameOver: GameOver) =
        when (gameOver) {
            // Ship is destroyed
            GameOver.INTEGRITY_ZERO,
            GameOver.INTEGRITY_ZERO_YEARS_FEW,
            GameOver.INTEGRITY_ZERO_YEARS_SOME,
            GameOver.INTEGRITY_ZERO_YEARS_LOTS,
            GameOver.INTEGRITY_ZERO_MATERIALS_ZERO,
            GameOver.INTEGRITY_ZERO_MATERIALS_LOW,
            GameOver.INTEGRITY_ZERO_MATERIALS_ENOUGH,
            GameOver.INTEGRITY_ZERO_CRYOPODS_ZERO,
            GameOver.INTEGRITY_ZERO_CRYOPODS_ONE,
            GameOver.INTEGRITY_ZERO_CRYOPODS_LOW,
            GameOver.INTEGRITY_ZERO_CRYOPODS_ENOUGH,
            GameOver.INTEGRITY_ZERO_FUEL_LOW,
            GameOver.INTEGRITY_ZERO_FUEL_SOME,
            GameOver.INTEGRITY_ZERO_FUEL_PLENTY,
            GameOver.INTEGRITY_ZERO_YEARS_LOTS_CRYOPODS_BUSTLING -> 0.25

            // Ship ran out of fuel
            GameOver.FUEL_ZERO,
            GameOver.FUEL_ZERO_YEARS_FEW,
            GameOver.FUEL_ZERO_YEARS_SOME,
            GameOver.FUEL_ZERO_YEARS_LOTS,
            GameOver.FUEL_ZERO_MATERIALS_ZERO,
            GameOver.FUEL_ZERO_MATERIALS_LOW,
            GameOver.FUEL_ZERO_MATERIALS_ENOUGH,
            GameOver.FUEL_ZERO_CRYOPODS_ZERO,
            GameOver.FUEL_ZERO_CRYOPODS_ONE,
            GameOver.FUEL_ZERO_CRYOPODS_NEAR_ZERO,
            GameOver.FUEL_ZERO_CRYOPODS_TOO_LOW,
            GameOver.FUEL_ZERO_CRYOPODS_LOW,
            GameOver.FUEL_ZERO_CRYOPODS_ENOUGH,
            GameOver.FUEL_ZERO_INTEGRITY_LOW,
            GameOver.FUEL_ZERO_INTEGRITY_ENOUGH,
            GameOver.FUEL_ZERO_INTEGRITY_PRISTINE,
            GameOver.FUEL_ZERO_MATERIALS_PLENTY_CRYOPODS_BUSTLING,
            GameOver.FUEL_ZERO_INTEGRITY_ENOUGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING -> 0.25

            // Solar System Planets
            GameOver.MERCURY,
            GameOver.VENUS,
            GameOver.EARTH,
            GameOver.MARS,
            GameOver.JUPITER,
            GameOver.SATURN,
            GameOver.URANUS,
            GameOver.NEPTUNE -> 0.25

            // Habitability: Deadly
            GameOver.HABITABILITY_DEADLY,
            GameOver.HABITABILITY_DEADLY_CRYOPODS_ENOUGH,
            GameOver.HABITABILITY_DEADLY_INTEGRITY_LOW,
            GameOver.HABITABILITY_DEADLY_INTEGRITY_MID_LOW_MATERIALS_ENOUGH -> 0.25

            // Habitability: Very Low
            GameOver.HABITABILITY_VERY_LOW,
            GameOver.HABITABILITY_VERY_LOW_CRYOPODS_ENOUGH_MATERIALS_ENOUGH,
            GameOver.HABITABILITY_VERY_LOW_CRYOPODS_MID_MATERIALS_ENOUGH,
            GameOver.HABITABILITY_VERY_LOW_INTEGRITY_LOW -> 0.5

            // Habitability: Low
            GameOver.HABITABILITY_LOW,
            GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_LOW,
            GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ZERO,
            GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ENOUGH,
            GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_LOW,
            GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ZERO -> 0.75

            GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH,
            GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_INTEGRITY_PRISTINE,
            GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_FUEL_PLENTY -> 1.0

            // Habitability: Medium
            GameOver.HABITABILITY_MEDIUM,
            GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_LOW,
            GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ZERO,
            GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_LOW,
            GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ZERO -> 1.25

            GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH -> 1.5
            GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH -> 1.75

            GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH,
            GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS,
            GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_BUSTLING -> 2.0

            // Habitability: High
            GameOver.HABITABILITY_HIGH,
            GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_LOW,
            GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ZERO,
            GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_LOW,
            GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ZERO -> 2.25

            GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH -> 2.5
            GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH -> 2.75

            GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH,
            GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS,
            GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING -> 3.0

            // Default
            GameOver.GAME_OVER -> 0.25
        }

    override suspend fun getGameOver(gameSession: GameSession): GameOver {
        val ship = gameSession.ship
        return when {
            ship.integrity <= 0 -> buildList {
                add(element = GameOver.INTEGRITY_ZERO)

                if (ship.yearsTraveled < 1000.0) add(element = GameOver.INTEGRITY_ZERO_YEARS_FEW)
                if (ship.yearsTraveled in 1000.0..5000.0) add(element = GameOver.INTEGRITY_ZERO_YEARS_SOME)
                if (ship.yearsTraveled > 5000.0) add(element = GameOver.INTEGRITY_ZERO_YEARS_LOTS)

                if (ship.materials < 1) add(element = GameOver.INTEGRITY_ZERO_MATERIALS_ZERO)
                if (ship.materials in 1..20) add(element = GameOver.INTEGRITY_ZERO_MATERIALS_LOW)
                if (ship.materials > 20) add(element = GameOver.INTEGRITY_ZERO_MATERIALS_ENOUGH)

                if (ship.cryopods < 1) add(element = GameOver.INTEGRITY_ZERO_CRYOPODS_ZERO)
                if (ship.cryopods == 1) add(element = GameOver.INTEGRITY_ZERO_CRYOPODS_ONE)
                if (ship.cryopods in 2..20) add(element = GameOver.INTEGRITY_ZERO_CRYOPODS_LOW)
                if (ship.cryopods > 20) add(element = GameOver.INTEGRITY_ZERO_CRYOPODS_ENOUGH)

                if (ship.fuel < 10) add(element = GameOver.INTEGRITY_ZERO_FUEL_LOW)
                if (ship.fuel in 10..90) add(element = GameOver.INTEGRITY_ZERO_FUEL_SOME)
                if (ship.fuel > 90) add(element = GameOver.INTEGRITY_ZERO_FUEL_PLENTY)

                if (ship.yearsTraveled >= 2000.0 && ship.cryopods >= 300) add(element = GameOver.INTEGRITY_ZERO_YEARS_LOTS_CRYOPODS_BUSTLING)
            }.random()

            ship.fuel <= 0 -> buildList {
                add(element = GameOver.FUEL_ZERO)

                if (ship.yearsTraveled < 1000.0) add(element = GameOver.FUEL_ZERO_YEARS_FEW)
                if (ship.yearsTraveled in 1000.0..5000.0) add(element = GameOver.FUEL_ZERO_YEARS_SOME)
                if (ship.yearsTraveled > 5000.0) add(element = GameOver.FUEL_ZERO_YEARS_LOTS)

                if (ship.materials < 1) add(element = GameOver.FUEL_ZERO_MATERIALS_ZERO)
                if (ship.materials in 1..20) add(element = GameOver.FUEL_ZERO_MATERIALS_LOW)
                if (ship.materials >= 20) add(element = GameOver.FUEL_ZERO_MATERIALS_ENOUGH)

                if (ship.cryopods < 1) add(element = GameOver.FUEL_ZERO_CRYOPODS_ZERO)
                if (ship.cryopods == 1) add(element = GameOver.FUEL_ZERO_CRYOPODS_ONE)
                if (ship.cryopods in 2..10) add(element = GameOver.FUEL_ZERO_CRYOPODS_NEAR_ZERO)
                if (ship.cryopods in 11..20) add(element = GameOver.FUEL_ZERO_CRYOPODS_TOO_LOW)
                if (ship.cryopods in 21..50) add(element = GameOver.FUEL_ZERO_CRYOPODS_LOW)
                if (ship.cryopods > 50) add(element = GameOver.FUEL_ZERO_CRYOPODS_ENOUGH)

                if (ship.integrity < 20) add(element = GameOver.FUEL_ZERO_INTEGRITY_LOW)
                if (ship.integrity in 20..90) add(element = GameOver.FUEL_ZERO_INTEGRITY_ENOUGH)
                if (ship.integrity > 90) add(element = GameOver.FUEL_ZERO_INTEGRITY_PRISTINE)

                if (ship.materials >= 100 && ship.cryopods >= 300) add(element = GameOver.FUEL_ZERO_MATERIALS_PLENTY_CRYOPODS_BUSTLING)
                if (ship.integrity >= 90 && ship.materials >= 100 && ship.cryopods >= 300) add(element = GameOver.FUEL_ZERO_INTEGRITY_ENOUGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING)
            }.random()

            gameSession.settledPlanetId == "1mercury" -> GameOver.MERCURY
            gameSession.settledPlanetId == "2venus" -> GameOver.VENUS
            gameSession.settledPlanetId == "3earth" -> GameOver.EARTH
            gameSession.settledPlanetId == "4mars" -> GameOver.MARS
            gameSession.settledPlanetId == "5jupiter" -> GameOver.JUPITER
            gameSession.settledPlanetId == "6saturn" -> GameOver.SATURN
            gameSession.settledPlanetId == "7uranus" -> GameOver.URANUS
            gameSession.settledPlanetId == "8neptune" -> GameOver.NEPTUNE

            gameSession.finalHabitability != null -> buildList {
                when (gameSession.finalHabitability) {
                    in 0.0..20.0 -> {
                        add(element = GameOver.HABITABILITY_DEADLY)
                        if (ship.cryopods >= 50) add(element = GameOver.HABITABILITY_DEADLY_CRYOPODS_ENOUGH)
                        if (ship.integrity < 20) add(element = GameOver.HABITABILITY_DEADLY_INTEGRITY_LOW)
                        if (ship.materials >= 50 && ship.integrity < 30) add(element = GameOver.HABITABILITY_DEADLY_INTEGRITY_MID_LOW_MATERIALS_ENOUGH)
                    }

                    in 21.0..40.0 -> {
                        add(element = GameOver.HABITABILITY_VERY_LOW)
                        if (ship.cryopods >= 50 && ship.materials >= 50) add(element = GameOver.HABITABILITY_VERY_LOW_CRYOPODS_ENOUGH_MATERIALS_ENOUGH)
                        if (ship.cryopods >= 100 && ship.materials >= 50) add(element = GameOver.HABITABILITY_VERY_LOW_CRYOPODS_MID_MATERIALS_ENOUGH)
                        if (ship.integrity < 20) add(element = GameOver.HABITABILITY_VERY_LOW_INTEGRITY_LOW)
                    }

                    in 41.0..60.0 -> when {
                        ship.materials >= 300 && ship.cryopods >= 150 -> {
                            add(element = GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH)
                            if (ship.integrity >= 90) add(element = GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_INTEGRITY_PRISTINE)
                            if (ship.fuel >= 50) add(element = GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_FUEL_PLENTY)
                        }

                        ship.materials >= 300 && ship.cryopods in 1..149 -> add(element = GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_LOW)
                        ship.materials >= 300 && ship.cryopods < 1 -> add(element = GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ZERO)
                        ship.materials < 300 && ship.cryopods >= 150 -> add(element = GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ENOUGH)
                        ship.materials < 300 && ship.cryopods in 1..149 -> add(element = GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_LOW)
                        ship.materials < 300 && ship.cryopods < 1 -> add(element = GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ZERO)
                        else -> add(element = GameOver.HABITABILITY_LOW)
                    }

                    in 61.0..80.0 -> when {
                        ship.materials >= 100 && ship.cryopods >= 100 -> {
                            add(element = GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH)
                            if (ship.yearsTraveled > 5000.0) add(element = GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS)
                            if (ship.cryopods >= 300) add(element = GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_BUSTLING)
                        }

                        ship.materials < 100 && ship.cryopods >= 100 -> when {
                            ship.integrity >= 75 -> {
                                add(element = GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH)
                            }

                            else -> {
                                add(element = GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH)
                            }
                        }

                        ship.materials >= 100 && ship.cryopods in 1..99 -> add(element = GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_LOW)
                        ship.materials >= 100 && ship.cryopods < 1 -> add(element = GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ZERO)
                        ship.materials < 100 && ship.cryopods in 1..99 -> add(element = GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_LOW)
                        ship.materials < 100 && ship.cryopods < 1 -> add(element = GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ZERO)
                        else -> add(element = GameOver.HABITABILITY_MEDIUM)
                    }

                    else -> when {
                        ship.materials >= 50 && ship.cryopods >= 50 -> {
                            add(element = GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH)
                            if (ship.yearsTraveled > 5000.0) add(element = GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS)
                            if (ship.cryopods >= 300) add(element = GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING)
                        }

                        ship.materials < 50 && ship.cryopods >= 50 -> when {
                            ship.integrity >= 50 -> {
                                add(element = GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH)
                            }

                            else -> {
                                add(element = GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH)
                            }
                        }

                        ship.materials >= 50 && ship.cryopods in 1..49 -> add(element = GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_LOW)
                        ship.materials >= 50 && ship.cryopods < 1 -> add(element = GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ZERO)
                        ship.materials < 50 && ship.cryopods in 1..49 -> add(element = GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_LOW)
                        ship.materials < 50 && ship.cryopods < 1 -> add(element = GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ZERO)
                        else -> add(element = GameOver.HABITABILITY_HIGH)
                    }
                }
            }.random()

            else -> GameOver.GAME_OVER
        }
    }

    override suspend fun isGameOver(gameSession: GameSession): Boolean =
        gameSession.ship.integrity <= 0 || gameSession.ship.fuel <= 0 || gameSession.settledPlanetId != null

    private fun GameSessionPrototype.toGameSession(): GameSession {
        val id = generateUuid()
        return GameSession(
            id = id,
            utc = now(),
            currentStellarHostId = null,
            visitedStellarHosts = emptySet(),
            launchedEvents = emptySet(),
            settledPlanetId = null,
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

    private fun GameSession.toGameSessionSchema(): GameSessionSchema =
        GameSessionSchema(
            id = id,
            utc = utc,
            currentStellarHostId = currentStellarHostId,
            visitedStellarHosts = visitedStellarHosts,
            launchedEvents = launchedEvents,
            settledPlanetId = settledPlanetId,
            finalHabitability = finalHabitability,
            score = score,
        )

    private fun Ship.toShipSchema(): ShipSchema =
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

    private fun Formula.toFormulaSchema(): FormulaSchema =
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

    private val gameSessionProjection = { id: String,
                                          utc: String,
                                          currentStellarHostId: String?,
                                          visitedStellarHosts: Set<String>,
                                          launchedEvents: Set<String>,
                                          settledPlanetId: String?,
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
            finalHabitability = finalHabitability,
            score = score,
            ship = Ship(
                id = id,
                engine = Engine(
                    id = engineId,
                    description = engineDescription,
                    velocity = engineVelocity,
                    fuelConsumption = engineFuelConsumption
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
}
