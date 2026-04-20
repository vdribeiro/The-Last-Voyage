package com.hybris.tlv.domain.usecase.achievement

import com.hybris.tlv.domain.achievement.Achievement
import com.hybris.tlv.domain.usecase.gamesession.model.GameSession

internal interface AchievementUseCases {

    /**
     * Sync [Achievement]s.
     */
    suspend fun syncAchievements(): Boolean

    /**
     * Prepopulate [Achievement]s.
     */
    suspend fun prepopulateAchievements(): Boolean

    /**
     * Get all [Achievement]s.
     */
    suspend fun getAchievements(): List<Achievement>

    /**
     * Update [Achievement]s based on the given [gameSession] and return the updated ones.
     */
    suspend fun updateAchievements(gameSession: GameSession): List<Achievement>
}
