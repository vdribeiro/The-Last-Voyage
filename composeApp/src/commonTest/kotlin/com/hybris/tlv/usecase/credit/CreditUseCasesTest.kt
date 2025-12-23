package com.hybris.tlv.usecase.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.credits

internal class CreditUseCasesTest: TestCase() {

    @Test
    fun `prepopulate and sync credits`() = runUnitTest {
        assertTrue(actual = useCases.credit.getCredits().isEmpty())
        useCases.credit.prepopulateCredits()
        assertEquals(expected = credits.sortedBy { it.id }, actual = useCases.credit.getCredits().sortedBy { it.id })

        reset()
        assertTrue(actual = useCases.credit.getCredits().isEmpty())
        useCases.credit.syncCredits()
        assertEquals(expected = credits.sortedBy { it.id }, actual = useCases.credit.getCredits().sortedBy { it.id })
    }
}
