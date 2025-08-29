package com.hybris.tlv.usecase.earth

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class EarthUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `prepopulate and get catastrophes`() = runTest {
        assertNull(actual = mock.useCases.earth.getRandomCatastrophe())
        mock.internalEarth.prepopulateCatastrophes()
        assertNotNull(actual = mock.useCases.earth.getRandomCatastrophe()).let {}
    }

    @Test
    fun `prepopulate and sync catastrophes`() = runTest {
        assertNull(actual = mock.useCases.earth.getRandomCatastrophe())
        assertTrue(actual = mock.internalEarth.syncCatastrophes() is SyncResult.Success)
        assertNotNull(actual = mock.useCases.earth.getRandomCatastrophe()).let {}
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.internalEarth.syncCatastrophes() is SyncResult.Error)
    }
}
