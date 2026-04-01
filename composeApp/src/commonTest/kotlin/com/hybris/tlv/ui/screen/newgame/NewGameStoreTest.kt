package com.hybris.tlv.ui.screen.newgame

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class NewGameStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.ship.syncEngines()
        val store = storeFactory.get().getNewGameStore()
        assertNull(actual = store.selectedFormula)
        assertNull(actual = store.selectedShip)
        assertFalse(actual = store.state.loading)
        assertEquals(expected = FakeData.engines.get().sortedBy { it.id }, actual = store.state.engines.sortedBy { it.id })
    }

    @Test
    fun initWithoutEngines() = runUnitTest {
        assertNavigation(list = emptyList())
        dependency.get().useCases.catastrophe.syncCatastrophes()
        storeFactory.get().getNewGameStore()
        assertNavigation(list = listOf(Screen.Feedback(tag = null, message = null)))
    }

    @Test
    fun selectShip() = runUnitTest {
        dependency.get().useCases.catastrophe.syncCatastrophes()
        dependency.get().useCases.ship.syncEngines()
        val store = storeFactory.get().getNewGameStore()
        assertNull(actual = store.selectedShip)
        store.send(action = NewGameAction.SelectShip)
        assertEquals(expected = FakeData.shipPrototype, actual = store.selectedShip)
    }

    @Test
    fun startGame() = runUnitTest {
        dependency.get().useCases.ship.syncEngines()
        val store = storeFactory.get().getNewGameStore()
        assertNavigation(list = emptyList())
        store.send(action = NewGameAction.SelectEngine(engine = FakeData.engines.get().random()))
        store.send(action = NewGameAction.SelectShip)
        assertNavigation(list = listOf(Screen.Catastrophe))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.NewGame)
        assertNavigation(list = listOf(Screen.NewGame))
        dependency.get().useCases.catastrophe.syncCatastrophes()
        dependency.get().useCases.ship.syncEngines()
        val store = storeFactory.get().getNewGameStore()
        store.send(action = NewGameAction.Back)
        assertNavigation(list = listOf(Screen.NewGame, Screen.MainMenu))
    }
}
