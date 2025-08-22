package com.hybris.tlv.usecase.earth.remote

import com.hybris.tlv.http.CATASTROPHES_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.usecase.earth.model.Catastrophe
import io.ktor.client.HttpClient

internal class EarthApi(
    private val httpClient: HttpClient
): EarthRemote {

    override suspend fun getCatastrophes(): Result<Catastrophe> =
        httpClient.getStream(path = CATASTROPHES_URL)
}
