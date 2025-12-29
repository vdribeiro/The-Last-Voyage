package com.hybris.tlv.screen.tutorial

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.TestCase
import com.hybris.tlv.navigation.Screen

internal class TutorialStoreTest: TestCase() {

    @Test
    fun completeTutorial() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Tutorial())
        assertNavigation(list = listOf(element = Screen.Tutorial()))

        val store = storeFactory.getTutorialStore(newGame = false)
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
        assertNavigation(list = emptyList())
    }

    @Test
    fun skipTutorial() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Tutorial())
        assertNavigation(list = listOf(element = Screen.Tutorial()))

        val store = storeFactory.getTutorialStore(newGame = false)
        store.send(action = TutorialAction.Skip)
        assertNavigation(list = emptyList())
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Tutorial())
        assertNavigation(list = listOf(element = Screen.Tutorial()))
        storeFactory.getTutorialStore(newGame = false).back()
        assertNavigation(list = emptyList())
    }
}
