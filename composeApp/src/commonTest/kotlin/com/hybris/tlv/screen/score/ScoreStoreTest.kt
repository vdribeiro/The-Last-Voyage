package com.hybris.tlv.screen.score

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.navigation.Screen

internal class ScoreStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = useCases.gameSession.getLatestGameSession()!!
        useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val store = storeFactory.getScoreStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = listOf(element = useCases.gameSession.getLatestGameSession()), actual = store.state.gameSessions)
    }

    @Test
    fun initWithoutScores() = runUnitTest {
        val store = storeFactory.getScoreStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = emptyList(), actual = store.state.gameSessions)
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        navigate(screen = Screen.Score)
        assertNavigationBackstack(list = listOf(element = Screen.Score))
        storeFactory.getScoreStore().back()
        assertNavigationBackstack(list = emptyList())
    }
}
