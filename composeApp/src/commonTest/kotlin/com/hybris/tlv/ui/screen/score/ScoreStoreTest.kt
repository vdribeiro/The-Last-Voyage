package com.hybris.tlv.ui.screen.score

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class ScoreStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        getUseCases().ship.prepopulateEngines()
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val latestGameSession = getUseCases().gameSession.getLatestGameSession()!!
        getUseCases().gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val store = getStoreFactory().getScoreStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = listOf(getUseCases().gameSession.getLatestGameSession()), actual = store.state.gameSessions)
    }

    @Test
    fun initWithoutScores() = runUnitTest {
        val store = getStoreFactory().getScoreStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.gameSessions.isEmpty())
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Score)
        assertNavigation(list = listOf(Screen.Score))
        getStoreFactory().getScoreStore().back()
        assertNavigation(list = emptyList())
    }
}
