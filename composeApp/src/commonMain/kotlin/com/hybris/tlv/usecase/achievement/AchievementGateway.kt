package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.AchievementSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.ACHIEVEMENTS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.ACHIEVEMENTS_JSON
import com.hybris.tlv.serializer.decode
import com.hybris.tlv.serializer.encode
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Telemetry
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
        val remoteVersion = config.remoteConfigs.achievementsVersion
        val localVersion = config.localConfigs.achievementsVersion
        Telemetry.info(tag = TAG, message = "Syncing achievements: remote version: $remoteVersion, local version: $localVersion")
        if (remoteVersion > localVersion) {
            when (val result = httpClient.getStream<Achievement>(path = ACHIEVEMENTS_URL)) {
                is Result.Error -> Telemetry.error(tag = TAG, message = "Unable to get achievements", throwable = result.error)
                is Result.Success -> {
                    rewriteAchievements(achievements = result.list)
                    config.localConfigs = config.localConfigs.copy(achievementsVersion = remoteVersion)
                    Telemetry.info(tag = TAG, message = "Successful achievements sync")
                    return
                }
            }
        }
        if (achievementDao.isAchievementEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating achievements")
            val achievements: List<Achievement> = loadFromJsonResource(path = ACHIEVEMENTS_JSON)
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
            description = description,
            preconditions = encode(value = preconditions).orEmpty(),
            done = done
        )

    private fun AchievementSchema.toAchievement(): Achievement =
        Achievement(
            id = id,
            description = description,
            preconditions = decode<Precondition>(value = preconditions) ?: Precondition(),
            done = done
        )

    companion object Companion {
        private const val TAG = "Achievement"
    }
}
