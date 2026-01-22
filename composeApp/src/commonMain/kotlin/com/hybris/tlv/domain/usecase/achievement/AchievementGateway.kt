package com.hybris.tlv.domain.usecase.achievement

import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.http.Result
import com.hybris.tlv.data.http.URL
import com.hybris.tlv.data.http.get
import com.hybris.tlv.data.serializer.loadFromJsonResource
import com.hybris.tlv.domain.usecase.achievement.model.Achievement
import com.hybris.tlv.domain.usecase.gamesession.model.GameSession
import com.hybris.tlv.infrastructure.resource.JsonResource
import database.AppDatabase

internal class AchievementGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): AchievementUseCases {

    private val achievementDao = database.achievementQueries

    override suspend fun syncAchievements(): Boolean = withContext(context = Dispatcher.IO) {
        when (val result = httpClient.get<Achievement>(path = URL.Achievements)) {
            is Result.Error -> {
                Telemetry.error(tag = TAG, message = "Unable to get achievements", throwable = result.error)
                false
            }

            is Result.Success -> {
                rewriteAchievements(achievements = result.list)
                Telemetry.info(tag = TAG, message = "Successful achievements sync")
                true
            }
        }
    }

    override suspend fun prepopulateAchievements(): Boolean = withContext(context = Dispatcher.IO) {
        if (achievementDao.isAchievementEmpty().executeAsList().isEmpty()) {
            Telemetry.info(tag = TAG, message = "Prepopulating achievements")
            val achievements: List<Achievement> = loadFromJsonResource(json = JsonResource.Achievements)
            rewriteAchievements(achievements = achievements)
            true
        } else false
    }

    private suspend fun rewriteAchievements(achievements: List<Achievement>) = achievementDao.transaction {
        achievementDao.truncateAchievement()
        achievements.forEach { achievementDao.upsertAchievement(Achievement = it.toAchievementSchema()) }
    }

    override suspend fun getAchievements(): List<Achievement> = withContext(context = Dispatcher.IO) {
        achievementDao.getAchievements().executeAsList().map { it.toAchievement() }
    }

    override suspend fun updateAchievements(gameSession: GameSession): List<Achievement> = withContext(context = Dispatcher.IO) {
        gameSession.currentStellarHostId ?: return@withContext emptyList()
        gameSession.settledPlanetId ?: return@withContext emptyList()
        gameSession.finalHabitability ?: return@withContext emptyList()

        val achievements = mutableSetOf<Achievement>().apply {
            achievementDao.getAchievementsByDone(done = false).executeAsList().map { it.toAchievement() }.forEach { achievement ->
                val preconditions = achievement.preconditions
                val ship = gameSession.ship
                val settledHostId = preconditions.settledHostId ?: gameSession.currentStellarHostId
                val settledPlanetId = preconditions.settledPlanetId ?: gameSession.settledPlanetId
                val habitabilitySignal = preconditions.habitability?.firstOrNull() ?: '='
                val habitability = preconditions.habitability?.drop(n = 1)?.toDoubleOrNull() ?: gameSession.finalHabitability
                val yearsTraveledSignal = preconditions.yearsTraveled?.firstOrNull() ?: '='
                val yearsTraveled = preconditions.yearsTraveled?.drop(n = 1)?.toDoubleOrNull() ?: ship.yearsTraveled
                val integritySignal = preconditions.integrity?.firstOrNull() ?: '='
                val integrity = preconditions.integrity?.drop(n = 1)?.toIntOrNull() ?: ship.integrity
                val sensorRangeSignal = preconditions.sensorRange?.firstOrNull() ?: '='
                val sensorRange = preconditions.sensorRange?.drop(n = 1)?.toIntOrNull() ?: ship.sensorRange
                val materialsSignal = preconditions.materials?.firstOrNull() ?: '='
                val materials = preconditions.materials?.drop(n = 1)?.toIntOrNull() ?: ship.materials
                val fuelSignal = preconditions.fuel?.firstOrNull() ?: '='
                val fuel = preconditions.fuel?.drop(n = 1)?.toIntOrNull() ?: ship.fuel
                val cryopodsSignal = preconditions.cryopods?.firstOrNull() ?: '='
                val cryopods = preconditions.cryopods?.drop(n = 1)?.toIntOrNull() ?: ship.cryopods
                if (settledHostId == gameSession.currentStellarHostId && settledPlanetId == gameSession.settledPlanetId && when (habitabilitySignal) {
                        '=' -> gameSession.finalHabitability == habitability
                        '+' -> gameSession.finalHabitability >= habitability
                        '-' -> gameSession.finalHabitability <= habitability
                        else -> false
                    } && when (yearsTraveledSignal) {
                        '=' -> ship.yearsTraveled == yearsTraveled
                        '+' -> ship.yearsTraveled >= yearsTraveled
                        '-' -> ship.yearsTraveled <= yearsTraveled
                        else -> false
                    } && when (integritySignal) {
                        '=' -> ship.integrity == integrity
                        '+' -> ship.integrity >= integrity
                        '-' -> ship.integrity <= integrity
                        else -> false
                    } && when (sensorRangeSignal) {
                        '=' -> ship.sensorRange == sensorRange
                        '+' -> ship.sensorRange >= sensorRange
                        '-' -> ship.sensorRange <= sensorRange
                        else -> false
                    } && when (materialsSignal) {
                        '=' -> ship.materials == materials
                        '+' -> ship.materials >= materials
                        '-' -> ship.materials <= materials
                        else -> false
                    } && when (fuelSignal) {
                        '=' -> ship.fuel == fuel
                        '+' -> ship.fuel >= fuel
                        '-' -> ship.fuel <= fuel
                        else -> false
                    } && when (cryopodsSignal) {
                        '=' -> ship.cryopods == cryopods
                        '+' -> ship.cryopods >= cryopods
                        '-' -> ship.cryopods <= cryopods
                        else -> false
                    }
                ) add(element = achievement.copy(done = true))
            }
        }
        achievementDao.transaction { achievements.forEach { achievementDao.upsertAchievement(Achievement = it.toAchievementSchema()) } }
        achievements.toList()
    }

    companion object Companion {
        private const val TAG = "Achievement"
    }
}
