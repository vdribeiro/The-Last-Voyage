package com.hybris.tlv.usecase.gamesession

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.gameSessionPrototype
import com.hybris.tlv.mock.hostsWithPlanets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.space.formula.Habitability
import com.hybris.tlv.usecase.space.model.Formula
import kotlin.math.ceil
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
        assertFalse(actual = mock.useCases.gameSession.isGameSessionOngoing())
        val gameSession = mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        assertTrue(actual = mock.useCases.gameSession.isGameSessionOngoing())
        assertEquals(expected = gameSession, actual = mock.useCases.gameSession.getLatestGameSession())
        assertTrue(actual = mock.useCases.gameSession.getGameSessions().isNotEmpty())
        val newGameSession = gameSession.copy(score = 9000.0)
        mock.useCases.gameSession.updateGameSession(gameSession = newGameSession)
        assertEquals(expected = newGameSession, actual = mock.useCases.gameSession.getLatestGameSession())
    }

    @Test
    fun `do event`() = runBlocking {
        val gameSession = mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val event = events.find { it.outcome != null }!!
        event.outcome!!
        val newGameSession = mock.useCases.gameSession.doEvent(gameSession = gameSession, event = event)
        assertEquals(expected = gameSession.ship.integrity + (event.outcome.integrity ?: 0), actual = newGameSession.ship.integrity)
        assertEquals(expected = gameSession.ship.fuel + (event.outcome.fuel ?: 0), actual = newGameSession.ship.fuel)
        assertEquals(expected = gameSession.ship.materials + (event.outcome.materials ?: 0), actual = newGameSession.ship.materials)
        assertEquals(expected = gameSession.ship.cryopods + (event.outcome.cryopods ?: 0), actual = newGameSession.ship.cryopods)
    }

    @Test
    fun travel() = runBlocking {
        val gameSession = mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val stellarHost = stellarHosts.random()
        val newGameSession = mock.useCases.gameSession.travel(gameSession = gameSession, stellarHost = stellarHost)
        assertEquals(expected = stellarHost.id, actual = newGameSession.currentStellarHostId)
        assertTrue(actual = newGameSession.visitedStellarHosts.contains(element = stellarHost.id))

        val distance = ceil(x = stellarHost.distance ?: 1.0).toInt()
        val speed = 0.1  // TODO - use engine speed - using 0.1c for now
        assertEquals(expected = gameSession.ship.yearsTraveled + (distance / speed), actual = newGameSession.ship.yearsTraveled)
        assertEquals(expected = gameSession.ship.fuel - distance, actual = newGameSession.ship.fuel)
    }

    @Test
    fun settle() = runBlocking {
        val gameSession = mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val stellarHost = hostsWithPlanets.filter { it.planets.isNotEmpty() }.random()
        val planet = stellarHost.planets.random().apply {
            habitability = Habitability.calculateHabitability(
                stellarHost = stellarHost,
                planet = this,
                formula = Formula()
            )
        }

        val newGameSession = mock.useCases.gameSession.settle(gameSession = gameSession, planet = planet)
        assertEquals(expected = planet.id, actual = newGameSession.settledPlanetId)
        assertEquals(expected = planet.habitability?.habitabilityScore?.times(other = 100.0), actual = newGameSession.finalHabitability)
    }

    @Test
    fun score() = runBlocking {
        val gameSession = mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameOver = GameOver.entries.random()
        val newGameSession = mock.useCases.gameSession.score(gameSession = gameSession, gameOver = gameOver)

        val ship = gameSession.ship
        // Base Score = (Cryopod Score) + (Resource Score) + (Journey Score)
        val cryopodScore = ship.cryopods * 100
        val resourceScore = ship.materials * 2 + ship.fuel * 1
        val journeyScore = ship.yearsTraveled * 5
        val baseScore = cryopodScore + resourceScore + journeyScore
        // Challenge Multiplier
        val challengeMultiplier = (1.0 + (15 - ship.assignedPoints) + 0.05).coerceIn(minimumValue = 0.01, maximumValue = 10.0)
        // Final Score = (Base Score) * Habitability Multiplier * Success Multiplier * Challenge Multiplier
        val score = baseScore * gameOver.multiplier * challengeMultiplier

        assertEquals(expected = score, actual = newGameSession.score)
    }

    @Test
    fun `is game over`() = runBlocking {
        val gameSession = mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        assertFalse(actual = mock.useCases.gameSession.isGameOver(gameSession = gameSession))

        val gameSessionNoIntegrity = gameSession.copy(ship = gameSession.ship.copy(integrity = 0))
        mock.useCases.gameSession.updateGameSession(gameSession = gameSessionNoIntegrity)
        assertTrue(actual = mock.useCases.gameSession.isGameOver(gameSession = gameSessionNoIntegrity))

        mock.useCases.gameSession.updateGameSession(gameSession = gameSession)
        assertFalse(actual = mock.useCases.gameSession.isGameOver(gameSession = gameSession))

        val gameSessionNoFuel = gameSession.copy(ship = gameSession.ship.copy(fuel = 0))
        mock.useCases.gameSession.updateGameSession(gameSession = gameSessionNoFuel)
        assertTrue(actual = mock.useCases.gameSession.isGameOver(gameSession = gameSessionNoFuel))
    }

    @Test
    fun `get game over`() = runBlocking {
        //TODO
    }
}
