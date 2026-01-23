package com.hybris.tlv.domain.usecase.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class CreditUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncCredits() = runUnitTest {
        assertTrue(actual = useCases.credit.getCredits().isEmpty())
        useCases.credit.prepopulateCredits()
        assertEquals(expected = FakeData.getCredits().sortedBy { it.id }, actual = useCases.credit.getCredits().sortedBy { it.id })

        reset()
        assertTrue(actual = useCases.credit.getCredits().isEmpty())
        useCases.credit.syncCredits()
        assertEquals(expected = FakeData.getCredits().sortedBy { it.id }, actual = useCases.credit.getCredits().sortedBy { it.id })
    }
}
