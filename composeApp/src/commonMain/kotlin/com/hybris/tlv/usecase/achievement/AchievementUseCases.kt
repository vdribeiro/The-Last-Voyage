package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal interface AchievementUseCases {

    /**
     * Sync [Achievement]s.
     */
    suspend fun syncAchievements()

    /**
     * Get all [Achievement]s.
     */
    suspend fun getAchievements(): List<Achievement>

    /**
     * Update [Achievement]s based on the given [gameSession] and return the updated ones.
     */
    suspend fun updateAchievements(gameSession: GameSession): List<Achievement>
}
