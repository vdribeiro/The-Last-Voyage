package com.hybris.tlv.usecase.learning.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.usecase.learning.model.Learning

internal interface LearningRemote {

    /**
     * Get learning from the API.
     */
    suspend fun getLearnings(): Result<Learning>
}
