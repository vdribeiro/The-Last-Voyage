package com.hybris.tlv.usecase.learning.local

import com.hybris.tlv.usecase.learning.model.Learning

internal interface LearningLocal {

    /**
     * Returns true if there are no [Learning]s in the database, false otherwise.
     */
    fun isLearningEmpty(): Boolean

    /**
     * Rewrites the [Learning] table with the given [learnings].
     */
    fun rewriteLearnings(learnings: List<Learning>)

    /**
     * Get [Learning]s from the database.
     */
    fun getLearnings(): List<Learning>
}
