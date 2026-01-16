package com.hybris.tlv.ui.screen.tutorial

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import com.hybris.tlv.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class TutorialStoreTest: TestCase() {

    @Test
    fun completeTutorial() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Tutorial())
        TestCase.assertNavigation(list = listOf(Screen.Tutorial()))

        val store = TestCase.storeFactory.getTutorialStore(newGame = false)
        assertNotNull(actual = store.state.ship)
        assertEquals(expected = Content.WELCOME, actual = store.state.currentContent)
        store.send(action = TutorialAction.Next)
        assertEquals(expected = Content.GOAL, actual = store.state.currentContent)
        store.send(action = TutorialAction.Next)
        assertEquals(expected = Content.SHIP, actual = store.state.currentContent)
        store.send(action = TutorialAction.Next)
        assertEquals(expected = Content.TRAVEL, actual = store.state.currentContent)
        store.send(action = TutorialAction.Next)
        assertEquals(expected = Content.SYSTEM, actual = store.state.currentContent)
        store.send(action = TutorialAction.Next)
        assertEquals(expected = Content.GAME_OVER, actual = store.state.currentContent)
        store.send(action = TutorialAction.Next)
        assertEquals(expected = Content.GAME_OVER, actual = store.state.currentContent)
        TestCase.assertNavigation(list = emptyList())
    }

    @Test
    fun skipTutorial() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Tutorial())
        TestCase.assertNavigation(list = listOf(Screen.Tutorial()))

        val store = TestCase.storeFactory.getTutorialStore(newGame = false)
        store.send(action = TutorialAction.Skip)
        TestCase.assertNavigation(list = emptyList())
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Tutorial())
        TestCase.assertNavigation(list = listOf(Screen.Tutorial()))
        TestCase.storeFactory.getTutorialStore(newGame = false).back()
        TestCase.assertNavigation(list = emptyList())
    }
}
