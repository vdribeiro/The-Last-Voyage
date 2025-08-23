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
    }

    @Test
    fun `send action back`() = runBlocking {
    }
}
