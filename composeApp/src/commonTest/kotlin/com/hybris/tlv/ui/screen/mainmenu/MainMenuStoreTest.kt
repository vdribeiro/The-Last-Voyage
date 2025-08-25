package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.gameSessionPrototype
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
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
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val mainMenuStore = store
        assertTrue(actual = mainMenuStore.stateFlow.value.ongoingGameSession)
    }

    @Test
    fun `init without game session`() = runBlocking {
        val mainMenuStore = store
        assertFalse(actual = mainMenuStore.stateFlow.value.ongoingGameSession)
    }

    @Test
    fun `send action change content`() = runBlocking {
        val gameStore = store
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)

        gameStore.send(action = MainMenuAction.NewGame)
        assertEquals(expected = NavigationManager.Screen.NEW_GAME, actual = mock.navigation.stateFlow.value.screen)

        gameStore.send(action = MainMenuAction.Continue)
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)

        gameStore.send(action = MainMenuAction.Learn)
        assertEquals(expected = NavigationManager.Screen.LEARN, actual = mock.navigation.stateFlow.value.screen)

        gameStore.send(action = MainMenuAction.Scores)
        assertEquals(expected = NavigationManager.Screen.SCORE, actual = mock.navigation.stateFlow.value.screen)

        gameStore.send(action = MainMenuAction.Achievements)
        assertEquals(expected = NavigationManager.Screen.ACHIEVEMENT, actual = mock.navigation.stateFlow.value.screen)

        gameStore.send(action = MainMenuAction.Credits)
        assertEquals(expected = NavigationManager.Screen.CREDIT, actual = mock.navigation.stateFlow.value.screen)
    }
}
