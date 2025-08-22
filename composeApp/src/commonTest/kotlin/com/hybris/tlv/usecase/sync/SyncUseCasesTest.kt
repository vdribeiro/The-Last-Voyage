package com.hybris.tlv.usecase.sync

import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
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
    fun `run setup`() = runBlocking {
        val results = mock.useCases.sync.setup().toList()
        assertTrue(actual = results[0] is SyncResult.Loading)
        assertTrue(actual = results[1] is SyncResult.Success)
    }

    @Test
    fun `get archive`() = runBlocking {

    }
}
