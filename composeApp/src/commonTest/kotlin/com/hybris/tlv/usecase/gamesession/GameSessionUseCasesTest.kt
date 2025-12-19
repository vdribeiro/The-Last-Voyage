package com.hybris.tlv.usecase.gamesession

import kotlin.math.ceil
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.events
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.planets
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.space.formula.Habitability
import com.hybris.tlv.usecase.space.model.Formula

internal class GameSessionUseCasesTest: TestCase() {

    @Test
    fun `write and get game sessions`() = runUnitTest {
        assertNull(actual = useCases.gameSession.getLatestGameSession())
        assertTrue(actual = useCases.gameSession.getGameSessions().isEmpty())
        assertFalse(actual = useCases.gameSession.isGameSessionOngoing())
        val gameSession = useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        assertTrue(actual = useCases.gameSession.isGameSessionOngoing())
        assertEquals(expected = gameSession, actual = useCases.gameSession.getLatestGameSession())
        assertTrue(actual = useCases.gameSession.getGameSessions().isNotEmpty())
        val newGameSession = gameSession.copy(score = 9000.0)
        useCases.gameSession.updateGameSession(gameSession = newGameSession)
        assertEquals(expected = newGameSession, actual = useCases.gameSession.getLatestGameSession())
    }

    @Test
    fun `do event`() = runUnitTest {
        val gameSession = useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val event = events.find { it.outcome != null }!!
        event.outcome!!
        val newGameSession = useCases.gameSession.launchEvent(gameSession = gameSession, event = event)
        assertEquals(expected = gameSession.ship.integrity + (event.outcome.integrity ?: 0), actual = newGameSession.ship.integrity)
        assertEquals(expected = gameSession.ship.fuel + (event.outcome.fuel ?: 0), actual = newGameSession.ship.fuel)
        assertEquals(expected = gameSession.ship.materials + (event.outcome.materials ?: 0), actual = newGameSession.ship.materials)
        assertEquals(expected = gameSession.ship.cryopods + (event.outcome.cryopods ?: 0), actual = newGameSession.ship.cryopods)
    }

    @Test
    fun travel() = runUnitTest {
        val gameSession = useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val stellarHost = stellarHosts.random()
        val newGameSession = useCases.gameSession.travel(gameSession = gameSession, stellarHost = stellarHost)
        assertEquals(expected = stellarHost.id, actual = newGameSession.currentStellarHostId)
        assertTrue(actual = newGameSession.visitedStellarHosts.contains(element = stellarHost.id))

        val distance = ceil(x = stellarHost.distance ?: 1.0).toInt()
        val speed = gameSession.ship.engine.velocity
        assertEquals(expected = gameSession.ship.yearsTraveled + (distance / speed), actual = newGameSession.ship.yearsTraveled)
        assertEquals(expected = gameSession.ship.fuel - distance, actual = newGameSession.ship.fuel)
    }

    @Test
    fun settle() = runUnitTest {
        val gameSession = useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val stellarHost = hostsWithPlanets.filter { it.planets.isNotEmpty() }.random()
        val planet = stellarHost.planets.random().apply {
            score = Habitability.calculateScores(
                stellarHost = stellarHost,
                planet = this,
                formula = Formula()
            )
        }

        val newGameSession = useCases.gameSession.settle(gameSession = gameSession, planet = planet)
        assertEquals(expected = planet.id, actual = newGameSession.settledPlanetId)
        assertEquals(expected = planet.score?.habitabilityScore?.times(other = 100.0), actual = newGameSession.finalHabitability)
    }

    @Test
    fun score() = runUnitTest {
        val gameSession = useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameOver = GameOver.entries.random()
        val newGameSession = useCases.gameSession.score(gameSession = gameSession, gameOver = gameOver)

        val ship = gameSession.ship
        val cryopodScore = ship.cryopods * 100
        val resourceScore = ship.materials * 2 + ship.fuel * 1
        val journeyScore = ship.yearsTraveled * 5
        val baseScore = cryopodScore + resourceScore + journeyScore
        val challengeMultiplier = (1.0 + (15 - ship.assignedPoints) + 0.05).coerceIn(minimumValue = 0.01, maximumValue = 10.0)
        val gameOverMultiplier = (useCases.gameSession as GameSessionGateway).getGameOverMultiplier(gameOver = gameOver)
        val score = baseScore * gameOverMultiplier * challengeMultiplier

        assertEquals(expected = score, actual = newGameSession.score)
    }

