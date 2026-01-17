package com.hybris.tlv.data.http

import kotlin.test.Test
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.domain.usecase.translation.model.Translation

internal class HttpTest: TestCase() {

    @Test
    fun networkFailure() = runUnitTest {
        val httpClient = HttpClientFactory(engine = TestEngine.mock).httpClient
        val response = httpClient.get<String>(path = URL.Configs)
        assertTrue(actual = response is Result.Error)
    }

    @Test
    fun networkSuccess() = runUnitTest {
        val httpClient = HttpClientFactory(engine = TestEngine.mock).httpClient
        val response = httpClient.get<Translation>(path = URL.Translations)
        assertTrue(actual = response is Result.Success)
    }
}
