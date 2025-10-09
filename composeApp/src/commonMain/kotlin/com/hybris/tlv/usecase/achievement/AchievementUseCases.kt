package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.usecase.achievement.model.Achievement

internal interface AchievementUseCases {

    /**
     * Sync [Achievement]s.
     */
    suspend fun syncAchievements()

    /**
     * Get all [Achievement]s.
     */
    suspend fun getAchievements(): List<Achievement>
}
