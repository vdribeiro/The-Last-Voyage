package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.database.AchievementSchema
import com.hybris.tlv.serializer.json
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.model.Precondition
import database.AppDatabase

internal class AchievementGateway(
    database: AppDatabase
): AchievementUseCases {

    private val achievementDao = database.achievementQueries

    override suspend fun getAchievements(): List<Achievement> =
        achievementDao.getAchievements().executeAsList().map { it.toAchievement() }

    private fun AchievementSchema.toAchievement(): Achievement =
        Achievement(
            id = id,
            name = name,
            description = description,
            preconditions = json.decodeFromString<Precondition>(string = preconditions),
            status = status
        )
}
