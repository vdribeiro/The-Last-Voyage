package com.hybris.tlv.usecase.gamesession

import com.hybris.tlv.usecase.gamesession.local.GameSessionLocal
import com.hybris.tlv.usecase.gamesession.mapper.toGameSession
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype

internal class GameSessionGateway(
    private val gameSessionDao: GameSessionLocal,
): GameSessionUseCases {

    override suspend fun startGame(gameSessionPrototype: GameSessionPrototype) =
        gameSessionDao.startGame(gameSession = gameSessionPrototype.toGameSession())

    override suspend fun getGameSessions(): List<GameSession> =
        gameSessionDao.getGameSessions()

    override suspend fun getLatestGameSession(): GameSession? =
        gameSessionDao.getLatestGameSession()

    override suspend fun updateGameSession(gameSession: GameSession) {
        gameSessionDao.updateGameSession(gameSession = gameSession)
    }
}
