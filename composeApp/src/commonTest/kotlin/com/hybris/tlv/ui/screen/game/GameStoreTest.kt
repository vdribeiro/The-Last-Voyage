package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.gameSessionPrototype
import com.hybris.tlv.mock.hostsWithPlanets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.screen.event.EventAction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking

internal class GameStoreTest {

    private val mock = Mock()
    private val store
        get() = GameStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = GameState(),
            spaceUseCases = mock.useCases.space,
            gameSessionUseCases = mock.useCases.gameSession
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.GAME)
    }

    @Test
    fun `init`() = runBlocking {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        assertNotNull(actual = gameStore.stateFlow.value.gameSession)
        assertEquals(expected = Content.SYSTEM, actual = gameStore.stateFlow.value.currentContent)
        assertEquals(expected = hostsWithPlanets, actual = gameStore.stateFlow.value.stellarHosts)
        assertEquals(expected = stellarHosts.first(), actual = gameStore.stateFlow.value.currentStellarHost)
        assertEquals(expected = hostsWithPlanets.drop(n = 1), actual = gameStore.stateFlow.value.nearStellarHosts)
        assertEquals(expected = setOf(stellarHosts.first().id), actual = gameStore.stateFlow.value.visitedStellarHosts)
    }

    @Test
    fun `init without game session`() = runBlocking {
        assertEquals(actual = NavigationManager.Screen.GAME, expected = mock.navigation.stateFlow.value.screen)
        val gameStore = store
        assertNull(actual = gameStore.stateFlow.value.gameSession)
        assertEquals(actual = NavigationManager.Screen.ERROR, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `ship is repaired`() = runBlocking {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = mock.useCases.gameSession.getLatestGameSession()
        assertNotNull(actual = latestGameSession)
        mock.gameSessionDao.updateGameSession(gameSession = latestGameSession.copy(integrity = 0))
        val gameStore = store
        assertEquals(actual = 1, expected = gameStore.stateFlow.value.gameSession?.integrity)
        assertEquals(actual = 89, expected = gameStore.stateFlow.value.gameSession?.materials)
    }

    @Test
    fun `game over by integrity`() = runBlocking {
        assertEquals(actual = NavigationManager.Screen.GAME, expected = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = mock.useCases.gameSession.getLatestGameSession()
        assertNotNull(actual = latestGameSession)
        mock.gameSessionDao.updateGameSession(gameSession = latestGameSession.copy(integrity = 0, materials = 0))
        store
        assertEquals(actual = NavigationManager.Screen.GAME_OVER, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `game over by fuel`() = runBlocking {
        assertEquals(actual = NavigationManager.Screen.GAME, expected = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype.copy(fuel = 0))
        store
        assertEquals(actual = NavigationManager.Screen.GAME_OVER, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `init without stellar host`() = runBlocking {
        assertEquals(actual = NavigationManager.Screen.GAME, expected = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        assertNull(actual = gameStore.stateFlow.value.gameSession)
        assertEquals(actual = NavigationManager.Screen.ERROR, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        assertEquals(actual = NavigationManager.Screen.GAME, expected = mock.navigation.stateFlow.value.screen)
        gameStore.send(action = GameAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action change tab`() = runBlocking {
        val gameStore = store
        assertEquals(actual = null, expected = gameStore.stateFlow.value.currentContent)

        gameStore.send(action = GameAction.ChangeTab(Content.SYSTEM))
        assertEquals(actual = Content.SYSTEM, expected = gameStore.stateFlow.value.currentContent)

        gameStore.send(action = GameAction.ChangeTab(Content.SHIP))
        assertEquals(actual = Content.SHIP, expected = gameStore.stateFlow.value.currentContent)

        gameStore.send(action = GameAction.ChangeTab(Content.TRAVEL))
        assertEquals(actual = Content.TRAVEL, expected = gameStore.stateFlow.value.currentContent)
    }

    @Test
    fun `send action travel`() = runBlocking {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts[1]))
        assertEquals(actual = 50.0, expected = gameStore.stateFlow.value.gameSession?.yearsTraveled)
        assertEquals(actual = 95, expected = gameStore.stateFlow.value.gameSession?.fuel)
        assertEquals(actual = stellarHosts[1].id, expected = gameStore.stateFlow.value.gameSession?.currentStellarHostId)
        assertEquals(actual = setOf(stellarHosts[0].id, stellarHosts[1].id), expected = gameStore.stateFlow.value.gameSession?.visitedStellarHosts)
    }

    @Test
    fun `send action travel without game session`() = runBlocking {
        assertEquals(actual = NavigationManager.Screen.GAME, expected = mock.navigation.stateFlow.value.screen)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts.first()))
        assertEquals(actual = NavigationManager.Screen.ERROR, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action travel without stellar host`() = runBlocking {
        assertEquals(actual = NavigationManager.Screen.GAME, expected = mock.navigation.stateFlow.value.screen)
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameStore = store
        gameStore.send(action = GameAction.Travel(stellarHost = stellarHosts.first()))
        assertEquals(actual = NavigationManager.Screen.ERROR, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action settle`() = runBlocking {

    }
}
