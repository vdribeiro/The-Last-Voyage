package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.achievement.model.Achievement
import kotlinx.coroutines.flow.Flow

internal interface AchievementUseCases {

    /**
     * Rewrites the local and remote [Achievement] data.
     */
    suspend fun setup(): Flow<SyncResult>

    /**
     * Syncs the remote [Achievement] data to local.
     */
    suspend fun syncAchievements(): Flow<SyncResult>

    /**
     * Prepopulate local [Achievement].
     */
    suspend fun prepopulateAchievements()

    /**
     * Get [Achievement] from the database.
     */
    suspend fun getAchievements(): List<Achievement>
}
