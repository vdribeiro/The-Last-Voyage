package com.hybris.tlv.ui.screen.mainmenu

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.getMainMenuStore
import com.hybris.tlv.reset
import com.hybris.tlv.useCases

internal class MainMenuStoreTest {

    private val store: MainMenuStore get() = getMainMenuStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = SplashScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = MainMenuScreen))
    }

    @Test
    fun `init`() = runBlocking {
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        getNavigation().navigate(navigationState = NavigationState(screen = MainMenuScreen))
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
        store
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
//        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)
//        getNavigation().back()
//        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

//        mainMenuStore.send(action = MainMenuAction.Learn)
//        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)
//        getNavigation().back()
//        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

//        mainMenuStore.send(action = MainMenuAction.HostDefinition)
//        assertEquals(expected = Content.HOST_DEFINITION, actual = mainMenuStore.stateFlow.value.currentContent)
//        getNavigation().back()
//        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

//        mainMenuStore.send(action = MainMenuAction.PlanetDefinition)
//        assertEquals(expected = Content.PLANET_DEFINITION, actual = mainMenuStore.stateFlow.value.currentContent)
//        getNavigation().back()
//        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

//        mainMenuStore.send(action = MainMenuAction.Habitability)
//        assertEquals(expected = Content.HABITABILITY, actual = mainMenuStore.stateFlow.value.currentContent)
//        getNavigation().back()
//        assertEquals(expected = Content.LEARN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

//        getNavigation().back()
//        assertEquals(expected = Content.MAIN_MENU, actual = mainMenuStore.stateFlow.value.currentContent)

//        mainMenuStore.send(action = MainMenuAction.NewGame)
//        assertEquals(expected = NewGameScreen, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//
//        mainMenuStore.send(action = MainMenuAction.Next)
//        assertEquals(expected = GameScreen, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//
//        mainMenuStore.send(action = MainMenuAction.Scores)
//        assertEquals(expected = ScoreScreen, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//
//        mainMenuStore.send(action = MainMenuAction.Achievements)
//        assertEquals(expected = AchievementScreen, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//
//        mainMenuStore.send(action = MainMenuAction.Credits)
//        assertEquals(expected = CreditScreen, actual = getNavigation().stateFlow.value.screen)
    }
}
