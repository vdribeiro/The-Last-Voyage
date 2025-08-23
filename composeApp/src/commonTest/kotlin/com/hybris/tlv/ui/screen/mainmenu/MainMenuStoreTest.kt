package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class MainMenuStoreTest {

    private val mock = Mock()
    private val store
        get() = MainMenuStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = MainMenuState(),
            remoteConfig = mock.remoteConfig,
            gameSessionUseCases = mock.useCases.gameSession
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.MAIN_MENU)
    }

    @Test
    fun `init`() = runBlocking {
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
