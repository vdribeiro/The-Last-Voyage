package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.sync.model.SyncResult

internal interface AchievementInternalUseCases {

    /**
     * Syncs the remote [Achievement] data to local.
     */
    suspend fun syncAchievements(): SyncResult

    /**
     * Prepopulate local [Achievement].
     */
    suspend fun prepopulateAchievements()
}
