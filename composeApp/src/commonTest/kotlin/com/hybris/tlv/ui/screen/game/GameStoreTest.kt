package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.planets
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.Screen
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

internal class GameStoreTest {

    private val store: GameStore get() = storeFactory.createGameStore()

    @BeforeTest
    fun setup() = runBlocking {
        testDependency.sqlDriver.clearDatabase()
        testDependency.navigation.navigate(screen = Screen.Game)
    }

    @Test
    fun `init`() = runBlocking {
        testDependency.useCases.space.prepopulateStellarHosts()
        testDependency.useCases.space.prepopulatePlanets()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        assertNotNull(actual = gameStore.stateFlow.value.ship)
        assertEquals(expected = Content.SYSTEM, actual = gameStore.stateFlow.value.currentContent)
        assertEquals(expected = stellarHosts.first(), actual = gameStore.stateFlow.value.currentStellarHost)
        assertEquals(
            expected = hostsWithPlanets.drop(n = 1).sortedBy { it.id },
            actual = gameStore.stateFlow.value.nearStellarHosts.sortedBy { it.id }
        )
    }

    @Test
    fun `init without game session`() = runBlocking {
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        val gameStore = store
        assertNull(actual = gameStore.stateFlow.value.ship)
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `ship is repaired`() = runBlocking {
        testDependency.useCases.space.prepopulateStellarHosts()
        testDependency.useCases.space.prepopulatePlanets()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = testDependency.useCases.gameSession.getLatestGameSession()!!
        testDependency.useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0)))
        val gameStore = store
        assertEquals(expected = 1, actual = gameStore.stateFlow.value.ship?.integrity)
        assertEquals(expected = 89, actual = gameStore.stateFlow.value.ship?.materials)
    }

    @Test
    fun `game over by integrity`() = runBlocking {
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = testDependency.useCases.gameSession.getLatestGameSession()!!
        testDependency.useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0, materials = 0)))
        store
        assertEquals(expected = Screen.GameOver, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `game over by fuel`() = runBlocking {
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype.copy(ship = gameSessionPrototype.ship.copy(fuel = 0)))
        store
        assertEquals(expected = Screen.GameOver, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `init without stellar host`() = runBlocking {
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        assertNull(actual = gameStore.stateFlow.value.ship)
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action back`() = runBlocking {
        testDependency.useCases.space.prepopulateStellarHosts()
        testDependency.useCases.space.prepopulatePlanets()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action change tab`() = runBlocking {
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store

        gameStore.send(action = GameAction.ChangeTab(Content.SYSTEM))
        assertEquals(expected = Content.SYSTEM, actual = gameStore.stateFlow.value.currentContent)

        gameStore.send(action = GameAction.ChangeTab(Content.SHIP))
        assertEquals(expected = Content.SHIP, actual = gameStore.stateFlow.value.currentContent)

        gameStore.send(action = GameAction.ChangeTab(Content.TRAVEL))
        assertEquals(expected = Content.TRAVEL, actual = gameStore.stateFlow.value.currentContent)
    }

    @Test
    fun `send action travel`() = runBlocking {
        testDependency.useCases.space.prepopulateStellarHosts()
        testDependency.useCases.space.prepopulatePlanets()
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts[1]))
        assertEquals(expected = Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action travel without game session`() = runBlocking {
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts.first()))
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action travel without stellar host`() = runBlocking {
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts.first()))
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action settle`() = runBlocking {
        testDependency.useCases.space.prepopulateStellarHosts()
        testDependency.useCases.space.prepopulatePlanets()
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Settle(planet = planets.first()))
        assertEquals(expected = Screen.GameOver, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action settle without game session`() = runBlocking {
        assertEquals(expected = Screen.Game, actual = testDependency.navigation.stateFlow.value.screen)
        val gameStore = store
        gameStore.send(action = GameAction.Settle(planet = planets.first()))
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
