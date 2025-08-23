package com.hybris.tlv.usecase.gamesession.local

import com.hybris.tlv.usecase.gamesession.mapper.toGameSessionSchema
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.gameSessionProjection
import database.AppDatabase

internal class GameSessionDao(
    database: AppDatabase
): GameSessionLocal {

    private val gameSessionDao = database.gameSessionQueries

    override fun startGame(gameSession: GameSession) {
        gameSessionDao.upsertGameSession(GameSession = gameSession.toGameSessionSchema())
    }

    override fun getGameSessions(): List<GameSession> =
        gameSessionDao.getGameSessions(mapper = gameSessionProjection).executeAsList()

    override fun getLatestGameSession(): GameSession? =
        gameSessionDao.getLatestGameSession(mapper = gameSessionProjection).executeAsOneOrNull()

    override fun updateGameSession(gameSession: GameSession) {
        gameSessionDao.upsertGameSession(GameSession = gameSession.toGameSessionSchema())
    }
}
