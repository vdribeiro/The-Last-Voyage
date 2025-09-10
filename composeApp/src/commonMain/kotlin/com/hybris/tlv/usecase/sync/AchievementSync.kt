package com.hybris.tlv.usecase.sync

import com.hybris.tlv.database.AchievementSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.ACHIEVEMENTS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.json
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.sync.model.SyncResult
import database.AppDatabase
import io.ktor.client.HttpClient

internal class AchievementSync(
    private val httpClient: HttpClient,
    database: AppDatabase
) {

    private val achievementDao = database.achievementQueries

    suspend fun syncAchievements(): SyncResult =
        when (val result = httpClient.getStream<Achievement>(path = ACHIEVEMENTS_URL)) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> rewriteAchievements(achievements = result.list).let { SyncResult.Success }
        }

    suspend fun prepopulateAchievements() {
        if (achievementDao.isAchievementEmpty().executeAsList().isEmpty()) {
            val achievements: List<Achievement> = loadFromJson(path = "files/achievements.json")
            rewriteAchievements(achievements = achievements)
        }
    }

    private fun rewriteAchievements(achievements: List<Achievement>) = achievementDao.transaction {
        achievementDao.truncateAchievement()
        achievements.forEach { achievementDao.upsertAchievement(Achievement = it.toAchievementSchema()) }
    }

    private fun Achievement.toAchievementSchema(): AchievementSchema =
        AchievementSchema(
            id = id,
            name = name,
            description = description,
            preconditions = json.encodeToString(value = preconditions),
            status = status
        )
}