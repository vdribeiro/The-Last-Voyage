package com.hybris.tlv.usecase.ship.remote

import com.hybris.tlv.http.ENGINES_URL
import com.hybris.tlv.http.Result
import com.hybris.tlv.http.getStream
import com.hybris.tlv.usecase.ship.model.Engine
import io.ktor.client.HttpClient

internal class ShipApi(
    private val httpClient: HttpClient
): ShipRemote {

    override suspend fun getEngines(): Result<Engine> =
        httpClient.getStream(url = ENGINES_URL)
}
