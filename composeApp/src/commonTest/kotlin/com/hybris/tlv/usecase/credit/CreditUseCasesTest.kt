package com.hybris.tlv.usecase.credit

import com.hybris.tlv.mock.Mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class CreditUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `prepopulate and get credits`() = runBlocking {
        assertTrue(actual = mock.useCases.credit.getCredits().isEmpty())
        mock.internalCredit.prepopulateCredits()
        assertTrue(actual = mock.useCases.credit.getCredits().isNotEmpty())
    }

    @Test
    fun `rewrite and sync credits`() = runBlocking {
        assertTrue(actual = mock.useCases.credit.getCredits().isEmpty())
        mock.internalCredit.rewriteCredits().last()
        assertTrue(actual = mock.useCases.credit.getCredits().isNotEmpty())
        mock.clearDatabase()
        assertTrue(actual = mock.useCases.credit.getCredits().isEmpty())
        mock.internalCredit.syncCredits().last()
        assertTrue(actual = mock.useCases.credit.getCredits().isNotEmpty())
    }
}
