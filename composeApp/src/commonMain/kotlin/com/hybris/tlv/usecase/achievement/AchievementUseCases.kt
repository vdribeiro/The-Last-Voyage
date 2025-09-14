package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.http.Result
import com.hybris.tlv.usecase.achievement.model.Achievement

internal interface AchievementUseCases {

    /**
     * Sync [Achievement]s.
     */
    override suspend fun syncAchievements(): Result<Achievement>

    /**
     * Prepopulate [Achievement]s.
     */
    override suspend fun prepopulateAchievements()

    /**
     * Get all [Achievement]s.
     */
    suspend fun getAchievements(): List<Achievement>
}
