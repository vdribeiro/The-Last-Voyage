package com.hybris.tlv.http.client

import io.ktor.client.HttpClient

internal interface HttpClientFactory {
    fun buildExoplanetHttpClient(): HttpClient
}
