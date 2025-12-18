package com.hybris.tlv.screen

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.TestCase
import com.hybris.tlv.navigation.Screen

internal class StoreTest: TestCase() {

    @Test
    fun navigate() = runUnitTest {
        val store = Store<Unit, Unit>(initialState = Unit)
        assertEquals(expected = screens.lastOrNull(), actual = null)

        navigate(screen = Screen.Achievement)
        assertEquals(expected = screens.lastOrNull(), actual = Screen.Achievement)

        store.back()
        assertEquals(expected = screens.lastOrNull(), actual = null)

        // TODO
    }
}