package com.hybris.tlv.data.http

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Headers
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.util.date.GMTDate
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import com.hybris.tlv.core.flow.Dispatcher

/**
 * A no-op implementation of [HttpClientEngine].
 */
internal object NoOpHttpEngine: HttpClientEngine {
    @InternalAPI
    override suspend fun execute(data: HttpRequestData): HttpResponseData =
        HttpResponseData(
            statusCode = HttpStatusCode.NoContent,
            requestTime = GMTDate(),
            headers = Headers.Empty,
            version = HttpProtocolVersion.HTTP_2_0,
            body = ByteReadChannel.Empty,
            callContext = coroutineContext
        )

    override val config: HttpClientEngineConfig = HttpClientEngineConfig()
    override val dispatcher: CoroutineDispatcher = Dispatcher.IO
    override val coroutineContext: CoroutineContext = dispatcher

    override fun close() {
        coroutineContext.cancel()
    }
}
