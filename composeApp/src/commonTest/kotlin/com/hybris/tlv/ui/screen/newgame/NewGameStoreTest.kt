package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.catastrophes
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Math
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

internal class NewGameStoreTest {

    private val mock = Mock()
    private val store
        get() = NewGameStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = NewGameState(),
            earthUseCases = mock.useCases.earth,
            gameSessionUseCases = mock.useCases.gameSession
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.NEW_GAME)
    }

    @Test
    fun `init`() = runBlocking {
        mock.internalEarth.syncCatastrophes()
        val newGameStore = store
        assertEquals(actual = Content.SHIP, expected = newGameStore.stateFlow.value.currentContent)
        assertEquals(actual = catastrophes, expected = newGameStore.stateFlow.value.catastrophes)
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.internalEarth.syncCatastrophes()
        val newGameStore = store
        assertEquals(actual = NavigationManager.Screen.NEW_GAME, expected = mock.navigation.stateFlow.value.screen)
        assertEquals(actual = Content.SHIP, expected = newGameStore.stateFlow.value.currentContent)
        newGameStore.send(action = NewGameAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)

        newGameStore.send(action = NewGameAction.Ship)
        assertEquals(actual = Content.SHIP, expected = newGameStore.stateFlow.value.currentContent)
        newGameStore.send(action = NewGameAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)

        newGameStore.send(action = NewGameAction.Advanced)
        assertEquals(actual = Content.ADVANCED, expected = newGameStore.stateFlow.value.currentContent)
        newGameStore.send(action = NewGameAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)

        newGameStore.send(action = NewGameAction.Start)
        assertEquals(actual = Content.START, expected = newGameStore.stateFlow.value.currentContent)
        assertNotNull(actual = newGameStore.stateFlow.value.selectedCatastrophe)
        newGameStore.send(action = NewGameAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select ship`() = runBlocking {
        mock.internalEarth.syncCatastrophes()
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
    fun `send action select math`() = runBlocking {
        mock.internalEarth.syncCatastrophes()
        val newGameStore = store
        val math = Math()
        newGameStore.send(action = NewGameAction.SelectMath(math = math))
        assertEquals(expected = math, actual = newGameStore.stateFlow.value.math)
    }

    @Test
    fun `send action start game`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.NEW_GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.internalEarth.syncCatastrophes()
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
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action start game without selected ship`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.NEW_GAME, actual = mock.navigation.stateFlow.value.screen)
        val newGameStore = store
        newGameStore.send(action = NewGameAction.StartGame)
        assertEquals(expected = NavigationManager.Screen.ERROR, actual = mock.navigation.stateFlow.value.screen)
    }
}
