package com.hybris.tlv.usecase.gamesession.local

import com.hybris.tlv.Tester
import com.hybris.tlv.mock.credits
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.gameSession
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class GameSessionLocalTest: Tester() {

    @Test
    fun `write and get game sessions`() = runBlocking {
        assertNull(actual = gameSessionDao.getLatestGameSession())
        assertTrue(actual = gameSessionDao.getGameSessions().isEmpty())
        gameSessionDao.startGame(gameSession = gameSession)
        assertEquals(expected = gameSession, actual = gameSessionDao.getLatestGameSession())
        assertTrue(actual = gameSessionDao.getGameSessions().isNotEmpty())
        val newGameSession = gameSession.copy(score = 9000.0)
        gameSessionDao.updateGameSession(gameSession = newGameSession)
        assertEquals(expected = newGameSession, actual = gameSessionDao.getLatestGameSession())
    }
}
