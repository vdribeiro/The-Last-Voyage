package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.usecase.achievement.local.AchievementLocal
import com.hybris.tlv.usecase.achievement.model.Achievement

internal class AchievementGateway(
    private val achievementDao: AchievementLocal
): AchievementUseCases {

    override suspend fun getAchievements(): List<Achievement> =
        achievementDao.getAchievements()
}
