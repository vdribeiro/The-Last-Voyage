package com.hybris.tlv.ui.screen.credit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class CreditStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.credit.prepopulateCredits()
        val store = getStoreFactory().getCreditStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = FakeData.credits.get(), actual = store.state.credits)
    }

    @Test
    fun initWithoutCredits() = runUnitTest {
        val store = getStoreFactory().getCreditStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.credits.isEmpty())
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Credit)
        assertNavigation(list = listOf(Screen.Credit))
        getStoreFactory().getCreditStore().back()
        assertNavigation(list = emptyList())
    }
}
