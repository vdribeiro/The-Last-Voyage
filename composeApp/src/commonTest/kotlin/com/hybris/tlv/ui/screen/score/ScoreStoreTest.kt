package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class ScoreStoreTest {

    private val store: ScoreStore get() = storeFactory.createScoreStore()

    @BeforeTest
    fun setup() = runBlocking {
        testDependency.sqlDriver.clearDatabase()
        testDependency.navigation.navigate(screen = NavigationManager.Screen.Splash)
        testDependency.navigation.navigate(screen = NavigationManager.Screen.MainMenu)
        testDependency.navigation.navigate(screen = NavigationManager.Screen.Score)
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
        assertEquals(expected = NavigationManager.Screen.Score, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MainMenu, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
