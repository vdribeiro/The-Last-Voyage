package com.hybris.tlv.usecase.ship.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.engines
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class ShipRemoteTest {

    @Test
    fun `get engines`() = runTest {
        assertEquals(expected = Result.Success(list = engines), actual = mock.shipApi.getEngines())
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.shipApi.getEngines() is Result.Error)
    }
}
