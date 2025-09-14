package com.hybris.tlv.usecase.learning

import com.hybris.tlv.database.LearningSchema
import com.hybris.tlv.http.HttpClientFactory.Companion.LEARNINGS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.learning.model.Learning
import database.AppDatabase
import io.ktor.client.HttpClient

internal class LearningGateway(
    private val httpClient: HttpClient,
    database: AppDatabase
): LearningUseCases {

    private val learningDao = database.learningQueries

    override suspend fun syncLearnings(): Result<Learning> =
        httpClient.getStream<Learning>(path = LEARNINGS_URL)

    override suspend fun prepopulateLearnings() {
        if (learningDao.isLearningEmpty().executeAsList().isEmpty()) {
            val learnings: List<Learning> = loadFromJson(path = "files/learnings.json")
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
}
