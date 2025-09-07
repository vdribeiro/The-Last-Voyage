package com.hybris.tlv.http

import io.ktor.client.HttpClient

internal object HttpClientFactory {

    fun buildHttpClient(): HttpClient = HttpClient {
        setLogging()
        setTimeout(timeout = 10_000L)
        setCache()
        setContentValidator()
        setContentEncoding(compressionQuality = 0.9F)
    }
}
