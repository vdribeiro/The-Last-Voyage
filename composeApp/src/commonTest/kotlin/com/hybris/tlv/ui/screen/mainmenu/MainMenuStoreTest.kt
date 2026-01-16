package com.hybris.tlv.ui.screen.mainmenu

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.ui.navigation.Screen

internal class MainMenuStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getMainMenuStore()
        assertFalse(actual = store.state.loading)
        assertFalse(actual = store.state.newVersionBanner)
        assertEquals(expected = TestCase.config.localConfigs.value.developerCorner, actual = store.state.developerCorner)
        assertTrue(actual = store.state.ongoingGameSession)
    }

    @Test
    fun initWithoutGameSession() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getMainMenuStore()
        assertFalse(actual = store.state.ongoingGameSession)
    }

    @Test
    fun newGame() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.NewGame)
        TestCase.assertNavigation(list = listOf(Screen.Tutorial()))
    }

    @Test
    fun game() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.Game)
        TestCase.assertNavigation(list = listOf(Screen.Game()))
    }

    @Test
    fun scores() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.Scores)
        TestCase.assertNavigation(list = listOf(Screen.Score))
    }

    @Test
    fun achievements() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.Achievements)
        TestCase.assertNavigation(list = listOf(Screen.Achievement))
    }

    @Test
    fun credits() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.Credits)
        TestCase.assertNavigation(list = listOf(Screen.Credit))
    }

    @Test
    fun stellarExplorer() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getMainMenuStore()
        store.send(action = MainMenuAction.StellarExplorer)
        TestCase.assertNavigation(list = listOf(Screen.StellarExplorer))
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.MainMenu)
        TestCase.assertNavigation(list = listOf(Screen.MainMenu))
        TestCase.storeFactory.getMainMenuStore().back()
        TestCase.assertNavigation(list = listOf(Screen.MainMenu))
    }
}
