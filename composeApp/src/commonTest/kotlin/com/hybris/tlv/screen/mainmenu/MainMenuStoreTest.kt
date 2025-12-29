package com.hybris.tlv.screen.mainmenu

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.navigation.Screen

internal class MainMenuStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getMainMenuStore()
        assertTrue(actual = store.state.ongoingGameSession)
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        val store = storeFactory.getMainMenuStore()
        assertFalse(actual = store.state.ongoingGameSession)
    }

    @Test
    fun navigate() = runUnitTest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getMainMenuStore()

        store.send(action = MainMenuAction.NewGame)
        assertNavigation(list = listOf(element = Screen.Tutorial()))

//        clearNavigation()
//        store.send(action = MainMenuAction.NewGame)
//        assertNavigation(list = listOf(element = Screen.Game()))
//        store.send(action = MainMenuAction.Scores)
//        assertNavigationBackstack(list = listOf(Screen.MainMenu, Screen.Tutorial(), Screen.Game(), Screen.Score))
//        store.send(action = MainMenuAction.Achievements)
//        assertNavigationBackstack(list = listOf(Screen.MainMenu, Screen.Tutorial(), Screen.Game(), Screen.Score, Screen.Achievement))
//        store.send(action = MainMenuAction.Credits)
//        assertNavigationBackstack(list = listOf(Screen.MainMenu, Screen.Tutorial(), Screen.Game(), Screen.Score, Screen.Achievement, Screen.Credit))
//        store.send(action = MainMenuAction.StellarExplorer)
//        assertNavigationBackstack(list = listOf(Screen.MainMenu, Screen.Tutorial(), Screen.Game(), Screen.Score, Screen.Achievement, Screen.Credit, Screen.StellarExplorer))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.MainMenu)
        assertNavigation(list = listOf(element = Screen.MainMenu))
        storeFactory.getMainMenuStore().back()
        assertNavigation(list = listOf(element = Screen.MainMenu))
    }
}
