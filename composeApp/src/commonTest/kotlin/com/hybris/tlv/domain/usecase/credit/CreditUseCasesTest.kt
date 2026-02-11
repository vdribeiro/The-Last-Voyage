package com.hybris.tlv.domain.usecase.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class CreditUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncCredits() = runUnitTest {
        assertTrue(actual = dependency.get().useCases.credit.getCredits().isEmpty())
        dependency.get().useCases.credit.prepopulateCredits()
        assertEquals(expected = FakeData.credits.get().sortedBy { it.id }, actual = dependency.get().useCases.credit.getCredits().sortedBy { it.id })

        resetData()
        assertTrue(actual = dependency.get().useCases.credit.getCredits().isEmpty())
        dependency.get().useCases.credit.syncCredits()
        assertEquals(expected = FakeData.credits.get().sortedBy { it.id }, actual = dependency.get().useCases.credit.getCredits().sortedBy { it.id })
    }
}
