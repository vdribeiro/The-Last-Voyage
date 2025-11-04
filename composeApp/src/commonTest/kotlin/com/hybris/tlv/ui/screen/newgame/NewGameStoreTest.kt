package com.hybris.tlv.ui.screen.newgame

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.engines
import com.hybris.tlv.reset
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.usecase.ship.model.ShipPrototype

internal class NewGameStoreTest {

    private val store: NewGameStore get() = testDependency.storeFactory.createNewGameStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.NewGame))
    }

    @Test
    fun `init`() = runBlocking {
        testDependency.useCases.catastrophe.syncCatastrophes()
        testDependency.useCases.ship.syncEngines()
        val newGameStore = store
        assertEquals(expected = Content.SHIP, actual = newGameStore.stateFlow.value.currentContent)
    }

    @Test
    fun `send action back`() = runBlocking {
        testDependency.useCases.catastrophe.syncCatastrophes()
        testDependency.useCases.ship.syncEngines()
        val newGameStore = store
        assertEquals(expected = Screen.NewGame, actual = testDependency.navigation.stateFlow.value.screen)
        assertEquals(expected = Content.SHIP, actual = newGameStore.stateFlow.value.currentContent)
        testDependency.navigation.back()
        assertEquals(expected = Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)

        newGameStore.send(action = NewGameAction.Next)
        assertEquals(expected = Content.SHIP, actual = newGameStore.stateFlow.value.currentContent)
        testDependency.navigation.back()
        assertEquals(expected = Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)

        newGameStore.send(action = NewGameAction.Next)
        assertEquals(expected = Content.START, actual = newGameStore.stateFlow.value.currentContent)
        assertNotNull(actual = newGameStore.stateFlow.value.selectedCatastrophe)
        testDependency.navigation.back()
        assertEquals(expected = Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select ship`() = runBlocking {
        testDependency.useCases.catastrophe.syncCatastrophes()
        testDependency.useCases.ship.syncEngines()
        val newGameStore = store
        assertNull(actual = newGameStore.selectedShip)
        val shipPrototype = ShipPrototype(
            assignedPoints = 1,
            sensorRange = 1,
            materials = 1,
            fuel = 1,
            cryopods = 1
        )
        newGameStore.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        assertEquals(expected = shipPrototype, actual = newGameStore.selectedShip)
    }

    @Test
    fun `send action start game`() = runBlocking {
        assertEquals(expected = Screen.NewGame, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.useCases.catastrophe.syncCatastrophes()
        testDependency.useCases.ship.syncEngines()
        val newGameStore = store
        val shipPrototype = ShipPrototype(
            assignedPoints = 1,
            sensorRange = 1,
            materials = 1,
            fuel = 1,
            cryopods = 1
        )
        newGameStore.send(action = NewGameAction.SelectShip(ship = shipPrototype))
        newGameStore.send(action = NewGameAction.SelectEngine(engine = engines.first()))
        newGameStore.send(action = NewGameAction.Next)
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action start game without selected ship`() = runBlocking {
        assertEquals(expected = Screen.NewGame, actual = testDependency.navigation.stateFlow.value.screen)
        val newGameStore = store
        newGameStore.send(action = NewGameAction.Next)
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
