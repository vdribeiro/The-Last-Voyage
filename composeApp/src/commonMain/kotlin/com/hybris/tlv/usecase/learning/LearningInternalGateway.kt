package com.hybris.tlv.usecase.learning

import com.hybris.tlv.http.Result
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.learning.local.LearningLocal
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.remote.LearningRemote
import com.hybris.tlv.usecase.sync.model.SyncResult

internal class LearningInternalGateway(
    private val learningApi: LearningRemote,
    private val learningDao: LearningLocal
): LearningInternalUseCases {

    override suspend fun syncLearnings(): SyncResult =
        when (val result = learningApi.getLearnings()) {
            is Result.Error -> SyncResult.Error(error = result.error)
            is Result.Success -> learningDao.rewriteLearnings(learnings = result.list).let { SyncResult.Success }
        }

    override suspend fun prepopulateLearnings() {
        if (learningDao.isLearningEmpty()) {
            val learnings: List<Learning> = loadFromJson(path = "files/learnings.json")
            learningDao.rewriteLearnings(learnings = learnings)
        }
    }

    override suspend fun upsertLearning(learning: Learning) =
        learningDao.upsertLearning(learning = learning)
}
