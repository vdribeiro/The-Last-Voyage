package com.hybris.tlv.usecase.gamesession

import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.local.GameSessionLocal
import com.hybris.tlv.usecase.gamesession.mapper.toGameSession
import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.ShipInternalUseCases
import com.hybris.tlv.usecase.space.SpaceInternalUseCases
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import kotlin.math.ceil

internal class GameSessionGateway(
    private val gameSessionDao: GameSessionLocal,
    private val shipInternalUseCases: ShipInternalUseCases,
    private val spaceInternalUseCases: SpaceInternalUseCases
): GameSessionUseCases {

    override suspend fun startGame(gameSessionPrototype: GameSessionPrototype): GameSession {
        val gameSession = gameSessionPrototype.toGameSession()
        updateGameSession(gameSession = gameSession)
        return gameSession
    }

    override suspend fun getGameSessions(): List<GameSession> =
        gameSessionDao.getGameSessions()

    override suspend fun getLatestGameSession(): GameSession? =
        gameSessionDao.getLatestGameSession()

    override suspend fun isGameSessionOngoing(): Boolean {
        val gameSession = getLatestGameSession()
        return gameSession != null &&
                gameSession.settledPlanetId == null &&
                gameSession.finalHabitability == null &&
                gameSession.ship.integrity > 0 &&
                gameSession.ship.fuel > 0
    }

    override suspend fun updateGameSession(gameSession: GameSession) {
        gameSessionDao.upsertGameSession(gameSession = gameSession)
        shipInternalUseCases.upsertShip(ship = gameSession.ship)
        spaceInternalUseCases.upsertFormula(formula = gameSession.formula)
    }

    override suspend fun doEvent(gameSession: GameSession, event: Event): GameSession {
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
        val speed = 0.1  // TODO - use engine speed - using 0.1c for now
        val yearsTraveled = gameSession.ship.yearsTraveled + (distance / speed)
        val fuel = gameSession.ship.fuel - distance

        val updatedGameSession = gameSession.copy(
            ship = gameSession.ship.copy(
                yearsTraveled = yearsTraveled,
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
            finalHabitability = planet.habitability?.habitabilityScore?.times(other = 100.0)
        )
        updateGameSession(gameSession = updatedGameSession)
        return updatedGameSession
    }

    override suspend fun score(gameSession: GameSession, gameOver: GameOver): GameSession {
        val ship = gameSession.ship

        // Base Score = (Cryopod Score) + (Resource Score) + (Journey Score)
        val cryopodScore = ship.cryopods * 100
        val resourceScore = ship.materials * 2 + ship.fuel * 1
        val journeyScore = ship.yearsTraveled * 5
        val baseScore = cryopodScore + resourceScore + journeyScore

        // Challenge Multiplier
        val challengeMultiplier = (1.0 + (15 - ship.assignedPoints) + 0.05).coerceIn(minimumValue = 0.01, maximumValue = 10.0)

        // Final Score = (Base Score) * Success Multiplier * Challenge Multiplier
        val score = baseScore * gameOver.multiplier * challengeMultiplier

        val updatedGameSession = gameSession.copy(score = score)
        updateGameSession(gameSession = updatedGameSession)
        return updatedGameSession
    }

    override suspend fun isGameOver(gameSession: GameSession): Boolean =
        gameSession.ship.integrity <= 0 || gameSession.ship.fuel <= 0 || gameSession.settledPlanetId != null

    override suspend fun getGameOver(gameSession: GameSession): GameOver {
        val ship = gameSession.ship
        var habitabilityMultiplier = 0.25
        var successMultiplier = 0.25
        val message = when {
            gameSession.settledPlanetId == "1mercury" -> "game_over_screen__mercury"
            gameSession.settledPlanetId == "2venus" -> "game_over_screen__venus"
            gameSession.settledPlanetId == "3earth" -> "game_over_screen__earth"
            gameSession.settledPlanetId == "4mars" -> "game_over_screen__mars"
            gameSession.settledPlanetId == "5jupiter" -> "game_over_screen__jupiter"
            gameSession.settledPlanetId == "6saturn" -> "game_over_screen__saturn"
            gameSession.settledPlanetId == "7uranus" -> "game_over_screen__uranus"
            gameSession.settledPlanetId == "8neptune" -> "game_over_screen__neptune"

            gameSession.finalHabitability != null -> buildList {
                when (gameSession.finalHabitability) {
                    in 0.0..20.0 -> {
                        habitabilityMultiplier = 0.25
                        add("game_over_screen__habitability_deadly")
                        if (ship.cryopods >= 50) add("game_over_screen__habitability_deadly_cryopods_enough")
                        if (ship.integrity < 20) add("game_over_screen__habitability_deadly_integrity_low")
                        if (ship.materials >= 50 && ship.integrity < 30) add("game_over_screen__habitability_deadly_integrity_mid_low_materials_enough")
                    }

                    in 21.0..40.0 -> {
                        habitabilityMultiplier = 0.50
                        add("game_over_screen__habitability_very_low")
                        if (ship.cryopods >= 50 && ship.materials >= 50) add("game_over_screen__habitability_very_low_cryopods_enough_materials_enough")
                        if (ship.cryopods >= 100 && ship.materials >= 50) add("game_over_screen__habitability_very_low_cryopods_mid_materials_enough")
                        if (ship.integrity < 20) add("game_over_screen__habitability_very_low_integrity_low")
                    }

                    in 41.0..60.0 -> {
                        habitabilityMultiplier = 1.0
                        when {
                            ship.materials >= 300 && ship.cryopods >= 150 -> {
                                successMultiplier = 1.0
                                add("game_over_screen__habitability_low_materials_enough_cryopods_enough")
                                if (ship.integrity >= 90) add("game_over_screen__habitability_low_materials_enough_cryopods_enough_integrity_pristine")
                                if (ship.fuel >= 50) add("game_over_screen__habitability_low_materials_enough_cryopods_enough_fuel_plenty")
                            }

                            ship.materials >= 300 && ship.cryopods in 1..149 -> add("game_over_screen__habitability_low_materials_enough_cryopods_low")
                            ship.materials >= 300 && ship.cryopods < 1 -> add("game_over_screen__habitability_low_materials_enough_cryopods_zero")
                            ship.materials < 300 && ship.cryopods >= 150 -> add("game_over_screen__habitability_low_materials_low_cryopods_enough")
                            ship.materials < 300 && ship.cryopods in 1..149 -> add("game_over_screen__habitability_low_materials_low_cryopods_low")
                            ship.materials < 300 && ship.cryopods < 1 -> add("game_over_screen__habitability_low_materials_low_cryopods_zero")
                            else -> add("game_over_screen__habitability_low")
                        }
                    }

                    in 61.0..80.0 -> {
                        habitabilityMultiplier = 1.2
                        when {
                            ship.materials >= 100 && ship.cryopods >= 100 -> {
                                successMultiplier = 1.0
                                add("game_over_screen__habitability_medium_materials_enough_cryopods_enough")
                                if (ship.yearsTraveled > 5000.0) add("game_over_screen__habitability_medium_materials_enough_cryopods_enough_years_lots")
                                if (ship.cryopods >= 300) add("game_over_screen__habitability_medium_materials_enough_cryopods_bustling")
                            }

                            ship.materials >= 100 && ship.cryopods in 1..99 -> add("game_over_screen__habitability_medium_materials_enough_cryopods_low")
                            ship.materials >= 100 && ship.cryopods < 1 -> add("game_over_screen__habitability_medium_materials_enough_cryopods_zero")
                            ship.materials < 100 && ship.cryopods >= 100 -> when {
                                ship.integrity >= 75 -> {
                                    successMultiplier = 0.75
                                    add("game_over_screen__habitability_medium_materials_low_cryopods_enough_integrity_enough")
                                }

                                else -> {
                                    successMultiplier = 0.5
                                    add("game_over_screen__habitability_medium_materials_low_cryopods_enough")
                                }
                            }

                            ship.materials < 100 && ship.cryopods in 1..99 -> add("game_over_screen__habitability_medium_materials_low_cryopods_low")
                            ship.materials < 100 && ship.cryopods < 1 -> add("game_over_screen__habitability_medium_materials_low_cryopods_zero")
                            else -> add("game_over_screen__habitability_medium")
                        }
                    }

                    else -> {
                        habitabilityMultiplier = 1.5
                        when {
                            ship.materials >= 50 && ship.cryopods >= 50 -> {
                                successMultiplier = 1.0
                                add("game_over_screen__habitability_high_materials_enough_cryopods_enough")
                                if (ship.yearsTraveled > 5000.0) add("game_over_screen__habitability_high_materials_enough_cryopods_enough_years_lots")
                                if (ship.cryopods >= 300) add("game_over_screen__habitability_high_materials_enough_cryopods_bustling")
                            }

                            ship.materials >= 50 && ship.cryopods in 1..49 -> add("game_over_screen__habitability_high_materials_enough_cryopods_low")
                            ship.materials >= 50 && ship.cryopods < 1 -> add("game_over_screen__habitability_high_materials_enough_cryopods_zero")
                            ship.materials < 50 && ship.cryopods >= 50 -> when {
                                ship.integrity >= 50 -> {
                                    successMultiplier = 0.75
                                    add("game_over_screen__habitability_high_materials_low_cryopods_enough_integrity_enough")
                                }

                                else -> {
                                    successMultiplier = 0.5
                                    add("game_over_screen__habitability_high_materials_low_cryopods_enough")
                                }
                            }

                            ship.materials < 50 && ship.cryopods in 1..49 -> add("game_over_screen__habitability_high_materials_low_cryopods_low")
                            ship.materials < 50 && ship.cryopods < 1 -> add("game_over_screen__habitability_high_materials_low_cryopods_zero")
                            else -> add("game_over_screen__habitability_high")
                        }
                    }
                }
            }.random()

            ship.integrity <= 0 -> buildList {
                add("game_over_screen__integrity_zero")

                if (ship.yearsTraveled < 1000.0) add("game_over_screen__integrity_zero_years_few")
                if (ship.yearsTraveled in 1000.0..5000.0) add("game_over_screen__integrity_zero_years_some")
                if (ship.yearsTraveled > 5000.0) add("game_over_screen__integrity_zero_years_lots")

                if (ship.materials < 1) add("game_over_screen__integrity_zero_materials_zero")
                if (ship.materials in 1..20) add("game_over_screen__integrity_zero_materials_low")
                if (ship.materials > 20) add("game_over_screen__integrity_zero_materials_enough")

                if (ship.cryopods < 1) add("game_over_screen__integrity_zero_cryopods_zero")
                if (ship.cryopods == 1) add("game_over_screen__integrity_zero_cryopods_one")
                if (ship.cryopods in 2..20) add("game_over_screen__integrity_zero_cryopods_low")
                if (ship.cryopods > 20) add("game_over_screen__integrity_zero_cryopods_enough")

                if (ship.fuel < 10) add("game_over_screen__integrity_zero_fuel_low")
                if (ship.fuel in 10..90) add("game_over_screen__integrity_zero_fuel_some")
                if (ship.fuel > 90) add("game_over_screen__integrity_zero_fuel_plenty")

                if (ship.yearsTraveled >= 2000.0 && ship.cryopods >= 300) add("game_over_screen__integrity_zero_years_lots_cryopods_bustling")
            }.random()

            ship.fuel <= 0 -> buildList {
                add("game_over_screen__fuel_zero")

                if (ship.yearsTraveled < 1000.0) add("game_over_screen__fuel_zero_years_few")
                if (ship.yearsTraveled in 1000.0..5000.0) add("game_over_screen__fuel_zero_years_some")
                if (ship.yearsTraveled > 5000.0) add("game_over_screen__fuel_zero_years_lots")

                if (ship.materials < 1) add("game_over_screen__fuel_zero_materials_zero")
                if (ship.materials in 1..20) add("game_over_screen__fuel_zero_materials_low")
                if (ship.materials >= 20) add("game_over_screen__fuel_zero_materials_enough")

                if (ship.cryopods < 1) add("game_over_screen__fuel_zero_cryopods_zero")
                if (ship.cryopods == 1) add("game_over_screen__fuel_zero_cryopods_one")
                if (ship.cryopods in 2..10) add("game_over_screen__fuel_zero_cryopods_near_zero")
                if (ship.cryopods in 11..20) add("game_over_screen__fuel_zero_cryopods_too_low")
                if (ship.cryopods in 21..50) add("game_over_screen__fuel_zero_cryopods_low")
                if (ship.cryopods > 50) add("game_over_screen__fuel_zero_cryopods_enough")

                if (ship.integrity < 20) add("game_over_screen__fuel_zero_integrity_low")
                if (ship.integrity in 20..90) add("game_over_screen__fuel_zero_integrity_enough")
                if (ship.integrity > 90) add("game_over_screen__fuel_zero_integrity_pristine")

                if (ship.materials >= 100 && ship.cryopods >= 300) add("game_over_screen__fuel_zero_materials_plenty_cryopods_bustling")
                if (ship.integrity >= 90 && ship.materials >= 100 && ship.cryopods >= 300) add("game_over_screen__fuel_zero_integrity_enough_materials_enough_cryopods_bustling")
            }.random()

            else -> "game_over_screen__game_over"
        }
        return Pair(first = message, second = habitabilityMultiplier * successMultiplier)
    }
}
