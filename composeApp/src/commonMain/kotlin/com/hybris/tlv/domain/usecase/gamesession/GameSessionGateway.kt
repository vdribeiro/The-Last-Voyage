package com.hybris.tlv.domain.usecase.gamesession

import kotlin.math.ceil
import kotlinx.coroutines.withContext
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.domain.event.Event
import com.hybris.tlv.domain.usecase.gamesession.model.GameOver
import com.hybris.tlv.domain.usecase.gamesession.model.GameSession
import com.hybris.tlv.domain.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.domain.space.Planet
import com.hybris.tlv.domain.space.StellarHost
import database.AppDatabase

internal class GameSessionGateway(
    database: AppDatabase
): GameSessionUseCases {

    private val gameSessionDao = database.gameSessionQueries
    private val shipDao = database.shipQueries
    private val formulaDao = database.formulaQueries

    override suspend fun startGame(gameSessionPrototype: GameSessionPrototype): GameSession = withContext(context = Dispatcher.IO) {
        val gameSession = gameSessionPrototype.toGameSession()
        updateGameSession(gameSession = gameSession)
    }

    override suspend fun getGameSessions(): List<GameSession> = withContext(context = Dispatcher.IO) {
        gameSessionDao.getGameSessions(mapper = gameSessionProjection).awaitAsList()
    }

    override suspend fun getLatestGameSession(): GameSession? = withContext(context = Dispatcher.IO) {
        gameSessionDao.getLatestGameSession(mapper = gameSessionProjection).awaitAsOneOrNull()
    }

    override suspend fun isGameSessionOngoing(): Boolean = withContext(context = Dispatcher.IO) {
        val gameSession = getLatestGameSession()
        gameSession != null &&
                gameSession.settledPlanetId == null &&
                gameSession.finalHabitability == null &&
                gameSession.ship.integrity > 0 &&
                gameSession.ship.fuel > 0
    }

    override suspend fun updateGameSession(gameSession: GameSession): GameSession = withContext(context = Dispatcher.IO) {
        gameSessionDao.upsertGameSession(GameSession = gameSession.toGameSessionSchema())
        shipDao.upsertShip(Ship = gameSession.ship.toShipSchema())
        formulaDao.upsertFormula(Formula = gameSession.formula.toFormulaSchema())
        gameSession
    }

    override suspend fun launchEvent(gameSession: GameSession, event: Event): GameSession = withContext(context = Dispatcher.IO) {
        val integrity = gameSession.ship.integrity + (event.outcome?.integrity ?: 0)
        val materials = gameSession.ship.materials + (event.outcome?.materials ?: 0)
        val fuel = gameSession.ship.fuel + (event.outcome?.fuel ?: 0)
        val cryopods = gameSession.ship.cryopods + (event.outcome?.cryopods ?: 0)
        val updatedGameSession = gameSession.copy(
            ship = gameSession.ship.copy(
                integrity = integrity,
                materials = materials,
                fuel = fuel,
                cryopods = cryopods
            ),
            launchedEvents = gameSession.launchedEvents + event.id
        )
        updateGameSession(gameSession = updatedGameSession)
    }

    override suspend fun travel(gameSession: GameSession, stellarHost: StellarHost): GameSession = withContext(context = Dispatcher.IO) {
        val distance = stellarHost.distance ?: 1.0
        val speed = gameSession.ship.engine.velocity
        val fuelConsumption = gameSession.ship.engine.fuelConsumption
        val yearsTraveled = gameSession.ship.yearsTraveled + (distance / speed)
        val fuel = gameSession.ship.fuel - distance * fuelConsumption
        val integrity = gameSession.ship.integrity - (yearsTraveled / 1000).toInt().coerceIn(minimumValue = 1, maximumValue = 100)

        val updatedGameSession = gameSession.copy(
            ship = gameSession.ship.copy(
                yearsTraveled = yearsTraveled,
                integrity = integrity,
                fuel = ceil(x = fuel).toInt(),
            ),
            currentStellarHostId = stellarHost.id,
            visitedStellarHosts = gameSession.visitedStellarHosts + stellarHost.id
        )
        updateGameSession(gameSession = updatedGameSession)
    }

    override suspend fun settle(gameSession: GameSession, planet: Planet): GameSession = withContext(context = Dispatcher.IO) {
        val updatedGameSession = gameSession.copy(
            settledPlanetId = planet.id,
            settledPlanetName = planet.name,
            finalHabitability = planet.score?.habitabilityScore?.times(other = 100.0)
        )
        updateGameSession(gameSession = updatedGameSession)
    }

    override suspend fun score(gameSession: GameSession, gameOver: GameOver): GameSession = withContext(context = Dispatcher.IO) {
        val ship = gameSession.ship

        // Base Score = Cryopod Score + Fuel Score + Materials Score + Journey Score
        val cryopodScore = ship.cryopods * CRYOPODS_MULTIPLIER
        val fuelScore = ship.fuel * FUEL_MULTIPLIER
        val materialsScore = ship.materials * MATERIALS_MULTIPLIER
        val journeyScore = ship.yearsTraveled * YEARS_MULTIPLIER
        val baseScore = cryopodScore + fuelScore + materialsScore + journeyScore
        // Challenge Multiplier
        val challengeMultiplier = (1.0 + (15 - ship.assignedPoints) + 0.05).coerceIn(minimumValue = 0.01, maximumValue = 10.0)
        // Final Score = Base Score * Challenge Multiplier * Game Over Multiplier
        val score = baseScore * challengeMultiplier * gameOver.multiplier

        val updatedGameSession = gameSession.copy(score = score)
        updateGameSession(gameSession = updatedGameSession)
    }

    override suspend fun getGameOver(gameSession: GameSession): GameOver = withContext(context = Dispatcher.Default) {
        val ship = gameSession.ship
        when {
            // Ship is destroyed
            ship.integrity <= 0 -> when {
                ship.yearsTraveled >= YEARS_LOTS && ship.cryopods >= CRYOPODS_LOTS -> GameOver.INTEGRITY_ZERO_YEARS_LOTS_CRYOPODS_BUSTLING
                ship.cryopods < 1 -> GameOver.INTEGRITY_ZERO_CRYOPODS_ZERO
                ship.cryopods == 1 -> GameOver.INTEGRITY_ZERO_CRYOPODS_ONE
                ship.cryopods in 2..CRYOPODS_LOW -> GameOver.INTEGRITY_ZERO_CRYOPODS_LOW
                ship.fuel < FUEL_LOW -> GameOver.INTEGRITY_ZERO_FUEL_LOW
                ship.fuel in FUEL_LOW..FUEL_SOME -> GameOver.INTEGRITY_ZERO_FUEL_SOME
                ship.fuel > FUEL_PLENTY -> GameOver.INTEGRITY_ZERO_FUEL_PLENTY
                ship.yearsTraveled < YEARS_FEW -> GameOver.INTEGRITY_ZERO_YEARS_FEW
                ship.yearsTraveled in YEARS_FEW..YEARS_SOME -> GameOver.INTEGRITY_ZERO_YEARS_SOME
                ship.yearsTraveled > YEARS_LOTS -> GameOver.INTEGRITY_ZERO_YEARS_LOTS
                ship.cryopods > CRYOPODS_SOME -> GameOver.INTEGRITY_ZERO_CRYOPODS_ENOUGH
                else -> GameOver.INTEGRITY_ZERO
            }

            // Ship ran out of fuel
            ship.fuel <= 0 -> when {
                ship.integrity >= INTEGRITY_HIGH && ship.materials >= MATERIALS_LOTS && ship.cryopods >= CRYOPODS_LOTS -> GameOver.FUEL_ZERO_INTEGRITY_ENOUGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING
                ship.materials >= MATERIALS_LOTS && ship.cryopods >= CRYOPODS_LOTS -> GameOver.FUEL_ZERO_MATERIALS_PLENTY_CRYOPODS_BUSTLING
                ship.cryopods < 1 -> GameOver.FUEL_ZERO_CRYOPODS_ZERO
                ship.cryopods == 1 -> GameOver.FUEL_ZERO_CRYOPODS_ONE
                ship.cryopods in 2..CRYOPODS_LOW -> GameOver.FUEL_ZERO_CRYOPODS_LOW
                ship.materials < 1 -> GameOver.FUEL_ZERO_MATERIALS_ZERO
                ship.materials in 1..MATERIALS_FEW -> GameOver.FUEL_ZERO_MATERIALS_LOW
                ship.integrity < INTEGRITY_LOW -> GameOver.FUEL_ZERO_INTEGRITY_LOW
                ship.yearsTraveled < YEARS_FEW -> GameOver.FUEL_ZERO_YEARS_FEW
                ship.yearsTraveled in YEARS_FEW..YEARS_SOME -> GameOver.FUEL_ZERO_YEARS_SOME
                ship.yearsTraveled > YEARS_LOTS -> GameOver.FUEL_ZERO_YEARS_LOTS
                ship.integrity > INTEGRITY_HIGH -> GameOver.FUEL_ZERO_INTEGRITY_PRISTINE
                ship.materials >= MATERIALS_LOTS -> GameOver.FUEL_ZERO_MATERIALS_ENOUGH
                ship.cryopods > CRYOPODS_SOME -> GameOver.FUEL_ZERO_CRYOPODS_ENOUGH
                ship.integrity in INTEGRITY_MID..INTEGRITY_HIGH -> GameOver.FUEL_ZERO_INTEGRITY_ENOUGH
                else -> GameOver.FUEL_ZERO
            }

            // Solar System Planets
            gameSession.settledPlanetId == "1mercury" -> GameOver.MERCURY
            gameSession.settledPlanetId == "2venus" -> GameOver.VENUS
            gameSession.settledPlanetId == "3earth" -> GameOver.EARTH
            gameSession.settledPlanetId == "4mars" -> GameOver.MARS
            gameSession.settledPlanetId == "5jupiter" -> GameOver.JUPITER
            gameSession.settledPlanetId == "6saturn" -> GameOver.SATURN
            gameSession.settledPlanetId == "7uranus" -> GameOver.URANUS
            gameSession.settledPlanetId == "8neptune" -> GameOver.NEPTUNE

            // Habitability
            gameSession.finalHabitability != null -> when (gameSession.finalHabitability) {
                // Deadly
                in 0.0..20.0 -> when {
                    ship.integrity < INTEGRITY_LOW -> GameOver.HABITABILITY_DEADLY_INTEGRITY_LOW
                    ship.materials >= MATERIALS_SOME && ship.integrity < INTEGRITY_MID -> GameOver.HABITABILITY_DEADLY_INTEGRITY_MID_LOW_MATERIALS_ENOUGH
                    ship.cryopods >= CRYOPODS_SOME -> GameOver.HABITABILITY_DEADLY_CRYOPODS_ENOUGH
                    else -> GameOver.HABITABILITY_DEADLY
                }

                // Very Low
                in 21.0..40.0 -> when {
                    ship.integrity < INTEGRITY_LOW && ship.cryopods > CRYOPODS_LOW -> GameOver.HABITABILITY_VERY_LOW_INTEGRITY_LOW
                    ship.cryopods >= CRYOPODS_SOME && ship.materials >= MATERIALS_SOME -> GameOver.HABITABILITY_VERY_LOW_CRYOPODS_MID_MATERIALS_ENOUGH
                    ship.cryopods >= CRYOPODS_LOW && ship.materials >= MATERIALS_SOME -> GameOver.HABITABILITY_VERY_LOW_CRYOPODS_ENOUGH_MATERIALS_ENOUGH
                    else -> GameOver.HABITABILITY_VERY_LOW
                }

                // Low
                in 41.0..60.0 -> when {
                    ship.materials >= MATERIALS_LOTS && ship.cryopods >= CRYOPODS_ENOUGH && ship.integrity >= INTEGRITY_HIGH -> GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_INTEGRITY_PRISTINE
                    ship.materials >= MATERIALS_LOTS && ship.cryopods >= CRYOPODS_ENOUGH && ship.fuel >= FUEL_PLENTY -> GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_FUEL_PLENTY
                    ship.materials >= MATERIALS_LOTS && ship.cryopods >= CRYOPODS_ENOUGH -> GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ENOUGH
                    ship.materials >= MATERIALS_LOTS && ship.cryopods in 1..CRYOPODS_ENOUGH -> GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_LOW
                    ship.materials >= MATERIALS_LOTS && ship.cryopods < 1 -> GameOver.HABITABILITY_LOW_MATERIALS_ENOUGH_CRYOPODS_ZERO
                    ship.materials < MATERIALS_LOTS && ship.cryopods >= CRYOPODS_ENOUGH -> GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ENOUGH
                    ship.materials < MATERIALS_LOTS && ship.cryopods in 1..CRYOPODS_ENOUGH -> GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_LOW
                    ship.materials < MATERIALS_LOTS && ship.cryopods < 1 -> GameOver.HABITABILITY_LOW_MATERIALS_LOW_CRYOPODS_ZERO
                    else -> GameOver.HABITABILITY_LOW
                }

                // Medium
                in 61.0..80.0 -> when {
                    ship.materials >= MATERIALS_SOME && ship.cryopods >= CRYOPODS_LOTS -> GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_BUSTLING
                    ship.materials >= MATERIALS_SOME && ship.cryopods >= CRYOPODS_SOME && ship.yearsTraveled > YEARS_LOTS -> GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS
                    ship.materials >= MATERIALS_SOME && ship.cryopods >= CRYOPODS_SOME -> GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ENOUGH
                    ship.materials >= MATERIALS_SOME && ship.cryopods in 1..CRYOPODS_SOME -> GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_LOW
                    ship.materials >= MATERIALS_SOME && ship.cryopods < 1 -> GameOver.HABITABILITY_MEDIUM_MATERIALS_ENOUGH_CRYOPODS_ZERO
                    ship.materials < MATERIALS_SOME && ship.cryopods >= CRYOPODS_SOME && ship.integrity >= INTEGRITY_HIGH -> GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH
                    ship.materials < MATERIALS_SOME && ship.cryopods >= CRYOPODS_SOME && ship.integrity < INTEGRITY_HIGH -> GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ENOUGH
                    ship.materials < MATERIALS_SOME && ship.cryopods in 1..CRYOPODS_SOME -> GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_LOW
                    ship.materials < MATERIALS_SOME && ship.cryopods < 1 -> GameOver.HABITABILITY_MEDIUM_MATERIALS_LOW_CRYOPODS_ZERO
                    else -> GameOver.HABITABILITY_MEDIUM
                }

                // High
                else -> when {
                    ship.materials >= MATERIALS_FEW && ship.cryopods >= CRYOPODS_LOW && ship.cryopods >= CRYOPODS_LOTS -> GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_BUSTLING
                    ship.materials >= MATERIALS_FEW && ship.cryopods >= CRYOPODS_LOW && ship.yearsTraveled > YEARS_LOTS -> GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH_YEARS_LOTS
                    ship.materials >= MATERIALS_FEW && ship.cryopods >= CRYOPODS_LOW -> GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ENOUGH
                    ship.materials >= MATERIALS_FEW && ship.cryopods in 1..CRYOPODS_LOW -> GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_LOW
                    ship.materials >= MATERIALS_FEW && ship.cryopods < 1 -> GameOver.HABITABILITY_HIGH_MATERIALS_ENOUGH_CRYOPODS_ZERO
                    ship.materials < MATERIALS_FEW && ship.cryopods >= CRYOPODS_LOW && ship.integrity >= INTEGRITY_MID -> GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH_INTEGRITY_ENOUGH
                    ship.materials < MATERIALS_FEW && ship.cryopods >= CRYOPODS_LOW && ship.integrity < INTEGRITY_MID -> GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ENOUGH
                    ship.materials < MATERIALS_FEW && ship.cryopods in 1..CRYOPODS_LOW -> GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_LOW
                    ship.materials < MATERIALS_FEW && ship.cryopods < 1 -> GameOver.HABITABILITY_HIGH_MATERIALS_LOW_CRYOPODS_ZERO
                    else -> GameOver.HABITABILITY_HIGH
                }
            }

            else -> GameOver.GAME_OVER
        }
    }

    override suspend fun isGameOver(gameSession: GameSession): Boolean = withContext(context = Dispatcher.Default) {
        gameSession.ship.integrity <= 0 || gameSession.ship.fuel <= 0 || gameSession.settledPlanetId != null
    }

    companion object {
        private const val CRYOPODS_MULTIPLIER = 10
        private const val FUEL_MULTIPLIER = 5
        private const val MATERIALS_MULTIPLIER = 2
        private const val YEARS_MULTIPLIER = 5
        private const val INTEGRITY_HIGH = 80
        private const val INTEGRITY_MID = 50
        private const val INTEGRITY_LOW = 20
        private const val YEARS_FEW = 1000.0
        private const val YEARS_SOME = 50000.0
        private const val YEARS_LOTS = 100000.0
        private const val CRYOPODS_LOW = 50
        private const val CRYOPODS_SOME = 100
        private const val CRYOPODS_ENOUGH = 200
        private const val CRYOPODS_LOTS = 500
        private const val FUEL_LOW = 10
        private const val FUEL_SOME = 100
        private const val FUEL_PLENTY = 300
        private const val MATERIALS_FEW = 50
        private const val MATERIALS_SOME = 100
        private const val MATERIALS_LOTS = 300
    }
}
