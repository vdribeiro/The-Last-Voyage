package com.hybris.tlv.screen.score

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.getScoreStore
import com.hybris.tlv.reset
import com.hybris.tlv.useCases

internal class ScoreStoreTest {

    private val store: ScoreStore get() = getScoreStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = SplashScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = MainMenuScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = ScoreScreen))
    }

    @Test
    fun `init`() = runBlocking {
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = useCases.gameSession.getLatestGameSession()!!
        useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val scoreStore = store
        assertEquals(expected = listOf(useCases.gameSession.getLatestGameSession()), actual = scoreStore.stateFlow.value.gameSessions)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
//        assertEquals(expected = ScoreScreen, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
    }
}
