package com.hybris.tlv.http.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

internal class AndroidHttpClientFactory: HttpClientFactory {

    override fun buildExoplanetHttpClient(): HttpClient =
        HttpClient(engineFactory = OkHttp) {
            setLogging()
            setTimeout(timeout = 60_000L * 5)
            setCache()
            setRequestUrl(url = EXOPLANET_ARCHIVE_URL)
            setContentValidator()
            setContentEncoding(compressionQuality = 0.9F)
        }
}
