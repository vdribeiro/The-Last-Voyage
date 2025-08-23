package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.gameSessionPrototype
import com.hybris.tlv.mock.hostsWithPlanets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
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
    fun `send action back`() = runBlocking {
        val gameStore = store
        assertEquals(actual = NavigationManager.Screen.GAME, expected = mock.navigation.stateFlow.value.screen)
        gameStore.send(action = GameAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
