package com.hybris.tlv.usecase.credit

import kotlin.test.Test
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase

internal class CreditUseCasesTest: TestCase() {

    @Test
    fun `sync and get credits`() = runUnitTest {
        assertTrue(actual = useCases.credit.getCredits().isEmpty())
        useCases.credit.syncCredits()
        assertTrue(actual = useCases.credit.getCredits().isNotEmpty())
    }
}
