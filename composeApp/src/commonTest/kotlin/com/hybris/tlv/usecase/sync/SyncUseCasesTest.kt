package com.hybris.tlv.usecase.sync

import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

internal class SyncUseCasesTest {

    private val mock = Mock()
    private val errorMock = Mock(httpClient = HttpClientFactory.buildErrorHttpClient())

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `prepopulate and sync`() = runBlocking {
        mock.useCases.sync.setup()
        val totalOperations = 8f

        val prepopulate = mock.useCases.sync.prepopulate().toList()
        for (i in 0..totalOperations.toInt() - 1) {
            assertEquals(expected = SyncResult.Loading(progress = i.toFloat(), total = totalOperations), actual = prepopulate[i])
        }
        assertEquals(expected = SyncResult.Success, actual = prepopulate.last())

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
}
