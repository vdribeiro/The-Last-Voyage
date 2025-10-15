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
import com.hybris.tlv.usecase.gamesession.model.GameSession
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

    // TODO
    override suspend fun updateAchievements(gameSession: GameSession): List<Achievement> {
        val achievements = mutableSetOf<Achievement>().apply {
            achievementDao.getAchievementsByDone(done = false).executeAsList().map { it.toAchievement() }.forEach { achievement ->
                val preconditions = achievement.preconditions
                val ship = gameSession.ship

                if (gameSession.currentStellarHostId == preconditions.settledHostId) add(element = achievement)
                if (gameSession.settledPlanetId == preconditions.settledPlanetId) add(element = achievement)

                val habitabilitySignal = preconditions.habitability?.firstOrNull()
                val habitability = preconditions.habitability?.drop(n = 1)?.toDoubleOrNull()
                if (gameSession.finalHabitability != null && habitabilitySignal != null && habitability != null) {
                    when (habitabilitySignal) {
                        '+' -> if (gameSession.finalHabitability >= habitability) add(element = achievement)
                        '-' -> if (gameSession.finalHabitability <= habitability) add(element = achievement)
                    }
                }

                val yearsTraveledSignal = preconditions.yearsTraveled?.firstOrNull()
                val yearsTraveled = preconditions.yearsTraveled?.drop(n = 1)?.toDoubleOrNull()
                if (yearsTraveledSignal != null && yearsTraveled != null) {
                    when (yearsTraveledSignal) {
                        '+' -> if (ship.yearsTraveled >= yearsTraveled) add(element = achievement)
                        '-' -> if (ship.yearsTraveled <= yearsTraveled) add(element = achievement)
                    }
                }

                val integritySignal = preconditions.integrity?.firstOrNull()
                val integrity = preconditions.integrity?.drop(n = 1)?.toIntOrNull()
                if (integritySignal != null && integrity != null) {
                    when (integritySignal) {
                        '+' -> if (ship.integrity >= integrity) add(element = achievement)
                        '-' -> if (ship.integrity <= integrity) add(element = achievement)
                    }
                }

                val sensorRangeSignal = preconditions.sensorRange?.firstOrNull()
                val sensorRange = preconditions.sensorRange?.drop(n = 1)?.toIntOrNull()
                if (sensorRangeSignal != null && sensorRange != null) {
                    when (sensorRangeSignal) {
                        '+' -> if (ship.sensorRange >= sensorRange) add(element = achievement)
                        '-' -> if (ship.sensorRange <= sensorRange) add(element = achievement)
                    }
                }

                val materialsSignal = preconditions.materials?.firstOrNull()
                val materials = preconditions.materials?.drop(n = 1)?.toIntOrNull()
                if (materialsSignal != null && materials != null) {
                    when (materialsSignal) {
                        '+' -> if (ship.materials >= materials) add(element = achievement)
                        '-' -> if (ship.materials <= materials) add(element = achievement)
                    }
                }

                val fuelSignal = preconditions.fuel?.firstOrNull()
                val fuel = preconditions.fuel?.drop(n = 1)?.toIntOrNull()
                if (fuelSignal != null && fuel != null) {
                    when (fuelSignal) {
                        '+' -> if (ship.fuel >= fuel) add(element = achievement)
                        '-' -> if (ship.fuel <= fuel) add(element = achievement)
                    }
                }

                val cryopodsSignal = preconditions.cryopods?.firstOrNull()
                val cryopods = preconditions.cryopods?.drop(n = 1)?.toIntOrNull()
                if (cryopodsSignal != null && cryopods != null) {
                    when (cryopodsSignal) {
                        '+' -> if (ship.cryopods >= cryopods) add(element = achievement)
                        '-' -> if (ship.cryopods <= cryopods) add(element = achievement)
                    }
                }
            }
        }
        return achievements.toList()
    }

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
