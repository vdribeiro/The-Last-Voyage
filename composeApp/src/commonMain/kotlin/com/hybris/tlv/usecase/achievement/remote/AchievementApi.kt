package com.hybris.tlv.usecase.achievement.remote

import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.achievement.mapper.toAchievement
import com.hybris.tlv.usecase.achievement.mapper.toAchievementMap
import com.hybris.tlv.usecase.achievement.model.Achievement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

internal class AchievementApi(
    private val firestore: Firestore
): AchievementRemote {

    override suspend fun rewriteAchievements(achievements: List<Achievement>): Flow<SyncResult> = runCatching {
        firestore.removeCollection(collection = ACHIEVEMENTS).last()
        firestore.setCollection(collection = ACHIEVEMENTS, documents = achievements.map { it.toAchievementMap() }).map { result ->
            when (result) {
                is FirestoreWriteResult.Error -> SyncResult.Error(error = result.error)
                is FirestoreWriteResult.PartialSuccess -> SyncResult.Loading(
                    progress = result.documents.size.toFloat(),
                    total = result.totalDocuments.toFloat()
                )

                is FirestoreWriteResult.Success -> SyncResult.Success
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = SyncResult.Error(error = it.message.orEmpty()))
    }

    override suspend fun getAchievements(queryMap: QueryMap): Flow<Result<Achievement>> = runCatching {
        firestore.getCollection(collection = ACHIEVEMENTS, queryMap = queryMap).map { result ->
            when (result) {
                is FirestoreReadResult.Error -> Result.Error(error = result.error)
                is FirestoreReadResult.PartialSuccess -> Result.PartialSuccess(
                    list = result.documents.map { it.toAchievement() },
                    total = result.totalDocuments
                )

                is FirestoreReadResult.Success -> Result.Success(list = result.documents.map { it.toAchievement() })
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = Result.Error(error = it.message.orEmpty()))
    }

    companion object Companion {
        private const val TAG = "AchievementApi"
        private const val ACHIEVEMENTS = "achievements"
        const val ACHIEVEMENTS_ID = "id"
        const val ACHIEVEMENTS_NAME = "name"
        const val ACHIEVEMENTS_DESCRIPTION = "description"
        const val ACHIEVEMENTS_PRECONDITIONS = "preconditions"
        const val ACHIEVEMENTS_STATUS = "status"
    }
}
