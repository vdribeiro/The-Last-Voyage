package com.hybris.tlv.screen.newgame

import com.hybris.tlv.TestCase

// TODO
internal class NewGameStoreTest: TestCase() {

//    private val store: NewGameStore get() = getNewGameStore()
//
//    @BeforeTest
//    fun setup() = runBlocking {
//        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = NewGameScreen))
//    }
//
//    @Test
//    fun `init`() = runBlocking {
//        useCases.catastrophe.syncCatastrophes()
//        useCases.ship.syncEngines()
//        val newGameStore = store
//        assertEquals(expected = Content.SHIP, actual = newGameStore.stateFlow.value.currentContent)
//    }
//
//    @Test
//    fun `send action back`() = runBlocking {
//        useCases.catastrophe.syncCatastrophes()
//        useCases.ship.syncEngines()
//        val newGameStore = store
//        assertEquals(expected = NewGameScreen, actual = getNavigation().stateFlow.value.screen)
//        assertEquals(expected = Content.SHIP, actual = newGameStore.stateFlow.value.currentContent)
//        getNavigation().back()
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
//
//        newGameStore.send(action = NewGameAction.Next)
//        assertEquals(expected = Content.SHIP, actual = newGameStore.stateFlow.value.currentContent)
//        getNavigation().back()
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
//
//        newGameStore.send(action = NewGameAction.Next)
//        assertEquals(expected = Content.START, actual = newGameStore.stateFlow.value.currentContent)
//        assertNotNull(actual = newGameStore.stateFlow.value.selectedCatastrophe)
//        getNavigation().back()
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
//    }
//
//    @Test
//    fun `send action select ship`() = runBlocking {
//        useCases.catastrophe.syncCatastrophes()
//        useCases.ship.syncEngines()
//        val newGameStore = store
//        assertNull(actual = newGameStore.selectedShip)
//        val shipPrototype = ShipPrototype(
//            assignedPoints = 1,
//            sensorRange = 1,
//            materials = 1,
//            fuel = 1,
//            cryopods = 1
//        )
//        newGameStore.send(action = NewGameAction.SelectShip(ship = shipPrototype))
//        assertEquals(expected = shipPrototype, actual = newGameStore.selectedShip)
//    }
//
//    @Test
//    fun `send action start game`() = runBlocking {
//        assertEquals(expected = NewGameScreen, actual = getNavigation().stateFlow.value.screen)
//        useCases.catastrophe.syncCatastrophes()
//        useCases.ship.syncEngines()
//        val newGameStore = store
//        val shipPrototype = ShipPrototype(
//            assignedPoints = 1,
//            sensorRange = 1,
//            materials = 1,
//            fuel = 1,
//            cryopods = 1
//        )
//        newGameStore.send(action = NewGameAction.SelectShip(ship = shipPrototype))
//        newGameStore.send(action = NewGameAction.SelectEngine(engine = engines.first()))
//        newGameStore.send(action = NewGameAction.Next)
//        assertEquals(expected = GameScreen, actual = getNavigation().stateFlow.value.screen)
//    }
//
//    @Test
//    fun `send action start game without selected ship`() = runBlocking {
//        assertEquals(expected = NewGameScreen, actual = getNavigation().stateFlow.value.screen)
//        val newGameStore = store
//        newGameStore.send(action = NewGameAction.Next)
//        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)
//    }
}
