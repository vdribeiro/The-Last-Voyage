package com.hybris.tlv.usecase.achievement.remote

import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.domain.Result
import com.hybris.tlv.usecase.domain.SyncResult
import kotlinx.coroutines.flow.Flow

internal interface AchievementRemote {

    /**
     * Rewrite [achievements] in the API.
     */
    suspend fun rewriteAchievements(achievements: List<Achievement>): Flow<SyncResult>

    /**
     * Get achievements from the API given the [queryMap].
     */
    suspend fun getAchievements(queryMap: QueryMap = QueryMap()): Flow<Result<Achievement>>
}
