package com.hybris.tlv.usecase.sync

import com.hybris.tlv.database.LearningSchema
import com.hybris.tlv.http.LEARNINGS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.sync.model.SyncResult
import database.AppDatabase
import io.ktor.client.HttpClient

internal class LearningSync(
    private val httpClient: HttpClient,
    database: AppDatabase
) {

    private val learningDao = database.learningQueries

    suspend fun syncLearnings(): SyncResult =
        when (val result = httpClient.getStream<Learning>(path = LEARNINGS_URL)) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> rewriteLearnings(learnings = result.list).let { SyncResult.Success }
        }

    suspend fun prepopulateLearnings() {
        if (learningDao.isLearningEmpty().executeAsList().isEmpty()) {
            val learnings: List<Learning> = loadFromJson(path = "files/learnings.json")
            rewriteLearnings(learnings = learnings)
        }
    }

    private fun rewriteLearnings(learnings: List<Learning>) = learningDao.transaction {
        learningDao.truncateLearning()
        learnings.forEach { learningDao.upsertLearning(Learning = it.toLearningSchema()) }
    }

    private fun Learning.toLearningSchema(): LearningSchema =
        com.hybris.tlv.database.LearningSchema(
            id = id,
            description = description,
            image = image,
            type = type,
        )
}