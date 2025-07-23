package com.hybris.tlv.usecase.gamesession.local

import com.hybris.tlv.usecase.gamesession.model.GameSession

internal interface GameSessionLocal {

    /**
     * Upsert a [gameSession] and end the last ongoing game session.
     */
    fun startGame(gameSession: GameSession)

    /**
     * Get all [GameSession]s ordered .
     */
    fun getGameSessions(): List<GameSession>

    /**
     * Get latest [GameSession].
     */
    fun getLatestGameSession(): GameSession?

    /**
     * Update a [gameSession].
     */
    fun updateGameSession(gameSession: GameSession)
}
