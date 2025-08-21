package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.http.Result
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.achievement.local.AchievementLocal
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.remote.AchievementRemote

internal class AchievementInternalGateway(
    private val achievementApi: AchievementRemote,
    private val achievementDao: AchievementLocal
): AchievementInternalUseCases {

    override suspend fun syncAchievements(): SyncResult =
        when (val result = achievementApi.getAchievements()) {
            is Result.Error -> {
                prepopulateAchievements()
                SyncResult.Error(error = result.error)
            }

            is Result.Success -> {
                achievementDao.rewriteAchievements(achievements = result.list)
                SyncResult.Success
            }
        }

    override suspend fun prepopulateAchievements() {
        if (achievementDao.isAchievementEmpty()) {
            val achievements: List<Achievement> = loadFromJson(path = "files/achievements.json")
            achievementDao.rewriteAchievements(achievements = achievements)
            true
        }
    }
}
