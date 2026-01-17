package com.hybris.tlv.screen.newgame

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
    fun init() = runUnitTest {
        useCases.ship.syncEngines()
        val store = storeFactory.getNewGameStore()
        assertNull(actual = store.selectedShip)
        assertNull(actual = store.selectedFormula)
        assertFalse(actual = store.state.loading)
        assertNotNull(actual = store.state.shipState)
        assertEquals(expected = engines.sortedBy { it.id }, actual = store.state.engines.sortedBy { it.id })
    }

    @Test
    fun initWithoutEngines() = runUnitTest {
        assertNavigation(list = emptyList())
        useCases.catastrophe.syncCatastrophes()
        storeFactory.getNewGameStore()
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun selectShip() = runUnitTest {
        useCases.catastrophe.syncCatastrophes()
        useCases.ship.syncEngines()
        val store = storeFactory.getNewGameStore()
        assertNull(actual = store.selectedShip)
        store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        assertEquals(expected = shipPrototype, actual = store.selectedShip)
        val engine = engines.random()
        store.send(action = NewGameAction.SelectEngine(engine = engine))
        assertEquals(expected = engine, actual = store.state.shipState?.engine)
    }

    @Test
    fun startGame() = runUnitTest {
        useCases.ship.syncEngines()
        val store = storeFactory.getNewGameStore()
        assertNavigation(list = emptyList())
        store.send(action = NewGameAction.SelectEngine(engine = engines.random()))
        store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        assertNavigation(list = listOf(Screen.Catastrophe))
    }

    @Test
    fun startGameWithoutShip() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = storeFactory.getNewGameStore()
        store.send(action = NewGameAction.SelectEngine(engine = engines.random()))
        store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.NewGame)
        assertNavigation(list = listOf(Screen.NewGame))
        useCases.catastrophe.syncCatastrophes()
        useCases.ship.syncEngines()
        storeFactory.getNewGameStore().back()
        assertNavigation(list = listOf(Screen.NewGame, Screen.MainMenu))
    }
}
