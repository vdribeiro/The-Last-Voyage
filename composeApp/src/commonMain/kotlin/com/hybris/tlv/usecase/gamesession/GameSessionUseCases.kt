package com.hybris.tlv.usecase.gamesession

import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype

internal interface GameSessionUseCases {

    /**
     * Set a new game session given its [gameSessionPrototype].
     */
    suspend fun startGame(gameSessionPrototype: GameSessionPrototype)

    /**
     * Get all game sessions from the database.
     */
    suspend fun getGameSessions(): List<GameSession>

    /**
     * Get latest game session from the database.
     */
    suspend fun getLatestGameSession(): GameSession?

    /**
     * Update the given [gameSession].
     */
    suspend fun updateGameSession(gameSession: GameSession)
}
