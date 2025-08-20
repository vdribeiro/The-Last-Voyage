package com.hybris.tlv.usecase.gamesession

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.gameSession
import com.hybris.tlv.mock.gameSessionPrototype
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class GameSessionUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `write and get game sessions`() = runBlocking {
        assertNull(actual = mock.useCases.gameSession.getLatestGameSession())
        assertTrue(actual = mock.useCases.gameSession.getGameSessions().isEmpty())
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = mock.useCases.gameSession.getLatestGameSession()
        assertNotNull(actual = latestGameSession)
        val gameSessionFromPrototype = gameSession.copy(
            id = latestGameSession.id,
            utc = latestGameSession.utc,
            yearsTraveled = 0.0,
            integrity = 100,
            currentStellarHostId = null
        )
        assertEquals(expected = gameSessionFromPrototype, actual = mock.useCases.gameSession.getLatestGameSession())
        assertTrue(actual = mock.useCases.gameSession.getGameSessions().isNotEmpty())
        val newGameSession = gameSessionFromPrototype.copy(score = 9000.0)
        mock.useCases.gameSession.updateGameSession(gameSession = newGameSession)
        assertEquals(expected = newGameSession, actual = mock.useCases.gameSession.getLatestGameSession())
    }
}
