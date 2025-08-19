package com.hybris.tlv.usecase.ship

import com.hybris.tlv.mock.Mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class ShipUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `prepopulate and get engines`() = runBlocking {
        assertTrue(actual = mock.useCases.ship.getEngines().isEmpty())
        mock.internalShip.prepopulateEngines()
        assertTrue(actual = mock.useCases.ship.getEngines().isNotEmpty())
    }

    @Test
    fun `rewrite and sync engines`() = runBlocking {
        assertTrue(actual = mock.useCases.ship.getEngines().isEmpty())
        mock.internalShip.rewriteEngines().last()
        assertTrue(actual = mock.useCases.ship.getEngines().isNotEmpty())
        mock.clearDatabase()
        assertTrue(actual = mock.useCases.ship.getEngines().isEmpty())
        mock.internalShip.syncEngines().last()
        assertTrue(actual = mock.useCases.ship.getEngines().isNotEmpty())
    }
}
