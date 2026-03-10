package com.hybris.tlv.ui.screen.mainmenu

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class MainMenuStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = storeFactory.get().getMainMenuStore()
        assertFalse(actual = store.state.loading)
        assertFalse(actual = store.state.newVersionBanner)
        assertEquals(expected = dependency.get().config.localConfigs.developerCorner, actual = store.state.developerCorner)
        assertTrue(actual = store.state.ongoingGameSession)
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        val store = storeFactory.get().getMainMenuStore()
        assertFalse(actual = store.state.ongoingGameSession)
    }

    @Test
    fun newGame() = runUnitTest {
        val store = storeFactory.get().getMainMenuStore()
        store.send(action = MainMenuAction.NewGame)
        assertNavigation(list = listOf(Screen.Tutorial(newGame = false)))
    }

    @Test
    fun game() = runUnitTest {
        val store = storeFactory.get().getMainMenuStore()
        store.send(action = MainMenuAction.Game)
        assertNavigation(list = listOf(Screen.Game(ship = null)))
    }

    @Test
    fun scores() = runUnitTest {
        val store = storeFactory.get().getMainMenuStore()
        store.send(action = MainMenuAction.Scores)
        assertNavigation(list = listOf(Screen.Score))
    }

    @Test
    fun achievements() = runUnitTest {
        val store = storeFactory.get().getMainMenuStore()
        store.send(action = MainMenuAction.Achievements)
        assertNavigation(list = listOf(Screen.Achievement))
    }

    @Test
    fun credits() = runUnitTest {
        val store = storeFactory.get().getMainMenuStore()
        store.send(action = MainMenuAction.Credits)
        assertNavigation(list = listOf(Screen.Credit))
    }

    @Test
    fun stellarExplorer() = runUnitTest {
        val store = storeFactory.get().getMainMenuStore()
        store.send(action = MainMenuAction.StellarExplorer)
        assertNavigation(list = listOf(Screen.StellarExplorer))
    }
}
