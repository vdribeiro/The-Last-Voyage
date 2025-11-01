package com.hybris.tlv.ui.screen.score

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.reset
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen

internal class ScoreStoreTest {

    private val store: ScoreStore get() = storeFactory.createScoreStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Splash))
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.MainMenu))
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Score))
    }

    @Test
    fun `init`() = runBlocking {
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = testDependency.useCases.gameSession.getLatestGameSession()!!
        testDependency.useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val scoreStore = store
        assertEquals(expected = listOf(testDependency.useCases.gameSession.getLatestGameSession()), actual = scoreStore.stateFlow.value.gameSessions)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = Screen.Score, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
