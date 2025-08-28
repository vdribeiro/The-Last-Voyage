package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.mock
import com.hybris.tlv.mock.gameSessionPrototype
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class ScoreStoreTest {

    private val store
        get() = ScoreStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = ScoreState(),
            gameSessionUseCases = mock.useCases.gameSession
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.SCORE)
    }

    @Test
    fun `init`() = runBlocking {
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = mock.useCases.gameSession.getLatestGameSession()!!
        mock.useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val scoreStore = store
        assertEquals(actual = listOf(mock.useCases.gameSession.getLatestGameSession()), expected = scoreStore.stateFlow.value.scores)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(actual = NavigationManager.Screen.SCORE, expected = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }
}
