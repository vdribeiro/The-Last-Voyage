package com.hybris.tlv.http.client

import io.ktor.client.HttpClient

internal expect object HttpClientFactory {
    fun buildExoplanetHttpClient(): HttpClient
}
