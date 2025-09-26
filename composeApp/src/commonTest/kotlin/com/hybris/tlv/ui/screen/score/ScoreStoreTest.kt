package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.mockCore
import com.hybris.tlv.storeFactory
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class ScoreStoreTest {

    private val store: ScoreStore get() = storeFactory.createScoreStore()

    @BeforeTest
    fun setup() = runBlocking {
        mockCore.sqlDriver.clearDatabase()
        mockCore.navigation?.navigate(screen = NavigationManager.Screen.SCORE) ?: Unit
    }

    @Test
    fun `init`() = runBlocking {
        mockCore.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = mockCore.useCases.gameSession.getLatestGameSession()!!
        mockCore.useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val scoreStore = store
        assertEquals(expected = listOf(mockCore.useCases.gameSession.getLatestGameSession()), actual = scoreStore.stateFlow.value.gameSessions)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = NavigationManager.Screen.SCORE, actual = mockCore.navigation?.stateFlow?.value?.screen)
        mockCore.navigation?.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mockCore.navigation?.stateFlow?.value?.screen)
    }
}