    @Test
    fun `is game over`() = runUnitTest {
        val gameSession = useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        assertFalse(actual = useCases.gameSession.isGameOver(gameSession = gameSession))

        val gameSessionNoIntegrity = gameSession.copy(ship = gameSession.ship.copy(integrity = 0))
        useCases.gameSession.updateGameSession(gameSession = gameSessionNoIntegrity)
        assertTrue(actual = useCases.gameSession.isGameOver(gameSession = gameSessionNoIntegrity))
        val gameOverNoIntegrity = useCases.gameSession.getGameOver(gameSession = gameSessionNoIntegrity)
        val gameOverNoIntegrityList = listOf(
            GameOver.INTEGRITY_ZERO,
            GameOver.INTEGRITY_ZERO_YEARS_FEW,
            GameOver.INTEGRITY_ZERO_YEARS_SOME,
            GameOver.INTEGRITY_ZERO_YEARS_LOTS,
            GameOver.INTEGRITY_ZERO_CRYOPODS_ZERO,
            GameOver.INTEGRITY_ZERO_CRYOPODS_ONE,
            GameOver.INTEGRITY_ZERO_CRYOPODS_LOW,
            GameOver.INTEGRITY_ZERO_CRYOPODS_ENOUGH,
            GameOver.INTEGRITY_ZERO_FUEL_LOW,
            GameOver.INTEGRITY_ZERO_FUEL_SOME,
            GameOver.INTEGRITY_ZERO_FUEL_PLENTY,
            GameOver.INTEGRITY_ZERO_YEARS_LOTS_CRYOPODS_BUSTLING
        )
        assertTrue(actual = gameOverNoIntegrityList.any { it == gameOverNoIntegrity })

        useCases.gameSession.updateGameSession(gameSession = gameSession)
        assertFalse(actual = useCases.gameSession.isGameOver(gameSession = gameSession))

        val gameSessionNoFuel = gameSession.copy(ship = gameSession.ship.copy(fuel = 0))
        useCases.gameSession.updateGameSession(gameSession = gameSessionNoFuel)
        assertTrue(actual = useCases.gameSession.isGameOver(gameSession = gameSessionNoFuel))
        val gameOverNoFuel = useCases.gameSession.getGameOver(gameSession = gameSessionNoFuel)
        val gameOverNoFuelList = listOf(
            GameOver.FUEL_ZERO,
            GameOver.FUEL_ZERO_YEARS_FEW,
            GameOver.FUEL_ZERO_YEARS_SOME,
            GameOver.FUEL_ZERO_YEARS_LOTS,
            GameOver.FUEL_ZERO_MATERIALS_ZERO,
            GameOver.FUEL_ZERO_MATERIALS_LOW,
            GameOver.FUEL_ZERO_MATERIALS_ENOUGH,
            GameOver.FUEL_ZERO_CRYOPODS_ZERO,
            GameOver.FUEL_ZERO_CRYOPODS_ONE,
            GameOver.FUEL_ZERO_CRYOPODS_LOW,
            GameOver.FUEL_ZERO_CRYOPODS_ENOUGH,
            GameOver.FUEL_ZERO_INTEGRITY_LOW,
            GameOver.FUEL_ZERO_INTEGRITY_ENOUGH,
            GameOver.FUEL_ZERO_INTEGRITY_PRISTINE,
            GameOver.FUEL_ZERO_MATERIALS_PLENTY_CRYOPODS_BUSTLING,
            GameOver.FUEL_ZERO_INTEGRITY_ENOUGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING
        )
        assertTrue(actual = gameOverNoFuelList.any { it == gameOverNoFuel })

        useCases.gameSession.updateGameSession(gameSession = gameSession)
        assertFalse(actual = useCases.gameSession.isGameOver(gameSession = gameSession))

        val gameSessionSettled = gameSession.copy(
            settledPlanetId = planets.random().id,
            finalHabitability = Random.nextDouble(until = 100.0)
        )
        useCases.gameSession.updateGameSession(gameSession = gameSessionSettled)
        assertTrue(actual = useCases.gameSession.isGameOver(gameSession = gameSessionSettled))
        val gameOverSettled = useCases.gameSession.getGameOver(gameSession = gameSessionSettled)
        val gameOverSettledList = GameOver.entries - (gameOverNoIntegrityList + gameOverNoFuelList)
        assertTrue(actual = gameOverSettledList.any { it == gameOverSettled })
    }
}
