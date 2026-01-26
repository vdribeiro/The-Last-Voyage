package com.hybris.tlv.domain.usecase.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class CreditUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncCredits() = runUnitTest {
        assertTrue(actual = getUseCases().credit.getCredits().isEmpty())
        getUseCases().credit.prepopulateCredits()
        assertEquals(expected = FakeData.credits.get().sortedBy { it.id }, actual = getUseCases().credit.getCredits().sortedBy { it.id })

        reset()
        assertTrue(actual = getUseCases().credit.getCredits().isEmpty())
        getUseCases().credit.syncCredits()
        assertEquals(expected = FakeData.credits.get().sortedBy { it.id }, actual = getUseCases().credit.getCredits().sortedBy { it.id })
    }
}
