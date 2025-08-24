package com.hybris.tlv.usecase.gamesession.local

import com.hybris.tlv.usecase.gamesession.model.GameSession

internal interface GameSessionLocal {

    /**
     * Get all [GameSession]s ordered .
     */
    fun getGameSessions(): List<GameSession>

    /**
     * Get latest [GameSession].
     */
    fun getLatestGameSession(): GameSession?

    /**
     * Upserts a [gameSession].
     */
    fun upsertGameSession(gameSession: GameSession)
}
