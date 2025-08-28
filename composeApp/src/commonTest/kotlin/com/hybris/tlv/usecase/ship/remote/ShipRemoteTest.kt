package com.hybris.tlv.usecase.ship.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.engines
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class ShipRemoteTest {

    @Test
    fun `get engines`() = runBlocking {
        assertEquals(expected = Result.Success(list = engines), actual = mock.shipApi.getEngines())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.shipApi.getEngines() is Result.Error)
    }
}
