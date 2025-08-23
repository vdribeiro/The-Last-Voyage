package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class NewGameStoreTest {

    private val mock = Mock()
    private val store
        get() = NewGameStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = NewGameState(),
            earthUseCases = mock.useCases.earth,
            gameSessionUseCases = mock.useCases.gameSession
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.NEW_GAME)
    }

    @Test
    fun `init`() = runBlocking {
    }

    @Test
    fun `send action back`() = runBlocking {
        val newGameStore = store
        assertEquals(actual = NavigationManager.Screen.NEW_GAME, expected = mock.navigation.stateFlow.value.screen)
        newGameStore.send(action = NewGameAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
