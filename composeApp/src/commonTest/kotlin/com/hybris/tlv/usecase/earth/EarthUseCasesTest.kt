package com.hybris.tlv.usecase.earth

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EarthUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `prepopulate and get catastrophes`() = runBlocking {
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isEmpty())
        mock.internalEarth.prepopulateCatastrophes()
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isNotEmpty())
    }

    @Test
    fun `prepopulate and sync catastrophes`() = runBlocking {
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isEmpty())
        assertTrue(actual = mock.internalEarth.syncCatastrophes() is SyncResult.Success)
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isNotEmpty())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.internalEarth.syncCatastrophes() is SyncResult.Error)
    }
}
