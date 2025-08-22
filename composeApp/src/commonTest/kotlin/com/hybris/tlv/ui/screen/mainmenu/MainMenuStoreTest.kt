package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.mainMenus
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
            mainMenuUseCases = mock.useCases.mainMenu
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.MAIN_MENU)
    }

    @Test
    fun `init`() = runBlocking {
        val mainMenuStore = store
        assertEquals(actual = mainMenus, expected = mainMenuStore.stateFlow.value.mainMenus)
    }

    @Test
    fun `send action back`() = runBlocking {
        val mainMenuStore = store
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
        mainMenuStore.send(action = MainMenuAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
