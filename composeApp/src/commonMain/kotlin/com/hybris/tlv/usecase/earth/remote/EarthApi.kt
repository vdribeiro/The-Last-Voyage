package com.hybris.tlv.usecase.earth.remote

import com.hybris.tlv.http.CATASTROPHES_URL
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.json
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.earth.model.Catastrophe
import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.isSuccess
import io.ktor.utils.io.toByteArray

internal class EarthApi(
    private val httpClient: HttpClient
): EarthRemote {

    override suspend fun getCatastrophes(): Result<Catastrophe> = runCatching {
        httpClient.prepareGet(urlString = CATASTROPHES_URL).execute { httpResponse ->
            if (!httpResponse.status.isSuccess()) return@execute Result.Error(error = "Unsuccessful response: ${httpResponse.status}")
            val channel = httpResponse.bodyAsChannel()
            val bytes = channel.toByteArray()
            Result.Success(list = json.decodeFromString<List<Catastrophe>>(string = bytes.decodeToString()))
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        Result.Error(error = it.message.orEmpty())
    }

    companion object {
        private const val TAG = "EarthApi"
    }
}
