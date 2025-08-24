package com.hybris.tlv.usecase.gamesession.local

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.gameSession
import com.hybris.tlv.mock.ship
import com.hybris.tlv.usecase.space.model.Formula
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
        mock.clearDatabase()
    }

    @Test
    fun `write and get game sessions`() = runBlocking {
        assertNull(actual = mock.gameSessionDao.getLatestGameSession())
        assertTrue(actual = mock.gameSessionDao.getGameSessions().isEmpty())
        mock.gameSessionDao.upsertGameSession(gameSession = gameSession)
        mock.shipDao.upsertShip(ship = ship)
        mock.spaceDao.upsertFormula(formula = Formula(id = "1"))
        assertEquals(expected = gameSession, actual = mock.gameSessionDao.getLatestGameSession())
        assertTrue(actual = mock.gameSessionDao.getGameSessions().isNotEmpty())
        val newGameSession = gameSession.copy(score = 9000.0)
        mock.gameSessionDao.upsertGameSession(gameSession = newGameSession)
        assertEquals(expected = newGameSession, actual = mock.gameSessionDao.getLatestGameSession())
    }
}
