package com.hybris.tlv.ui.screen.score

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.getNavigation
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Route

internal class ScoreStoreTest {

    private val store: ScoreStore get() = getStoreFactory().createScoreStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        getNavigation().navigate(navigationState = NavigationState(route = Route.Splash))
        getNavigation().navigate(navigationState = NavigationState(route = Route.MainMenu))
        getNavigation().navigate(navigationState = NavigationState(route = Route.Score))
    }

    @Test
    fun `init`() = runBlocking {
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = getUseCases().gameSession.getLatestGameSession()!!
        getUseCases().gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val scoreStore = store
        assertEquals(expected = listOf(getUseCases().gameSession.getLatestGameSession()), actual = scoreStore.stateFlow.value.gameSessions)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = Route.Score, actual = getNavigation().stateFlow.value.route)
        getNavigation().back()
        assertEquals(expected = Route.MainMenu, actual = getNavigation().stateFlow.value.route)
    }
}
