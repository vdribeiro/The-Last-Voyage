package com.hybris.tlv.http

import io.ktor.client.HttpClient

internal object HttpClientFactory {

    fun buildHttpClient(): HttpClient = HttpClient {
        setLogging()
        setTimeout(timeout = 60_000L * 5)
        setCache()
        setContentValidator()
        setContentEncoding(compressionQuality = 0.9F)
    }
}