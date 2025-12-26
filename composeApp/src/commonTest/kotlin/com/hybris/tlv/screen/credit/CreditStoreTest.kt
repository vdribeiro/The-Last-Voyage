package com.hybris.tlv.screen.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import com.hybris.tlv.TestCase
import com.hybris.tlv.credits
import com.hybris.tlv.navigation.Screen

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
        assertEquals(expected = emptyList(), actual = store.state.credits)
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        navigate(screen = Screen.Credit)
        assertNavigationBackstack(list = listOf(element = Screen.Credit))
        storeFactory.getCreditStore().back()
        assertNavigationBackstack(list = emptyList())
    }
}
