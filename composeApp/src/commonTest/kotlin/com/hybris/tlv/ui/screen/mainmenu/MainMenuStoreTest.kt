package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class MainMenuStoreTest {

    private val store
        get() = MainMenuStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = MainMenuState(),
            config = mock.config,
            learningUseCases = mock.useCases.learning,
            gameSessionUseCases = mock.useCases.gameSession
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.MAIN_MENU)
    }

    @Test
    fun `init`() = runBlocking {
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val mainMenuStore = store
        assertTrue(actual = mainMenuStore.stateFlow.value.ongoingGameSession)
        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)
    }

    @Test
    fun `init without game session`() = runBlocking {
        val mainMenuStore = store
        assertFalse(actual = mainMenuStore.stateFlow.value.ongoingGameSession)
    }

    @Test
    fun `send action change content`() = runBlocking {
        val mainMenuStore = store
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)
        mock.navigation.back()
        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

        mainMenuStore.send(action = MainMenuAction.Learn)
        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)
        mock.navigation.back()
        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

        mainMenuStore.send(action = MainMenuAction.HostDefinition)
        assertEquals(expected = Content.HOST_DEFINITION, actual = mainMenuStore.stateFlow.value.currentContent)
        mock.navigation.back()
        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

        mainMenuStore.send(action = MainMenuAction.PlanetDefinition)
        assertEquals(expected = Content.PLANET_DEFINITION, actual = mainMenuStore.stateFlow.value.currentContent)
        mock.navigation.back()
        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

        mainMenuStore.send(action = MainMenuAction.Habitability)
        assertEquals(expected = Content.HABITABILITY, actual = mainMenuStore.stateFlow.value.currentContent)
        mock.navigation.back()
        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

        mock.navigation.back()
        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

        mainMenuStore.send(action = MainMenuAction.NewGame)
        assertEquals(expected = NavigationManager.Screen.NEW_GAME, actual = mock.navigation.stateFlow.value.screen)

        mainMenuStore.send(action = MainMenuAction.Continue)
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)

        mainMenuStore.send(action = MainMenuAction.Scores)
        assertEquals(expected = NavigationManager.Screen.SCORE, actual = mock.navigation.stateFlow.value.screen)

        mainMenuStore.send(action = MainMenuAction.Achievements)
        assertEquals(expected = NavigationManager.Screen.ACHIEVEMENT, actual = mock.navigation.stateFlow.value.screen)

        mainMenuStore.send(action = MainMenuAction.Credits)
        assertEquals(expected = NavigationManager.Screen.CREDIT, actual = mock.navigation.stateFlow.value.screen)
    }
}
