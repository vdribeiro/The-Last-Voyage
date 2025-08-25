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
        TODO()
    }
}
