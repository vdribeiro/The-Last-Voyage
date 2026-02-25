package com.hybris.tlv.ui.screen.score

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.core.locale.getLocalDateTime
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class ScoreStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        var latestGameSession = dependency.get().useCases.gameSession.getLatestGameSession()!!
        dependency.get().useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val store = getStoreFactory().getScoreStore()
        assertFalse(actual = store.state.loading)
        latestGameSession = dependency.get().useCases.gameSession.getLatestGameSession()!!
        assertEquals(
            expected = listOf(latestGameSession.copy(utc = getLocalDateTime(utc = latestGameSession.utc))),
            actual = store.state.gameSessions
        )
    }

    @Test
    fun initWithoutScores() = runUnitTest {
        val store = getStoreFactory().getScoreStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.gameSessions.isEmpty())
    }
}
