package com.hybris.tlv.ui.screen.tutorial

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class TutorialStoreTest: TestCase() {

    @Test
    fun completeTutorial() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Tutorial(newGame = false))
        assertNavigation(list = listOf(Screen.Tutorial(newGame = false)))

        val store = storeFactory.get().getTutorialStore(newGame = false)
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
        assertNavigation(list = emptyList())
    }

    @Test
    fun skipTutorial() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Tutorial(newGame = false))
        assertNavigation(list = listOf(Screen.Tutorial(newGame = false)))

        val store = storeFactory.get().getTutorialStore(newGame = false)
        store.send(action = TutorialAction.Skip)
        assertNavigation(list = emptyList())
    }
}
