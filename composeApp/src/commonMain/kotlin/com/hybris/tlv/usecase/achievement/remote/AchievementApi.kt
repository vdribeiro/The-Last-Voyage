package com.hybris.tlv.usecase.achievement.remote

import com.hybris.tlv.http.ACHIEVEMENTS_URL
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.json
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.achievement.model.Achievement
import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.isSuccess
import io.ktor.utils.io.toByteArray

internal class AchievementApi(
    private val httpClient: HttpClient
): AchievementRemote {

    override suspend fun getAchievements(): Result<Achievement> = runCatching {
        httpClient.prepareGet(urlString = ACHIEVEMENTS_URL).execute { httpResponse ->
            if (!httpResponse.status.isSuccess()) return@execute Result.Error(error = "Unsuccessful response: ${httpResponse.status}")
            val channel = httpResponse.bodyAsChannel()
            val bytes = channel.toByteArray()
            Result.Success(list = json.decodeFromString<List<Achievement>>(string = bytes.decodeToString()))
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        Result.Error(error = it.message.orEmpty())
    }

    companion object Companion {
        private const val TAG = "AchievementApi"
    }
}
