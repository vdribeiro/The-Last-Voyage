package com.hybris.tlv.ui.screen.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.credits
import com.hybris.tlv.ui.navigation.Screen

internal class CreditStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.credit.prepopulateCredits()
        val store = storeFactory.getCreditStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = credits, actual = store.state.credits)
    }

    @Test
    fun initWithoutCredits() = runUnitTest {
        val store = storeFactory.getCreditStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.credits.isEmpty())
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Credit)
        assertNavigation(list = listOf(Screen.Credit))
        storeFactory.getCreditStore().back()
        assertNavigation(list = emptyList())
    }
}
