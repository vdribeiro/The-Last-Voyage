package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.achievement.local.AchievementLocal
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.remote.AchievementRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import thelastvoyage.composeapp.generated.resources.Res

internal class AchievementGateway(
    private val achievementApi: AchievementRemote,
    private val achievementDao: AchievementLocal
): AchievementUseCases {

    private suspend fun loadAchievementsFromJson(): List<Achievement> = runCatching {
        val jsonString = Res.readBytes(path = "files/achievements.json").decodeToString()
        json.decodeFromString<List<Achievement>>(string = jsonString)
    }.getOrDefault(defaultValue = emptyList())

    override suspend fun setup(): Flow<SyncResult> {
        val achievements = loadAchievementsFromJson()
        achievementDao.rewriteAchievements(achievements = achievements)
        return achievementApi.rewriteAchievements(achievements = achievements)
    }

    override suspend fun syncAchievements(): Flow<SyncResult> =
        achievementApi.getAchievements(queryMap = QueryMap().apply {
            paginate = true
            limit = 1000
        }).map { result ->
            when (result) {
                is Result.Error -> {
                    prepopulateAchievements()
                    SyncResult.Error(error = result.error)
                }

                is Result.PartialSuccess -> SyncResult.Loading(
                    progress = result.list.size.toFloat(),
                    total = result.total.toFloat()
                )

                is Result.Success -> {
                    achievementDao.rewriteAchievements(achievements = result.list)
                    SyncResult.Success
                }
            }
        }

    override suspend fun prepopulateAchievements() {
        if (achievementDao.isAchievementEmpty()) {
            val achievements = loadAchievementsFromJson()
            achievementDao.rewriteAchievements(achievements = achievements)
            true
        }
    }

    override suspend fun getAchievements(): List<Achievement> =
        achievementDao.getAchievements()
}
