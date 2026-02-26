package com.hybris.tlv.ui.screen.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class CreditStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.credit.prepopulateCredits()
        val store = storeFactory.get().getCreditStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = FakeData.credits.get(), actual = store.state.credits)
    }

    @Test
    fun initWithoutCredits() = runUnitTest {
        val store = storeFactory.get().getCreditStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.credits.isEmpty())
    }
}
