package com.hybris.tlv.usecase.credit.remote

import com.hybris.tlv.http.CREDITS_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.usecase.credit.model.Credit
import io.ktor.client.HttpClient

internal class CreditApi(
    private val httpClient: HttpClient,
): CreditRemote {

    override suspend fun getCredits(): Result<Credit> =
        httpClient.getStream(path = CREDITS_URL)
}
