package com.hybris.tlv.ui.screen.splash

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import com.hybris.tlv.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class SplashStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getSplashStore(reset = true)
        assertNotNull(actual = store.setupJob)
        store.setupJob?.join()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = 1f, actual = store.state.progress)
        assertEquals(expected = Content.INTRO, actual = store.state.currentContent)
    }

    @Test
    fun next() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        val store = TestCase.storeFactory.getSplashStore(reset = true)
        store.setupJob?.join()
        store.send(action = SplashAction.Next)
        TestCase.assertNavigation(list = listOf(Screen.MainMenu))
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Splash())
        TestCase.assertNavigation(list = listOf(Screen.Splash()))
        TestCase.storeFactory.getSplashStore(reset = false).back()
        TestCase.assertNavigation(list = listOf(Screen.Splash()))
    }
}
