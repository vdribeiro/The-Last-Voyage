package com.hybris.tlv.usecase.learning.remote

import com.hybris.tlv.http.LEARNINGS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.usecase.learning.model.Learning
import io.ktor.client.HttpClient

internal class LearningApi(
    private val httpClient: HttpClient
): LearningRemote {

    override suspend fun getLearnings(): Result<Learning> =
        httpClient.getStream(path = LEARNINGS_URL)
}
