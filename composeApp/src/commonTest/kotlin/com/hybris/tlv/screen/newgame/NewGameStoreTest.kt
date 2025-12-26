package com.hybris.tlv.screen.newgame

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import com.hybris.tlv.TestCase
import com.hybris.tlv.engines
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.shipPrototype

internal class NewGameStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.catastrophe.syncCatastrophes()
        useCases.ship.syncEngines()
        val store = storeFactory.getNewGameStore()
        assertEquals(expected = Content.SHIP, actual = store.state.currentContent)
    }

    @Test
    fun initWithoutEngines() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        useCases.catastrophe.syncCatastrophes()
        storeFactory.getNewGameStore()
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun initWithoutCatastrophes() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        useCases.ship.syncEngines()
        storeFactory.getNewGameStore()
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
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
        useCases.catastrophe.syncCatastrophes()
        useCases.ship.syncEngines()
        val store = storeFactory.getNewGameStore()
        store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        store.send(action = NewGameAction.SelectEngine(engine = engines.random()))
        assertNavigationBackstack(list = emptyList())
        store.send(action = NewGameAction.Next)
        store.send(action = NewGameAction.Next)
        assertNavigationBackstack(list = listOf(element = Screen.Game()))
    }

    @Test
    fun startGameWithoutShip() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        val store = storeFactory.getNewGameStore()
        store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        store.send(action = NewGameAction.SelectEngine(engine = engines.random()))
        store.send(action = NewGameAction.Next)
        store.send(action = NewGameAction.Next)
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        navigate(screen = Screen.NewGame)
        assertNavigationBackstack(list = listOf(element = Screen.NewGame))
        useCases.catastrophe.syncCatastrophes()
        useCases.ship.syncEngines()
        storeFactory.getNewGameStore().back()
        assertNavigationBackstack(list = listOf(Screen.NewGame, Screen.MainMenu))
    }
}
