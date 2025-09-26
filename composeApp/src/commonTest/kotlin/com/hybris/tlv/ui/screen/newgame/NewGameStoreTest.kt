package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

internal class NewGameStoreTest {

    private val store: NewGameStore get() = storeFactory.createNewGameStore()

    @BeforeTest
    fun setup() = runBlocking {
        testCore.sqlDriver.clearDatabase()
        testCore.navigation.navigate(screen = NavigationManager.Screen.NEW_GAME)
    }

    @Test
    fun `init`() = runBlocking {
        testCore.useCases.catastrophe.prepopulateCatastrophes()
        val newGameStore = store
        assertEquals(expected = Content.SHIP, actual = newGameStore.stateFlow.value.currentContent)
    }

    @Test
    fun `send action back`() = runBlocking {
        testCore.useCases.catastrophe.prepopulateCatastrophes()
        val newGameStore = store
        assertEquals(expected = NavigationManager.Screen.NEW_GAME, actual = testCore.navigation.stateFlow.value.screen)
        assertEquals(expected = Content.SHIP, actual = newGameStore.stateFlow.value.currentContent)
        testCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = testCore.navigation.stateFlow.value.screen)

        newGameStore.send(action = NewGameAction.Ship)
        assertEquals(expected = Content.SHIP, actual = newGameStore.stateFlow.value.currentContent)
        testCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = testCore.navigation.stateFlow.value.screen)

        newGameStore.send(action = NewGameAction.Advanced)
        assertEquals(expected = Content.ADVANCED, actual = newGameStore.stateFlow.value.currentContent)
        testCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = testCore.navigation.stateFlow.value.screen)

        newGameStore.send(action = NewGameAction.Start)
        assertEquals(expected = Content.START, actual = newGameStore.stateFlow.value.currentContent)
        assertNotNull(actual = newGameStore.stateFlow.value.selectedCatastrophe)
        testCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = testCore.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select ship`() = runBlocking {
        testCore.useCases.catastrophe.prepopulateCatastrophes()
        val newGameStore = store
        assertNull(actual = newGameStore.stateFlow.value.selectedShip)
        val shipPrototype = ShipPrototype(
            assignedPoints = 1,
            sensorRange = 1,
            materials = 1,
            fuel = 1,
            cryopods = 1
        )
        newGameStore.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        assertEquals(expected = shipPrototype, actual = newGameStore.stateFlow.value.selectedShip)
    }

    @Test
    fun `send action select formula`() = runBlocking {
        testCore.useCases.catastrophe.prepopulateCatastrophes()
        val newGameStore = store
        val formula = Formula()
        newGameStore.send(action = NewGameAction.SelectFormula(formula = formula))
        assertEquals(expected = formula, actual = newGameStore.stateFlow.value.formula)
    }

    @Test
    fun `send action start game`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.NEW_GAME, actual = testCore.navigation.stateFlow.value.screen)
        testCore.useCases.catastrophe.prepopulateCatastrophes()
        val newGameStore = store
        val shipPrototype = ShipPrototype(
            assignedPoints = 1,
            sensorRange = 1,
            materials = 1,
            fuel = 1,
            cryopods = 1
        )
        newGameStore.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        newGameStore.send(action = NewGameAction.StartGame)
        assertEquals(expected = NavigationManager.Screen.GAME, actual = testCore.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action start game without selected ship`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.NEW_GAME, actual = testCore.navigation.stateFlow.value.screen)
        val newGameStore = store
        newGameStore.send(action = NewGameAction.StartGame)
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = testCore.navigation.stateFlow.value.screen)
    }
}
