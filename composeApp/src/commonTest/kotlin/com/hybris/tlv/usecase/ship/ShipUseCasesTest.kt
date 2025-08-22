package com.hybris.tlv.usecase.ship

import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class ShipUseCasesTest {

    private val mock = Mock()
    private val errorMock = Mock(httpClient = HttpClientFactory.buildErrorHttpClient())

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
    fun `prepopulate and sync engines`() = runBlocking {
        assertTrue(actual = mock.useCases.ship.getEngines().isEmpty())
        assertTrue(actual = mock.internalShip.syncEngines() is SyncResult.Success)
        assertTrue(actual = mock.useCases.ship.getEngines().isNotEmpty())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.internalShip.syncEngines() is SyncResult.Error)
    }
}
