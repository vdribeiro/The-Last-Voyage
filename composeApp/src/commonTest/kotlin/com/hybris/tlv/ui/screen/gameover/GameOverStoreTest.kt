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
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Route

internal class GameOverStoreTest {

    private val store: GameOverStore get() = getStoreFactory().createGameOverStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        getNavigation().navigate(navigationState = NavigationState(route = Route.GameOver))
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
        assertEquals(expected = Route.GameOver, actual = getNavigation().stateFlow.value.route)
        val gameOverStore = store
        assertNull(actual = gameOverStore.stateFlow.value.gameSession)
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action back`() = runBlocking {
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store
        assertEquals(expected = Route.GameOver, actual = getNavigation().stateFlow.value.route)
        getNavigation().back()
        assertEquals(expected = Route.GameOver, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action continue`() = runBlocking {
        assertEquals(expected = Route.GameOver, actual = getNavigation().stateFlow.value.route)
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameOverStore = store
        assertEquals(expected = Content.MESSAGE, actual = gameOverStore.stateFlow.value.currentContent)
        gameOverStore.send(action = GameOverAction.Next)
        assertEquals(expected = Content.SCORE, actual = gameOverStore.stateFlow.value.currentContent)
        gameOverStore.send(action = GameOverAction.Next)
        assertEquals(expected = Route.MainMenu, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action continue without game session`() = runBlocking {
        val gameOverStore = store
        gameOverStore.send(action = GameOverAction.Next)
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)
    }
}
