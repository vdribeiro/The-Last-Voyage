package com.hybris.tlv.usecase.learning

import com.hybris.tlv.database.LearningSchema
import com.hybris.tlv.usecase.learning.model.Learning
import database.AppDatabase

internal class LearningGateway(
    database: AppDatabase
): LearningUseCases {

    private val learningDao = database.learningQueries

    override suspend fun getLearnings(): List<Learning> =
        learningDao.getLearnings().executeAsList().map { it.toLearning() }

    private fun LearningSchema.toLearning(): Learning =
        Learning(
            id = id,
            description = description,
            image = image,
            type = type,
        )
}
