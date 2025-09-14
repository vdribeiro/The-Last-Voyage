package com.hybris.tlv.usecase.learning

import com.hybris.tlv.usecase.learning.model.Learning

internal interface LearningUseCases {

    /**
     * Sync [Learning]s.
     */
    suspend fun syncLearnings()

    /**
     * Prepopulate [Learning]s.
     */
    suspend fun prepopulateLearnings()

    /**
     * Get all [Learning]s.
     */
    suspend fun getLearnings(): List<Learning>
}
