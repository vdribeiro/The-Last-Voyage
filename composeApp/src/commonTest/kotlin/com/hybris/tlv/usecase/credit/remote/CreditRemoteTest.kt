package com.hybris.tlv.usecase.credit.remote

import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.credits
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class CreditRemoteTest {

    @Test
    fun `get credits`() = runBlocking {
        assertEquals(expected = Result.Success(list = credits), actual = Mock().creditApi.getCredits())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = Mock(httpClient = HttpClientFactory.buildErrorHttpClient()).creditApi.getCredits() is Result.Error)
    }
}
