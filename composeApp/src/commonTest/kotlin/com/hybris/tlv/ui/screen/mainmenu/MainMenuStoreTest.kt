package com.hybris.tlv.ui.screen.mainmenu

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.gameSessionPrototype
import com.hybris.tlv.ui.navigation.Screen

internal class MainMenuStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getMainMenuStore()
        assertFalse(actual = store.state.loading)
        assertFalse(actual = store.state.newVersionBanner)
        assertEquals(expected = config.localConfigs.value.developerCorner, actual = store.state.developerCorner)
        assertTrue(actual = store.state.ongoingGameSession)
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        val store = storeFactory.getMainMenuStore()
        assertFalse(actual = store.state.ongoingGameSession)
    }

    @Test
    fun newGame() = runUnitTest {
        val store = storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.NewGame)
        assertNavigation(list = listOf(Screen.Tutorial()))
    }

    @Test
    fun game() = runUnitTest {
        val store = storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.Game)
        assertNavigation(list = listOf(Screen.Game()))
    }

    @Test
    fun scores() = runUnitTest {
        val store = storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.Scores)
        assertNavigation(list = listOf(Screen.Score))
    }

    @Test
    fun achievements() = runUnitTest {
        val store = storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.Achievements)
        assertNavigation(list = listOf(Screen.Achievement))
    }

    @Test
    fun credits() = runUnitTest {
        val store = storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.Credits)
        assertNavigation(list = listOf(Screen.Credit))
    }

    @Test
    fun stellarExplorer() = runUnitTest {
        val store = storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.StellarExplorer)
        assertNavigation(list = listOf(Screen.StellarExplorer))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.MainMenu)
        assertNavigation(list = listOf(Screen.MainMenu))
        storeFactory.getMainMenuStore().back()
        assertNavigation(list = listOf(Screen.MainMenu))
    }
}
