package com.hybris.tlv.usecase.achievement.remote

import com.hybris.tlv.http.ACHIEVEMENTS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.usecase.achievement.model.Achievement
import io.ktor.client.HttpClient

internal class AchievementApi(
    private val httpClient: HttpClient
): AchievementRemote {

    override suspend fun getAchievements(): Result<Achievement> =
        httpClient.getStream(path = ACHIEVEMENTS_URL)
}
