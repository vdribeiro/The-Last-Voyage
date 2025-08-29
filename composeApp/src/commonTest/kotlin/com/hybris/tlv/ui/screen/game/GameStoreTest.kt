package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.gameSessionPrototype
import com.hybris.tlv.mock.hostsWithPlanets
import com.hybris.tlv.mock.mock
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

internal class GameStoreTest {

    private val store
        get() = GameStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = GameState(),
            shipUseCases = mock.useCases.ship,
            spaceUseCases = mock.useCases.space,
            gameSessionUseCases = mock.useCases.gameSession
        )

    @BeforeTest
    fun setup() = runTest {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.GAME)
    }

    @Test
    fun `init`() = runTest {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        assertNotNull(actual = gameStore.stateFlow.value.gameSession)
        assertEquals(expected = Content.SYSTEM, actual = gameStore.stateFlow.value.currentContent)
        assertEquals(expected = stellarHosts.first(), actual = gameStore.stateFlow.value.currentStellarHost)
        assertEquals(
            expected = hostsWithPlanets.drop(n = 1).sortedBy { it.id },
            actual = gameStore.stateFlow.value.nearStellarHosts.sortedBy { it.id }
        )
    }

    @Test
    fun `init without game session`() = runTest {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        val gameStore = store
        assertNull(actual = gameStore.stateFlow.value.gameSession)
        assertEquals(expected = NavigationManager.Screen.ERROR, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `ship is repaired`() = runTest {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val ship = mock.useCases.gameSession.getLatestGameSession()?.ship?.copy(integrity = 0)
        assertNotNull(actual = ship)
        mock.shipDao.upsertShip(ship = ship)
        val gameStore = store
        assertEquals(expected = 1, actual = gameStore.stateFlow.value.gameSession?.ship?.integrity)
        assertEquals(expected = 89, actual = gameStore.stateFlow.value.gameSession?.ship?.materials)
    }

    @Test
    fun `game over by integrity`() = runTest {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val ship = mock.useCases.gameSession.getLatestGameSession()?.ship?.copy(integrity = 0, materials = 0)
        assertNotNull(actual = ship)
        mock.shipDao.upsertShip(ship = ship)
        store
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `game over by fuel`() = runTest {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype.copy(ship = gameSessionPrototype.ship.copy(fuel = 0)))
        store
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `init without stellar host`() = runTest {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        assertNull(actual = gameStore.stateFlow.value.gameSession)
        assertEquals(expected = NavigationManager.Screen.ERROR, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action back`() = runTest {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action change tab`() = runTest {
        val gameStore = store

        gameStore.send(action = GameAction.ChangeTab(Content.SYSTEM))
        assertEquals(expected = Content.SYSTEM, actual = gameStore.stateFlow.value.currentContent)

        gameStore.send(action = GameAction.ChangeTab(Content.SHIP))
        assertEquals(expected = Content.SHIP, actual = gameStore.stateFlow.value.currentContent)

        gameStore.send(action = GameAction.ChangeTab(Content.TRAVEL))
        assertEquals(expected = Content.TRAVEL, actual = gameStore.stateFlow.value.currentContent)
    }

    @Test
    fun `send action travel`() = runTest {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts[1]))
        assertEquals(expected = NavigationManager.Screen.EVENT, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action travel without game session`() = runTest {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts.first()))
        assertEquals(expected = NavigationManager.Screen.ERROR, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action travel without stellar host`() = runTest {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts.first()))
        assertEquals(expected = NavigationManager.Screen.ERROR, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action settle`() = runTest {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Settle(planet = planets.first()))
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action settle without game session`() = runTest {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        val gameStore = store
        gameStore.send(action = GameAction.Settle(planet = planets.first()))
        assertEquals(expected = NavigationManager.Screen.ERROR, actual = mock.navigation.stateFlow.value.screen)
    }
}
