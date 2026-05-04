package com.hybris.tlv.domain.usecase.gamesession

import com.hybris.tlv.domain.event.Event
import com.hybris.tlv.domain.space.Planet
import com.hybris.tlv.domain.space.StellarHost
import com.hybris.tlv.domain.usecase.gamesession.model.GameOver
import com.hybris.tlv.domain.usecase.gamesession.model.GameSession
import com.hybris.tlv.domain.usecase.gamesession.model.GameSessionPrototype

internal interface GameSessionUseCases {

    /**
     * Set a new game session given its [gameSessionPrototype].
     */
    suspend fun startGame(gameSessionPrototype: GameSessionPrototype): GameSession

    /**
     * Get all game sessions.
     */
    suspend fun getGameSessions(): List<GameSession>

    /**
     * Get latest game session.
     */
    suspend fun getLatestGameSession(): GameSession?

    /**
     * Checks if a game session is ongoing.
     */
    suspend fun isGameSessionOngoing(): Boolean

    /**
     * Update the given [gameSession].
     */
    suspend fun updateGameSession(gameSession: GameSession): GameSession

    /**
     * Update the given [gameSession] with the given [event] outcomes.
     */
    suspend fun launchEvent(gameSession: GameSession, event: Event): GameSession

    /**
     * Update the given [gameSession] by travelling to the given [stellarHost].
     * This will use fuel, add to the years traveled, and add to the visited stellar hosts list.
     */
    suspend fun travel(gameSession: GameSession, stellarHost: StellarHost): GameSession

    /**
     * Update the given [gameSession] by settling in the given [planet].
     */
    suspend fun settle(gameSession: GameSession, planet: Planet): GameSession

    /**
     * Update the given [gameSession] by attributing a score with the given [gameOver].
     */
    suspend fun score(gameSession: GameSession, gameOver: GameOver): GameSession

    /**
     * Get the [GameOver] for the given [gameSession].
     */
    suspend fun getGameOver(gameSession: GameSession): GameOver

    /**
     * Checks if the given [gameSession] is in a game over state.
     */
    suspend fun isGameOver(gameSession: GameSession): Boolean
}
