package com.hybris.tlv.http

import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.encodeURLPath
import io.ktor.http.isSuccess
import io.ktor.utils.io.toByteArray
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.distantPast
import com.hybris.tlv.locale.hasTimePassed
import com.hybris.tlv.locale.now
import com.hybris.tlv.platform.isDebug
import com.hybris.tlv.serializer.decode
import com.hybris.tlv.telemetry.Telemetry

internal suspend inline fun <reified T> HttpClient.getStream(
    path: String,
    queryMap: Map<String, String> = emptyMap(),
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = runCatching {
    withContext(context = Dispatcher.IO) {
        if (!isInternetAvailableDebounced()) throw Throwable(message = "No internet connection available.")
        prepareGet(urlString = path.encodeURLPath()) {
            queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
            block()
        }.execute { httpResponse ->
            val list: List<T> = httpResponse.body()
            Result.Success(list = list)
        }
    }
}.getOrElse { Result.Error(error = it) }

private suspend fun isInternetAvailableDebounced(): Boolean = runCatching {
    mutex.withLock {
        if (!hasTimePassed(dateTime = lastCheckTime, duration = cacheTTL)) return@withLock lastKnownStatus
        lastKnownStatus = isInternetAvailable()
        lastCheckTime = now()
        return@withLock lastKnownStatus
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it) }.getOrDefault(defaultValue = false)

private const val TAG = "Network"

private val mutex = Mutex()
private val cacheTTL: Duration = if (isDebug) ZERO else 5.seconds
private var lastCheckTime = distantPast()
private var lastKnownStatus = false
