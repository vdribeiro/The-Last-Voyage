package com.hybris.tlv.ui.screen.newgame

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class NewGameStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        getUseCases().ship.syncEngines()
        val store = getStoreFactory().getNewGameStore()
        assertNull(actual = store.selectedShip)
        assertNull(actual = store.selectedFormula)
        assertFalse(actual = store.state.loading)
        assertNotNull(actual = store.state.shipState)
        assertEquals(expected = FakeData.engines.get().sortedBy { it.id }, actual = store.state.engines.sortedBy { it.id })
    }

    @Test
    fun initWithoutEngines() = runUnitTest {
        assertNavigation(list = emptyList())
        getUseCases().catastrophe.syncCatastrophes()
        getStoreFactory().getNewGameStore()
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun selectShip() = runUnitTest {
        getUseCases().catastrophe.syncCatastrophes()
        getUseCases().ship.syncEngines()
        val store = getStoreFactory().getNewGameStore()
        assertNull(actual = store.selectedShip)
        store.send(action = NewGameAction.SelectShip(ship = FakeData.shipPrototype))
        assertEquals(expected = FakeData.shipPrototype, actual = store.selectedShip)
        val engine = FakeData.engines.get().random()
        store.send(action = NewGameAction.SelectEngine(engine = engine))
        assertEquals(expected = engine, actual = store.state.shipState?.engine)
    }

    @Test
    fun startGame() = runUnitTest {
        getUseCases().ship.syncEngines()
        val store = getStoreFactory().getNewGameStore()
        assertNavigation(list = emptyList())
        store.send(action = NewGameAction.SelectEngine(engine = FakeData.engines.get().random()))
        store.send(action = NewGameAction.SelectShip(ship = FakeData.shipPrototype))
        assertNavigation(list = listOf(Screen.Catastrophe))
    }

    @Test
    fun startGameWithoutShip() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = getStoreFactory().getNewGameStore()
        store.send(action = NewGameAction.SelectEngine(engine = FakeData.engines.get().random()))
        store.send(action = NewGameAction.SelectShip(ship = FakeData.shipPrototype))
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.NewGame)
        assertNavigation(list = listOf(Screen.NewGame))
        getUseCases().catastrophe.syncCatastrophes()
        getUseCases().ship.syncEngines()
        getStoreFactory().getNewGameStore().back()
        assertNavigation(list = listOf(Screen.NewGame, Screen.MainMenu))
    }
}
