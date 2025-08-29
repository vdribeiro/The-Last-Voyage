package com.hybris.tlv.usecase.ship

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class ShipUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `prepopulate and get engines`() = runTest {
        assertTrue(actual = mock.useCases.ship.getEngines().isEmpty())
        mock.internalShip.prepopulateEngines()
        assertTrue(actual = mock.useCases.ship.getEngines().isNotEmpty())
    }

    @Test
    fun `prepopulate and sync engines`() = runTest {
        assertTrue(actual = mock.useCases.ship.getEngines().isEmpty())
        assertTrue(actual = mock.internalShip.syncEngines() is SyncResult.Success)
        assertTrue(actual = mock.useCases.ship.getEngines().isNotEmpty())
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.internalShip.syncEngines() is SyncResult.Error)
    }
}
