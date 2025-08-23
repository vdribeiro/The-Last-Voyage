package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class GameOverStoreTest {

    private val mock = Mock()
    private val store
        get() = GameOverStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = GameOverState(),
            locale = mock.locale,
            gameSessionUseCases = mock.useCases.gameSession
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.GAME_OVER)
    }

    @Test
    fun `init`() = runBlocking {
        store
    }

    @Test
    fun `send action back`() = runBlocking {
        val gameOverStore = store
        assertEquals(actual = NavigationManager.Screen.GAME_OVER, expected = mock.navigation.stateFlow.value.screen)
        gameOverStore.send(action = GameOverAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
