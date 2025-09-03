package com.hybris.tlv.usecase.learning.local

import com.hybris.tlv.usecase.learning.mapper.toLearning
import com.hybris.tlv.usecase.learning.mapper.toLearningSchema
import com.hybris.tlv.usecase.learning.model.Learning
import database.AppDatabase

internal class LearningDao(
    database: AppDatabase
): LearningLocal {

    private val learningDao = database.learningQueries

    override fun isLearningEmpty(): Boolean =
        learningDao.isLearningEmpty().executeAsList().isEmpty()

    override fun rewriteLearnings(learnings: List<Learning>) = learningDao.transaction {
        learningDao.truncateLearning()
        learnings.forEach { learningDao.upsertLearning(Learning = it.toLearningSchema()) }
    }

    override fun getLearnings(): List<Learning> =
        learningDao.getLearnings().executeAsList().map { it.toLearning() }
}
