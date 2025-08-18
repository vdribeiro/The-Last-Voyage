package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.usecase.achievement.model.Achievement

internal interface AchievementUseCases {

    /**
     * Get [Achievement] from the database.
     */
    suspend fun getAchievements(): List<Achievement>
}
