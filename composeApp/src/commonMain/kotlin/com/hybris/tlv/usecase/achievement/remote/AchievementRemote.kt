package com.hybris.tlv.usecase.achievement.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.usecase.achievement.model.Achievement

internal interface AchievementRemote {

    /**
     * Get achievements from the API.
     */
    suspend fun getAchievements(): Result<Achievement>
}
