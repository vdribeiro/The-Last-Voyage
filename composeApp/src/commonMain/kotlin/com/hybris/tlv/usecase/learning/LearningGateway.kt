package com.hybris.tlv.usecase.learning

import com.hybris.tlv.usecase.learning.local.LearningLocal
import com.hybris.tlv.usecase.learning.model.Learning

internal class LearningGateway(
    private val learningDao: LearningLocal
): LearningUseCases {

    override suspend fun getLearnings(): List<Learning> =
        learningDao.getLearnings()
}
