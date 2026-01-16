package com.hybris.tlv.ui.screen.score

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.ui.navigation.Screen

internal class ScoreStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = TestCase.useCases.gameSession.getLatestGameSession()!!
        TestCase.useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val store = TestCase.storeFactory.getScoreStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = listOf(TestCase.useCases.gameSession.getLatestGameSession()), actual = store.state.gameSessions)
    }

    @Test
    fun initWithoutScores() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getScoreStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.gameSessions.isEmpty())
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Score)
        TestCase.assertNavigation(list = listOf(Screen.Score))
        TestCase.storeFactory.getScoreStore().back()
        TestCase.assertNavigation(list = emptyList())
    }
}
