package com.hybris.tlv.usecase.ship.remote

import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.engines
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class ShipRemoteTest {

    @Test
    fun `get engines`() = runBlocking {
        assertEquals(expected = Result.Success(list = engines), actual = Mock().shipApi.getEngines())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = Mock(httpClient = HttpClientFactory.buildErrorHttpClient()).shipApi.getEngines() is Result.Error)
    }
}
