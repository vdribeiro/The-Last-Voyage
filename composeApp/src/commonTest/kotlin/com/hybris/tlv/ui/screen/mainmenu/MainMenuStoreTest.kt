package com.hybris.tlv.ui.screen.mainmenu

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen

internal class MainMenuStoreTest {

    private val store: MainMenuStore get() = storeFactory.createMainMenuStore()

    @BeforeTest
    fun setup() = runBlocking {
        testDependency.sqlDriver.clearDatabase()
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Splash))
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.MainMenu))
    }

    @Test
    fun `init`() = runBlocking {
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.MainMenu))
        val mainMenuStore = store
        delay(timeMillis = 100L)
        assertTrue(actual = mainMenuStore.stateFlow.value.ongoingGameSession)
//        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)
    }

    @Test
    fun `init without game session`() = runBlocking {
        val mainMenuStore = store
        assertFalse(actual = mainMenuStore.stateFlow.value.ongoingGameSession)
    }

    @Test
    fun `send action change content`() = runBlocking {
        val mainMenuStore = store
        assertEquals(expected = Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)
//        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)
        testDependency.navigation.back()
//        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

//        mainMenuStore.send(action = MainMenuAction.Learn)
//        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)
//        testDependency.navigation.back()
//        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

//        mainMenuStore.send(action = MainMenuAction.HostDefinition)
//        assertEquals(expected = Content.HOST_DEFINITION, actual = mainMenuStore.stateFlow.value.currentContent)
//        testDependency.navigation.back()
//        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

//        mainMenuStore.send(action = MainMenuAction.PlanetDefinition)
//        assertEquals(expected = Content.PLANET_DEFINITION, actual = mainMenuStore.stateFlow.value.currentContent)
//        testDependency.navigation.back()
//        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

//        mainMenuStore.send(action = MainMenuAction.Habitability)
//        assertEquals(expected = Content.HABITABILITY, actual = mainMenuStore.stateFlow.value.currentContent)
//        testDependency.navigation.back()
//        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

        testDependency.navigation.back()
//        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

        mainMenuStore.send(action = MainMenuAction.NewGame)
        assertEquals(expected = Screen.NewGame, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()

        mainMenuStore.send(action = MainMenuAction.Next)
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()

        mainMenuStore.send(action = MainMenuAction.Scores)
        assertEquals(expected = Screen.Score, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()

        mainMenuStore.send(action = MainMenuAction.Achievements)
        assertEquals(expected = Screen.Achievement, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()

        mainMenuStore.send(action = MainMenuAction.Credits)
        assertEquals(expected = Screen.Credit, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
