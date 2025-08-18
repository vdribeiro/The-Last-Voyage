package com.hybris.tlv.usecase.ship.local

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.engines
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class ShipLocalTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.reset()
    }

    @Test
    fun `write and get engines`() = runBlocking {
        assertTrue(actual = mock.shipDao.isEngineEmpty())
        mock.shipDao.rewriteEngines(engines = engines)
        assertEquals(expected = engines, actual = mock.shipDao.getEngines())
    }
}
