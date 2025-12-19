package com.hybris.tlv.screen.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.credits
import com.hybris.tlv.navigation.Screen

internal class CreditStoreTest: TestCase() {

    @Test
    fun initStore() = runUnitTest {
        useCases.credit.prepopulateCredits()
        val store = storeFactory.getCreditStore()
        assertEquals(expected = false, actual = store.state().loading)
        assertEquals(expected = credits, actual = store.state().credits)
    }

    @Test
    fun `send action back`() = runUnitTest {
        assertTrue(actual = screens.isEmpty())
        navigate(screen = Screen.Credit)
        assertEquals(expected = listOf(element = Screen.Credit), actual = screens)
        val store = storeFactory.getCreditStore()
        store.back()
        assertTrue(actual = screens.isEmpty())
    }
}
