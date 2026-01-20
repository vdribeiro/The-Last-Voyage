package com.hybris.tlv.ui.screen.score

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.gameSessionPrototype
import com.hybris.tlv.ui.navigation.Screen

internal class ScoreStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = useCases.gameSession.getLatestGameSession()!!
        useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val store = storeFactory.getScoreStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = listOf(useCases.gameSession.getLatestGameSession()), actual = store.state.gameSessions)
    }

    @Test
    fun initWithoutScores() = runUnitTest {
        val store = storeFactory.getScoreStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.gameSessions.isEmpty())
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Score)
        assertNavigation(list = listOf(Screen.Score))
        storeFactory.getScoreStore().back()
        assertNavigation(list = emptyList())
    }
}
