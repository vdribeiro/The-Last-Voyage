package com.hybris.tlv.usecase.catastrophe

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EarthUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `prepopulate and get catastrophes`() = runBlocking {
        assertNull(actual = mock.useCases.catastrophe.getRandomCatastrophe())
        mock.internalEarth.prepopulateCatastrophes()
        assertNotNull(actual = mock.useCases.catastrophe.getRandomCatastrophe()).let {}
    }

    @Test
    fun `prepopulate and sync catastrophes`() = runBlocking {
        assertNull(actual = mock.useCases.catastrophe.getRandomCatastrophe())
        assertTrue(actual = mock.internalEarth.syncCatastrophes() is SyncResult.Success)
        assertNotNull(actual = mock.useCases.catastrophe.getRandomCatastrophe()).let {}
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.internalEarth.syncCatastrophes() is SyncResult.Error)
    }
}
