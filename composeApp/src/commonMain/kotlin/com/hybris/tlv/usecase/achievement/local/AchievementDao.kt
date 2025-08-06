package com.hybris.tlv.usecase.achievement.local

import com.hybris.tlv.usecase.achievement.mapper.toAchievement
import com.hybris.tlv.usecase.achievement.mapper.toAchievementSchema
import com.hybris.tlv.usecase.achievement.model.Achievement
import database.AppDatabase

internal class AchievementDao(
    database: AppDatabase
): AchievementLocal {

    private val achievementDao = database.achievementQueries

    override fun isAchievementEmpty(): Boolean =
        achievementDao.isAchievementEmpty().executeAsList().isEmpty()

    override fun rewriteAchievements(achievements: List<Achievement>) = achievementDao.transaction {
        achievementDao.truncateAchievement()
        achievements.forEach { achievementDao.upsertAchievement(Achievement = it.toAchievementSchema()) }
    }

    override fun getAchievements(): List<Achievement> =
        achievementDao.getAchievements().executeAsList().map { it.toAchievement() }
}
