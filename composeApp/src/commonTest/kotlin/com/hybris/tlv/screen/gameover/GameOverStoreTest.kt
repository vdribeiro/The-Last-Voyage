package com.hybris.tlv.screen.gameover

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.navigation.Screen

internal class GameOverStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameOverStore()
        assertNotNull(actual = store.state.gameSession)
        assertNotNull(actual = store.state.gameOver)
        assertEquals(expected = Content.MESSAGE, actual = store.state.currentContent)
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        storeFactory.getGameOverStore()
        assertNavigation(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun nextContent() = runUnitTest {
        assertNavigation(list = emptyList())
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameOverStore()
        assertEquals(expected = Content.MESSAGE, actual = store.state.currentContent)
        store.send(action = GameOverAction.Next)
        assertEquals(expected = Content.SCORE, actual = store.state.currentContent)
        store.send(action = GameOverAction.Next)
        assertNavigation(list = listOf(element = Screen.MainMenu))
    }

    @Test
    fun nextContentWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = storeFactory.getGameOverStore()
        store.send(action = GameOverAction.Next)
        assertNavigation(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.GameOver)
        assertNavigation(list = listOf(element = Screen.GameOver))
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        storeFactory.getGameOverStore().back()
        assertNavigation(list = listOf(element = Screen.GameOver))
    }
}
