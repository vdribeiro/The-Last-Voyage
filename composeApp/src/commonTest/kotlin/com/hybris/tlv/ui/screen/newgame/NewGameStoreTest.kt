package com.hybris.tlv.ui.screen.newgame

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import com.hybris.tlv.TestCase
import com.hybris.tlv.engines
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.shipPrototype

internal class NewGameStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        TestCase.useCases.ship.syncEngines()
        val store = TestCase.storeFactory.getNewGameStore()
        assertNull(actual = store.selectedShip)
        assertNull(actual = store.selectedFormula)
        assertFalse(actual = store.state.loading)
        assertNotNull(actual = store.state.shipState)
        assertEquals(expected = engines.sortedBy { it.id }, actual = store.state.engines.sortedBy { it.id })
    }

    @Test
    fun initWithoutEngines() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.useCases.catastrophe.syncCatastrophes()
        TestCase.storeFactory.getNewGameStore()
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun selectShip() = TestCase.runUnitTest {
        TestCase.useCases.catastrophe.syncCatastrophes()
        TestCase.useCases.ship.syncEngines()
        val store = TestCase.storeFactory.getNewGameStore()
        assertNull(actual = store.selectedShip)
        store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        assertEquals(expected = shipPrototype, actual = store.selectedShip)
        val engine = engines.random()
        store.send(action = NewGameAction.SelectEngine(engine = engine))
        assertEquals(expected = engine, actual = store.state.shipState?.engine)
    }

    @Test
    fun startGame() = TestCase.runUnitTest {
        TestCase.useCases.ship.syncEngines()
        val store = TestCase.storeFactory.getNewGameStore()
        TestCase.assertNavigation(list = emptyList())
        store.send(action = NewGameAction.SelectEngine(engine = engines.random()))
        store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        TestCase.assertNavigation(list = listOf(Screen.Catastrophe))
    }

    @Test
    fun startGameWithoutShip() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        val store = TestCase.storeFactory.getNewGameStore()
        store.send(action = NewGameAction.SelectEngine(engine = engines.random()))
        store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.NewGame)
        TestCase.assertNavigation(list = listOf(Screen.NewGame))
        TestCase.useCases.catastrophe.syncCatastrophes()
        TestCase.useCases.ship.syncEngines()
        TestCase.storeFactory.getNewGameStore().back()
        TestCase.assertNavigation(list = listOf(Screen.NewGame, Screen.MainMenu))
    }
}
