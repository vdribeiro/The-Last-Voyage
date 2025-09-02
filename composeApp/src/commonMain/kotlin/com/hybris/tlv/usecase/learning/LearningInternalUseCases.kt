package com.hybris.tlv.usecase.learning

import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.sync.model.SyncResult

internal interface LearningInternalUseCases {

    /**
     * Syncs the remote [Learning] data to local.
     */
    suspend fun syncLearnings(): SyncResult

    /**
     * Prepopulate local [Learning].
     */
    suspend fun prepopulateLearnings()

    /**
     * Upsert a [Learning] into the database.
     */
    suspend fun upsertLearning(learning: Learning)
}
