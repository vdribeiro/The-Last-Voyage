package com.hybris.tlv.usecase.ship.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.engines
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class ShipRemoteTest {

    private val mock = Mock()

    @Test
    fun `write and get engines`() = runBlocking {
        assertEquals(expected = Result.Success(list = engines), actual = mock.shipApi.getEngines())
    }
}
