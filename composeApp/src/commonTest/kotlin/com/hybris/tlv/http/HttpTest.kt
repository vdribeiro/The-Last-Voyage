package com.hybris.tlv.http

import kotlin.test.Test
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.config.Configs
import com.hybris.tlv.http.HttpClientFactory.Companion.CONFIGS_URL

internal class HttpTest: TestCase() {

    @Test
    fun networkFailure() = runUnitTest {
        val httpClient = HttpClientFactory(engine = TestEngine.mock).httpClient
        val response = httpClient.getStream<String>(path = "Unknown")
        assertTrue(actual = response is Result.Error)
    }

    @Test
    fun networkSuccess() = runUnitTest {
        val httpClient = HttpClientFactory(engine = TestEngine.mock).httpClient
        val response = httpClient.getStream<Configs>(path = CONFIGS_URL)
        assertTrue(actual = response is Result.Success)
    }
}
