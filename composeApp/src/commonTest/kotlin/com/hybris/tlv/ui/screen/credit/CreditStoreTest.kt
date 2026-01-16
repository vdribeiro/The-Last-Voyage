package com.hybris.tlv.ui.screen.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.credits
import com.hybris.tlv.ui.navigation.Screen

internal class CreditStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        TestCase.useCases.credit.prepopulateCredits()
        val store = TestCase.storeFactory.getCreditStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = credits, actual = store.state.credits)
    }

    @Test
    fun initWithoutCredits() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getCreditStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.credits.isEmpty())
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Credit)
        TestCase.assertNavigation(list = listOf(Screen.Credit))
        TestCase.storeFactory.getCreditStore().back()
        TestCase.assertNavigation(list = emptyList())
    }
}
