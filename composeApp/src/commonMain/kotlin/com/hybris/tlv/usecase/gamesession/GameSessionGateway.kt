package com.hybris.tlv.usecase.gamesession

import com.hybris.tlv.usecase.gamesession.local.GameSessionLocal
import com.hybris.tlv.usecase.gamesession.mapper.toGameSession
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases

internal class GameSessionGateway(
    private val gameSessionDao: GameSessionLocal,
    private val shipUseCases: ShipUseCases,
    private val spaceUseCases: SpaceUseCases
): GameSessionUseCases {

    override suspend fun startGame(gameSessionPrototype: GameSessionPrototype) {
        val gameSession = gameSessionPrototype.toGameSession()
        gameSessionDao.startGame(gameSession = gameSession)
        shipUseCases.upsertShip(ship = gameSession.ship)
        spaceUseCases.upsertFormula(formula = gameSession.formula)
    }

    override suspend fun getGameSessions(): List<GameSession> =
        gameSessionDao.getGameSessions()

    override suspend fun getLatestGameSession(): GameSession? =
        gameSessionDao.getLatestGameSession()

    override suspend fun updateGameSession(gameSession: GameSession) {
        gameSessionDao.updateGameSession(gameSession = gameSession)
    }

    override suspend fun isGameOver(gameSession: GameSession): Boolean =
        (gameSession.ship.integrity <= 0 || gameSession.ship.fuel <= 0)
}
