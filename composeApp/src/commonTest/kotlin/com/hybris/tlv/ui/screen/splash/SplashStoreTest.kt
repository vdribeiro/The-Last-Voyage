package com.hybris.tlv.ui.screen.splash

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class SplashStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        val store = getStoreFactory().getSplashStore(reset = true)
        assertNotNull(actual = store.setupJob)
        store.setupJob?.join()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = 1f, actual = store.state.progress)
        assertEquals(expected = Content.INTRO, actual = store.state.currentContent)
    }

    @Test
    fun next() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = getStoreFactory().getSplashStore(reset = true)
        store.setupJob?.join()
        store.send(action = SplashAction.Next)
        assertNavigation(list = listOf(Screen.MainMenu))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Splash())
        assertNavigation(list = listOf(Screen.Splash()))
        getStoreFactory().getSplashStore(reset = false).back()
        assertNavigation(list = listOf(Screen.Splash()))
    }
}
