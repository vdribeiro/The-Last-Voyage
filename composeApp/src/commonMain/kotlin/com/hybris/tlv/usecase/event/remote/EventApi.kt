package com.hybris.tlv.usecase.event.remote

import com.hybris.tlv.http.EVENTS_URL
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.serializer.json
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.event.model.Event
import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.isSuccess
import io.ktor.utils.io.toByteArray

internal class EventApi(
    private val httpClient: HttpClient
): EventRemote {

    override suspend fun getEvents(): Result<Event> = runCatching {
        httpClient.prepareGet(urlString = EVENTS_URL).execute { httpResponse ->
            if (!httpResponse.status.isSuccess()) return@execute Result.Error(error = "Unsuccessful response: ${httpResponse.status}")
            val channel = httpResponse.bodyAsChannel()
            val bytes = channel.toByteArray()
            Result.Success(list = json.decodeFromString<List<Event>>(string = bytes.decodeToString()))
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        Result.Error(error = it.message.orEmpty())
    }

    companion object {
        private const val TAG = "EventApi"
    }
}
