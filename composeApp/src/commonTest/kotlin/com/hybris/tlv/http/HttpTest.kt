package com.hybris.tlv.http

import kotlin.test.Test
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.config.Configs

internal class HttpTest: TestCase() {

    @Test
    fun networkFailure() = runUnitTest {
        val httpClient = HttpClientFactory(engine = TestEngine.mock).httpClient
        val response = httpClient.getStream<String>(path = URL.Configs)
        assertTrue(actual = response is Result.Error)
    }

    @Test
    fun networkSuccess() = runUnitTest {
        val httpClient = HttpClientFactory(engine = TestEngine.mock).httpClient
        val response = httpClient.getStream<Configs>(path = URL.Configs)
        assertTrue(actual = response is Result.Success)
    }
}
