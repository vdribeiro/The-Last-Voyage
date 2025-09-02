package com.hybris.tlv.usecase.learning

import com.hybris.tlv.usecase.learning.model.Learning

internal interface LearningUseCases {

    /**
     * Get all [Learning]s.
     */
    suspend fun getLearnings(): List<Learning>
}
