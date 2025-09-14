package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.AchievementSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.ACHIEVEMENTS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.json
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.model.Precondition
import database.AppDatabase
import io.ktor.client.HttpClient

internal class AchievementGateway(
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    database: AppDatabase
): AchievementUseCases {

    private val achievementDao = database.achievementQueries

    override suspend fun syncAchievements() {
        if (config.remoteConfigs.achievementsVersion > config.localConfigs.achievementsVersion) {
            when (val result = httpClient.getStream<Achievement>(path = ACHIEVEMENTS_URL)) {
                is Result.Error -> Logger.error(tag = TAG, message = result.error)
                is Result.Success -> rewriteAchievements(achievements = result.list)
            }
        }
    }

    override suspend fun prepopulateAchievements() {
        if (achievementDao.isAchievementEmpty().executeAsList().isEmpty()) {
            val achievements: List<Achievement> = loadFromJson(path = "files/achievements.json")
            rewriteAchievements(achievements = achievements)
        }
    }

    private fun rewriteAchievements(achievements: List<Achievement>) = achievementDao.transaction {
        achievementDao.truncateAchievement()
        achievements.forEach { achievementDao.upsertAchievement(Achievement = it.toAchievementSchema()) }
    }

    override suspend fun getAchievements(): List<Achievement> =
        achievementDao.getAchievements().executeAsList().map { it.toAchievement() }

    private fun Achievement.toAchievementSchema(): AchievementSchema =
        AchievementSchema(
            id = id,
            name = name,
            description = description,
            preconditions = runCatching {
                json.encodeToString(value = preconditions)
            }.getOrDefault(defaultValue = ""),
            status = status
        )

    private fun AchievementSchema.toAchievement(): Achievement =
        Achievement(
            id = id,
            name = name,
            description = description,
            preconditions = runCatching {
                json.decodeFromString<Precondition>(string = preconditions)
            }.getOrDefault(defaultValue = Precondition()),
            status = status
        )

    companion object Companion {
        private const val TAG = "Achievement"
    }
}
