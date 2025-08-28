package com.hybris.tlv.usecase.credit

import com.hybris.tlv.Core
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class CreditUseCasesTest {

    private val mock = Mock()
    private val errorMock = Mock(httpClient = HttpClientFactory.buildErrorHttpClient())

    @BeforeTest
    fun setup() {
        mock.clearDatabase()

        val core: Core = Core()
        core.achievementDao
    }

    @Test
    fun `prepopulate and get credits`() = runBlocking {
        assertTrue(actual = mock.useCases.credit.getCredits().isEmpty())
        mock.internalCredit.prepopulateCredits()
        assertTrue(actual = mock.useCases.credit.getCredits().isNotEmpty())
    }

    @Test
    fun `prepopulate and sync credits`() = runBlocking {
        assertTrue(actual = mock.useCases.credit.getCredits().isEmpty())
        assertTrue(actual = mock.internalCredit.syncCredits() is SyncResult.Success)
        assertTrue(actual = mock.useCases.credit.getCredits().isNotEmpty())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.internalCredit.syncCredits() is SyncResult.Error)
    }
}
