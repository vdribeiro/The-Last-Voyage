package com.hybris.tlv.domain.usecase.gamesession

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.yield
import com.hybris.tlv.domain.usecase.gamesession.model.GameOver
import com.hybris.tlv.domain.usecase.space.SUN
import com.hybris.tlv.domain.usecase.space.formula.Habitability
import com.hybris.tlv.domain.usecase.space.model.Formula
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class GameSessionUseCasesTest: TestCase() {

    @Test
    fun writeAndGetGameSessions() = runUnitTest {
        dependency.get().useCases.ship.prepopulateEngines()
        assertNull(actual = dependency.get().useCases.gameSession.getLatestGameSession())
        assertTrue(actual = dependency.get().useCases.gameSession.getGameSessions().isEmpty())
        assertFalse(actual = dependency.get().useCases.gameSession.isGameSessionOngoing())

        val gameSession = dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        yield()
        assertEquals(expected = gameSession, actual = dependency.get().useCases.gameSession.getLatestGameSession())
        assertEquals(expected = listOf(gameSession), actual = dependency.get().useCases.gameSession.getGameSessions())
        assertTrue(actual = dependency.get().useCases.gameSession.isGameSessionOngoing())

        val newGameSession = gameSession.copy(score = 9000.0)
        dependency.get().useCases.gameSession.updateGameSession(gameSession = newGameSession)
        assertEquals(expected = newGameSession, actual = dependency.get().useCases.gameSession.getLatestGameSession())
    }

    @Test
    fun launchEvent() = runUnitTest {
        val gameSession = dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val event = FakeData.events.get().first { it.outcome != null }

        val newGameSession = dependency.get().useCases.gameSession.launchEvent(gameSession = gameSession, event = event)
        assertEquals(expected = gameSession.ship.integrity + (event.outcome?.integrity ?: 0), actual = newGameSession.ship.integrity)
        assertEquals(expected = gameSession.ship.fuel + (event.outcome?.fuel ?: 0), actual = newGameSession.ship.fuel)
        assertEquals(expected = gameSession.ship.materials + (event.outcome?.materials ?: 0), actual = newGameSession.ship.materials)
        assertEquals(expected = gameSession.ship.cryopods + (event.outcome?.cryopods ?: 0), actual = newGameSession.ship.cryopods)
    }

    @Test
    fun travel() = runUnitTest {
        val gameSession = dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val stellarHost = FakeData.stellarHosts.get().filter { it.id != SUN }.random()

        val newGameSession = dependency.get().useCases.gameSession.travel(gameSession = gameSession, stellarHost = stellarHost)
        assertEquals(expected = stellarHost.id, actual = newGameSession.currentStellarHostId)
        assertTrue(actual = newGameSession.visitedStellarHosts.contains(element = stellarHost.id))
        assertTrue(actual = newGameSession.ship.yearsTraveled > gameSession.ship.yearsTraveled)
        assertTrue(actual = newGameSession.ship.integrity < gameSession.ship.integrity)
        assertTrue(actual = newGameSession.ship.fuel < gameSession.ship.fuel)
    }

    @Test
    fun settle() = runUnitTest {
        val gameSession = dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val stellarHost = FakeData.stellarHostsWithPlanets.get().filter { it.planets.isNotEmpty() }.random()
        val planet = stellarHost.planets.random().apply {
            score = Habitability.calculateScores(
                stellarHost = stellarHost,
                planet = this,
                formula = Formula()
            )
        }

        val newGameSession = dependency.get().useCases.gameSession.settle(gameSession = gameSession, planet = planet)
        assertEquals(expected = planet.id, actual = newGameSession.settledPlanetId)
        assertEquals(expected = planet.score?.habitabilityScore?.times(other = 100.0), actual = newGameSession.finalHabitability)
    }

    @Test
    fun score() = runUnitTest {
        val gameSession = dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val gameOver = GameOver.entries.random()

        val newGameSession = dependency.get().useCases.gameSession.score(gameSession = gameSession, gameOver = gameOver)
        assertNotNull(actual = newGameSession.score)
    }

    @Test
    fun isGameOver() = runUnitTest {
        val gameSession = dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        assertFalse(actual = dependency.get().useCases.gameSession.isGameOver(gameSession = gameSession))

        val gameSessionNoIntegrity = gameSession.copy(ship = gameSession.ship.copy(integrity = 0))
        dependency.get().useCases.gameSession.updateGameSession(gameSession = gameSessionNoIntegrity)
        assertTrue(actual = dependency.get().useCases.gameSession.isGameOver(gameSession = gameSessionNoIntegrity))
        val gameOverNoIntegrity = dependency.get().useCases.gameSession.getGameOver(gameSession = gameSessionNoIntegrity)
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

        dependency.get().useCases.gameSession.updateGameSession(gameSession = gameSession)
        assertFalse(actual = dependency.get().useCases.gameSession.isGameOver(gameSession = gameSession))

        val gameSessionNoFuel = gameSession.copy(ship = gameSession.ship.copy(fuel = 0))
        dependency.get().useCases.gameSession.updateGameSession(gameSession = gameSessionNoFuel)
        assertTrue(actual = dependency.get().useCases.gameSession.isGameOver(gameSession = gameSessionNoFuel))
        val gameOverNoFuel = dependency.get().useCases.gameSession.getGameOver(gameSession = gameSessionNoFuel)
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

        dependency.get().useCases.gameSession.updateGameSession(gameSession = gameSession)
        assertFalse(actual = dependency.get().useCases.gameSession.isGameOver(gameSession = gameSession))

        val gameSessionSettled = gameSession.copy(
            settledPlanetId = FakeData.planets.get().random().id,
            finalHabitability = Random.nextDouble(until = 100.0)
        )
        dependency.get().useCases.gameSession.updateGameSession(gameSession = gameSessionSettled)
        assertTrue(actual = dependency.get().useCases.gameSession.isGameOver(gameSession = gameSessionSettled))
        val gameOverSettled = dependency.get().useCases.gameSession.getGameOver(gameSession = gameSessionSettled)
        val gameOverSettledList = GameOver.entries - (gameOverNoIntegrityList + gameOverNoFuelList)
        assertTrue(actual = gameOverSettledList.any { it == gameOverSettled })
    }
}
