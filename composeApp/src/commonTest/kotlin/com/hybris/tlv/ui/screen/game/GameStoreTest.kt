package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.games
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class GameStoreTest {

    private val mock = Mock()
    private val store
        get() = GameStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = GameState(),
            gameUseCases = mock.useCases.game
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.GAME)
    }

    @Test
    fun `init`() = runBlocking {
        val gameStore = store
        assertEquals(actual = games, expected = gameStore.stateFlow.value.games)
    }

    @Test
    fun `send action back`() = runBlocking {
        val gameStore = store
        assertEquals(actual = NavigationManager.Screen.GAME, expected = mock.navigation.stateFlow.value.screen)
        gameStore.send(action = GameAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
