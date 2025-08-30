package com.hybris.tlv.usecase.sync

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

internal class SyncUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun sync() = runBlocking {
        val totalOperations = 8f
        val sync = mock.useCases.sync.sync().toList()
        for (i in 0..totalOperations.toInt() - 1) {
            assertEquals(expected = SyncResult.Loading(progress = i.toFloat(), total = totalOperations), actual = sync[i])
        }
        assertEquals(expected = SyncResult.Success, actual = sync.last())
    }

    @Test
    fun `get archive`() = runBlocking {
        val totalOperations = 6f
        val archive = mock.useCases.sync.getArchive().toList()
        for (i in 0..totalOperations.toInt() - 1) {
            assertEquals(expected = SyncResult.Loading(progress = i.toFloat(), total = totalOperations), actual = archive[i])
        }
        assertEquals(expected = SyncResult.Success, actual = archive.last())
    }

    @Test
    fun `get error`() = runBlocking {
        val totalOperations = 8f
        val errorSync = errorMock.useCases.sync.sync().toList()
        for (i in 0..totalOperations.toInt() - 1) {
            assertEquals(expected = SyncResult.Loading(progress = i.toFloat(), total = totalOperations), actual = errorSync[i])
        }
        assertEquals(expected = SyncResult.Success, actual = errorSync.last())
    }
}
