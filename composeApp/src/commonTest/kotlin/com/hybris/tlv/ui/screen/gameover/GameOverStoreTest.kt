package com.hybris.tlv.ui.screen.gameover

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.getNavigation
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset
import com.hybris.tlv.ui.navigation.Screen

internal class GameOverStoreTest {

    private val store: GameOverStore get() = getStoreFactory().createGameOverStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        getNavigation().navigate(navigationState = NavigationState(screen = Screen.GameOver))
    }

    @Test
    fun `init`() = runBlocking {
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameOverStore = store
        assertNotNull(actual = gameOverStore.stateFlow.value.gameSession)
        assertNotNull(actual = gameOverStore.stateFlow.value.gameOver)
        assertEquals(expected = Content.MESSAGE, actual = gameOverStore.stateFlow.value.currentContent)
    }

    @Test
    fun `init without game session`() = runBlocking {
        assertEquals(expected = Screen.GameOver, actual = getNavigation().stateFlow.value.screen)
        val gameOverStore = store
        assertNull(actual = gameOverStore.stateFlow.value.gameSession)
        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)
    }

    @Test
    fun `send action back`() = runBlocking {
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store
        assertEquals(expected = Screen.GameOver, actual = getNavigation().stateFlow.value.screen)
        getNavigation().back()
        assertEquals(expected = Screen.GameOver, actual = getNavigation().stateFlow.value.screen)
    }

    @Test
    fun `send action continue`() = runBlocking {
        assertEquals(expected = Screen.GameOver, actual = getNavigation().stateFlow.value.screen)
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameOverStore = store
        assertEquals(expected = Content.MESSAGE, actual = gameOverStore.stateFlow.value.currentContent)
        gameOverStore.send(action = GameOverAction.Next)
        assertEquals(expected = Content.SCORE, actual = gameOverStore.stateFlow.value.currentContent)
        gameOverStore.send(action = GameOverAction.Next)
        assertEquals(expected = Screen.MainMenu, actual = getNavigation().stateFlow.value.screen)
    }

    @Test
    fun `send action continue without game session`() = runBlocking {
        val gameOverStore = store
        gameOverStore.send(action = GameOverAction.Next)
        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)
    }
}
