package com.hybris.tlv.usecase.ship.local

import com.hybris.tlv.Tester
import com.hybris.tlv.mock.engines
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class ShipLocalTest: Tester() {

    @Test
    fun `write and get engines`() = runBlocking {
        assertTrue(actual = shipDao.isEngineEmpty())
        shipDao.rewriteEngines(engines = engines)
        assertEquals(expected = engines, actual = shipDao.getEngines())
    }
}
