package com.hybris.tlv.usecase.gamesession.local

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.gameSession
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class GameSessionLocalTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.reset()
    }

    @Test
    fun `write and get game sessions`() = runBlocking {
        assertNull(actual = mock.gameSessionDao.getLatestGameSession())
        assertTrue(actual = mock.gameSessionDao.getGameSessions().isEmpty())
        mock.gameSessionDao.startGame(gameSession = gameSession)
        assertEquals(expected = gameSession, actual = mock.gameSessionDao.getLatestGameSession())
        assertTrue(actual = mock.gameSessionDao.getGameSessions().isNotEmpty())
        val newGameSession = gameSession.copy(score = 9000.0)
        mock.gameSessionDao.updateGameSession(gameSession = newGameSession)
        assertEquals(expected = newGameSession, actual = mock.gameSessionDao.getLatestGameSession())
    }
}
