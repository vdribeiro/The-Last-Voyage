package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

internal class GameOverStoreTest {

    private val store: GameOverStore get() = storeFactory.createGameOverStore()

    @BeforeTest
    fun setup() = runBlocking {
        testCore.sqlDriver.clearDatabase()
        testCore.navigation.navigate(screen = NavigationManager.Screen.GAME_OVER)
    }

    @Test
    fun `init`() = runBlocking {
        testCore.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameOverStore = store
        assertNotNull(actual = gameOverStore.stateFlow.value.gameSession)
        assertNotNull(actual = gameOverStore.stateFlow.value.gameOver)
        assertEquals(expected = Content.MESSAGE, actual = gameOverStore.stateFlow.value.currentContent)
    }

    @Test
    fun `init without game session`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = testCore.navigation.stateFlow.value.screen)
        val gameOverStore = store
        assertNull(actual = gameOverStore.stateFlow.value.gameSession)
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = testCore.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action back`() = runBlocking {
        testCore.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = testCore.navigation.stateFlow.value.screen)
        testCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = testCore.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action continue`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = testCore.navigation.stateFlow.value.screen)
        testCore.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameOverStore = store
        assertEquals(expected = Content.MESSAGE, actual = gameOverStore.stateFlow.value.currentContent)
        gameOverStore.send(action = GameOverAction.Continue)
        assertEquals(expected = Content.SCORE, actual = gameOverStore.stateFlow.value.currentContent)
        gameOverStore.send(action = GameOverAction.Continue)
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = testCore.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action continue without game session`() = runBlocking {
        val gameOverStore = store
        gameOverStore.send(action = GameOverAction.Continue)
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = testCore.navigation.stateFlow.value.screen)
    }
}
