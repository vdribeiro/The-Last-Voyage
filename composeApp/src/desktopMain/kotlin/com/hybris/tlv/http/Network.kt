package com.hybris.tlv.http

import com.hybris.tlv.telemetry.Telemetry
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.head

internal actual suspend fun isInternetAvailable(): Boolean = runCatching {
    val response = httpClient.head(urlString = "https://clients3.google.com/generate_204")
    return response.status.value in 200..299
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to check connectivity", throwable = it)
    false
}

private val httpClient by lazy {
    HttpClient {
        install(plugin = HttpTimeout) {
            connectTimeoutMillis = 1500L
            socketTimeoutMillis = 1500L
            requestTimeoutMillis = 1500L
        }
    }
}

private const val TAG = "Network"
