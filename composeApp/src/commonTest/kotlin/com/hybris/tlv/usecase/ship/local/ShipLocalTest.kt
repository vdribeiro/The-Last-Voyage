package com.hybris.tlv.usecase.ship.local

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.engines
import com.hybris.tlv.mock.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class ShipLocalTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `write and get engines`() = runTest {
        assertTrue(actual = mock.shipDao.isEngineEmpty())
        mock.shipDao.rewriteEngines(engines = engines)
        assertEquals(expected = engines, actual = mock.shipDao.getEngines())
    }
}
