package com.hybris.tlv.usecase.learning

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.database.LearningSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.LEARNINGS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.LEARNINGS_JSON
import com.hybris.tlv.serializer.loadFromJsonResource
import com.hybris.tlv.telemetry.Logger
import com.hybris.tlv.usecase.learning.model.Learning
import database.AppDatabase
import io.ktor.client.HttpClient

internal class LearningGateway(
    private val config: ConfigManager,
    private val httpClient: HttpClient,
    database: AppDatabase
): LearningUseCases {

    private val learningDao = database.learningQueries

    override suspend fun syncLearnings() {
        if (config.remoteConfigs.learningsVersion > config.localConfigs.learningsVersion) {
            when (val result = httpClient.getStream<Learning>(path = LEARNINGS_URL)) {
                is Result.Error -> Logger.error(tag = TAG, message = "Unable to get learnings", throwable = result.error)
                is Result.Success -> rewriteLearnings(learnings = result.list)
            }
            config.localConfigs = config.localConfigs.copy(learningsVersion = config.remoteConfigs.learningsVersion)
        }
    }

    override suspend fun prepopulateLearnings() {
        if (learningDao.isLearningEmpty().executeAsList().isEmpty()) {
            val learnings: List<Learning> = loadFromJsonResource(path = LEARNINGS_JSON)
            rewriteLearnings(learnings = learnings)
        }
    }

    private fun rewriteLearnings(learnings: List<Learning>) = learningDao.transaction {
        learningDao.truncateLearning()
        learnings.forEach { learningDao.upsertLearning(Learning = it.toLearningSchema()) }
    }

    override suspend fun getLearnings(): List<Learning> =
        learningDao.getLearnings().executeAsList().map { it.toLearning() }

    private fun Learning.toLearningSchema(): LearningSchema =
        LearningSchema(
            id = id,
            description = description,
            image = image,
            type = type,
        )

    private fun LearningSchema.toLearning(): Learning =
        Learning(
            id = id,
            description = description,
            image = image,
            type = type,
        )

    companion object Companion {
        private const val TAG = "Learning"
    }
}
