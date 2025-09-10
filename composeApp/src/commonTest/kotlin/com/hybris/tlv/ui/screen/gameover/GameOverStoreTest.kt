package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

internal class GameOverStoreTest {

    private val store by lazy {
        GameOverStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = GameOverState(),
            gameSessionUseCases = mock.useCases.gameSession
        )
    }

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.GAME_OVER)
    }

    @Test
    fun `init`() = runBlocking {
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameOverStore = store.apply { setup(state = GameOverState()) }
        assertNotNull(actual = gameOverStore.stateFlow.value.gameSession)
        assertNotNull(actual = gameOverStore.stateFlow.value.gameOver)
        assertEquals(expected = Content.MESSAGE, actual = gameOverStore.stateFlow.value.currentContent)
    }

    @Test
    fun `init without game session`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)
        val gameOverStore = store.apply { setup(state = GameOverState()) }
        assertNull(actual = gameOverStore.stateFlow.value.gameSession)
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store.setup(state = GameOverState())
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action continue`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameOverStore = store.apply { setup(state = GameOverState()) }
        assertEquals(expected = Content.MESSAGE, actual = gameOverStore.stateFlow.value.currentContent)
        gameOverStore.send(action = GameOverAction.Continue)
        assertEquals(expected = Content.SCORE, actual = gameOverStore.stateFlow.value.currentContent)
        gameOverStore.send(action = GameOverAction.Continue)
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action continue without game session`() = runBlocking {
        val gameOverStore = store.apply { setup(state = GameOverState()) }
        gameOverStore.send(action = GameOverAction.Continue)
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mock.navigation.stateFlow.value.screen)
    }
}
