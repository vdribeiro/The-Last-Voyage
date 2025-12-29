package com.hybris.tlv.screen.splash

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.TestCase
import com.hybris.tlv.navigation.Screen

internal class SplashStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        val store = storeFactory.getSplashStore(reset = true)
        store.setupJob?.join()
        assertEquals(expected = 1f, actual = store.state.progress)
        assertEquals(expected = Content.INTRO, actual = store.state.currentContent)
    }

    @Test
    fun next() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = storeFactory.getSplashStore(reset = true)
        store.setupJob?.join()
        store.send(action = SplashAction.Next)
        assertNavigation(list = listOf(element = Screen.MainMenu))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Splash())
        assertNavigation(list = listOf(element = Screen.Splash()))
        storeFactory.getSplashStore(reset = false).back()
        assertNavigation(list = listOf(element = Screen.Splash()))
    }
}
