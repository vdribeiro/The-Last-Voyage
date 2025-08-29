package com.hybris.tlv.usecase.credit

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class CreditUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `prepopulate and get credits`() = runTest {
        assertTrue(actual = mock.useCases.credit.getCredits().isEmpty())
        mock.internalCredit.prepopulateCredits()
        assertTrue(actual = mock.useCases.credit.getCredits().isNotEmpty())
    }

    @Test
    fun `prepopulate and sync credits`() = runTest {
        assertTrue(actual = mock.useCases.credit.getCredits().isEmpty())
        assertTrue(actual = mock.internalCredit.syncCredits() is SyncResult.Success)
        assertTrue(actual = mock.useCases.credit.getCredits().isNotEmpty())
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.internalCredit.syncCredits() is SyncResult.Error)
    }
}
