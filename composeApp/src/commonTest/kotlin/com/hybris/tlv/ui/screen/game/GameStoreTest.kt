package com.hybris.tlv.ui.screen.game

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.getNavigation
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.getUseCases
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.planets
import com.hybris.tlv.reset
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Route

internal class GameStoreTest {

    private val store: GameStore get() = getStoreFactory().createGameStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        getNavigation().navigate(navigationState = NavigationState(route = Route.Game))
    }

    @Test
    fun `init`() = runBlocking {
        getUseCases().space.syncStellarHosts()
        getUseCases().space.syncPlanets()
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
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
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)
        val gameStore = store
        assertNull(actual = gameStore.stateFlow.value.ship)
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `ship is repaired`() = runBlocking {
        getUseCases().space.syncStellarHosts()
        getUseCases().space.syncPlanets()
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = getUseCases().gameSession.getLatestGameSession()!!
        getUseCases().gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0)))
        val gameStore = store
        assertEquals(expected = 1, actual = gameStore.stateFlow.value.ship?.integrity)
        assertEquals(expected = 89, actual = gameStore.stateFlow.value.ship?.materials)
    }

    @Test
    fun `game over by integrity`() = runBlocking {
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = getUseCases().gameSession.getLatestGameSession()!!
        getUseCases().gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0, materials = 0)))
        store
        assertEquals(expected = Route.GameOver, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `game over by fuel`() = runBlocking {
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype.copy(ship = gameSessionPrototype.ship.copy(fuel = 0)))
        store
        assertEquals(expected = Route.GameOver, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `init without stellar host`() = runBlocking {
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        assertNull(actual = gameStore.stateFlow.value.ship)
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action back`() = runBlocking {
        getUseCases().space.syncStellarHosts()
        getUseCases().space.syncPlanets()
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)
        getNavigation().back()
        assertEquals(expected = Route.MainMenu, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action change tab`() = runBlocking {
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
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
        getUseCases().space.syncStellarHosts()
        getUseCases().space.syncPlanets()
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts[1]))
        assertEquals(expected = Route.Event, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action travel without game session`() = runBlocking {
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts.first()))
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action travel without stellar host`() = runBlocking {
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts.first()))
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action settle`() = runBlocking {
        getUseCases().space.syncStellarHosts()
        getUseCases().space.syncPlanets()
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Settle(planet = planets.first()))
        assertEquals(expected = Route.GameOver, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action settle without game session`() = runBlocking {
        assertEquals(expected = Route.Game, actual = getNavigation().stateFlow.value.route)
        val gameStore = store
        gameStore.send(action = GameAction.Settle(planet = planets.first()))
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)
    }
}
