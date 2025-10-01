package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class ScoreStoreTest {

    private val store: ScoreStore get() = storeFactory.createScoreStore()

    @BeforeTest
    fun setup() = runBlocking {
        testCore.clearDatabase()
        testCore.navigation.navigate(screen = NavigationManager.Screen.Splash)
        testCore.navigation.navigate(screen = NavigationManager.Screen.MainMenu)
        testCore.navigation.navigate(screen = NavigationManager.Screen.Score)
    }

    @Test
    fun `init`() = runBlocking {
        testCore.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = testCore.useCases.gameSession.getLatestGameSession()!!
        testCore.useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val scoreStore = store
        assertEquals(expected = listOf(testCore.useCases.gameSession.getLatestGameSession()), actual = scoreStore.stateFlow.value.gameSessions)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = NavigationManager.Screen.Score, actual = testCore.navigation.stateFlow.value.screen)
        testCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MainMenu, actual = testCore.navigation.stateFlow.value.screen)
    }
}
