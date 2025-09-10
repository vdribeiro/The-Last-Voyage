package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.mock
import com.hybris.tlv.planets
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class GameStoreTest {

    private val store by lazy {
        GameStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = GameState(),
            shipUseCases = mock.useCases.ship,
            spaceUseCases = mock.useCases.space,
            gameSessionUseCases = mock.useCases.gameSession
        )
    }

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.GAME)
    }

    @Test
    fun `init`() = runBlocking {
        mock.useCases.sync.sync().last()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store.apply { setup(state = GameState()) }
        assertNotNull(actual = gameStore.stateFlow.value.gameSession)
        assertEquals(expected = Content.SYSTEM, actual = gameStore.stateFlow.value.currentContent)
        assertEquals(expected = stellarHosts.first(), actual = gameStore.stateFlow.value.currentStellarHost)
        assertEquals(
            expected = hostsWithPlanets.drop(n = 1).sortedBy { it.id },
            actual = gameStore.stateFlow.value.nearStellarHosts.orEmpty().sortedBy { it.id }
        )
    }

    @Test
    fun `init without game session`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        val gameStore = store.apply { setup(state = GameState()) }
        assertNull(actual = gameStore.stateFlow.value.gameSession)
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `ship is repaired`() = runBlocking {
        mock.useCases.sync.sync().last()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = mock.useCases.gameSession.getLatestGameSession()!!
        mock.useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0)))
        val gameStore = store.apply { setup(state = GameState()) }
        assertEquals(expected = 1, actual = gameStore.stateFlow.value.gameSession?.ship?.integrity)
        assertEquals(expected = 89, actual = gameStore.stateFlow.value.gameSession?.ship?.materials)
    }

    @Test
    fun `game over by integrity`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = mock.useCases.gameSession.getLatestGameSession()!!
        mock.useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0, materials = 0)))
        store.setup(state = GameState())
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `game over by fuel`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype.copy(ship = gameSessionPrototype.ship.copy(fuel = 0)))
        store.setup(state = GameState())
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `init without stellar host`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store.apply { setup(state = GameState()) }
        assertNull(actual = gameStore.stateFlow.value.gameSession)
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.useCases.sync.sync().last()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store.setup(state = GameState())
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action change tab`() = runBlocking {
        mock.useCases.sync.sync().last()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store.apply { setup(state = GameState()) }

        gameStore.send(action = GameAction.ChangeTab(Content.SYSTEM))
        assertEquals(expected = Content.SYSTEM, actual = gameStore.stateFlow.value.currentContent)

        gameStore.send(action = GameAction.ChangeTab(Content.SHIP))
        assertEquals(expected = Content.SHIP, actual = gameStore.stateFlow.value.currentContent)

        gameStore.send(action = GameAction.ChangeTab(Content.TRAVEL))
        assertEquals(expected = Content.TRAVEL, actual = gameStore.stateFlow.value.currentContent)
    }

    @Test
    fun `send action travel`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.sync.sync().last()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store.apply { setup(state = GameState()) }
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts[1]))
        assertEquals(expected = NavigationManager.Screen.EVENT, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action travel without game session`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        val gameStore = store.apply { setup(state = GameState()) }
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts.first()))
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action travel without stellar host`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store.apply { setup(state = GameState()) }
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts.first()))
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action settle`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.sync.sync().last()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store.apply { setup(state = GameState()) }
        gameStore.send(action = GameAction.Settle(planet = planets.first()))
        assertEquals(expected = NavigationManager.Screen.GAME_OVER, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action settle without game session`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
        val gameStore = store.apply { setup(state = GameState()) }
        gameStore.send(action = GameAction.Settle(planet = planets.first()))
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mock.navigation.stateFlow.value.screen)
    }
}
