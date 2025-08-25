package com.hybris.tlv.usecase.gamesession.model

import com.hybris.tlv.usecase.ship.model.Ship

internal sealed class GameOver(val multiplier: Double) {

    /**
     * The ship is destroyed.
     */
    data class IntegrityFailure(val ship: Ship): GameOver(multiplier = 0.1)

    /**
     * The ship ran out of fuel.
     */
    data class FuelFailure(val ship: Ship): GameOver(multiplier = 0.1)

    /**
     * Settled on the solar system.
     */
    data class PlanetSettlement(val planetId: String): GameOver(multiplier = 0.5)

    /**
     * Settled on an exoplanet.
     */
    data class ExoplanetSettlement(
        val habitability: Double,
        val ship: Ship,
        private val calculatedMultiplier: Double
    ): GameOver(multiplier = calculatedMultiplier)

    /**
     * Fallback state.
     */
    object Generic: GameOver(multiplier = 0.0)

    companion object Companion {
        private val SPECIAL_PLANETS = setOf(
            "1mercury", "2venus", "3earth", "4mars",
            "5jupiter", "6saturn", "7uranus", "8neptune"
        )

        fun from(gameSession: GameSession): GameOver =
            when {
                gameSession.ship.integrity <= 0 -> IntegrityFailure(ship = gameSession.ship)
                gameSession.ship.fuel <= 0 -> FuelFailure(ship = gameSession.ship)
                gameSession.settledPlanetId in SPECIAL_PLANETS -> PlanetSettlement(planetId = gameSession.settledPlanetId!!)
                gameSession.finalHabitability != null -> createSettlementSuccess(
                    habitability = gameSession.finalHabitability,
                    ship = gameSession.ship
                )

                else -> Generic
            }

        private fun createSettlementSuccess(habitability: Double, ship: Ship): ExoplanetSettlement {
            val habitabilityMultiplier = when (habitability) {
                in 0.0..20.0 -> 0.25
                in 21.0..40.0 -> 0.50
                in 41.0..60.0 -> 1.0
                in 61.0..80.0 -> 1.2
                else -> 1.5
            }

            val successMultiplier = when {
                habitability > 80.0 -> when {
                    ship.materials >= 50 && ship.cryopods >= 50 -> 1.0
                    ship.materials < 50 && ship.cryopods >= 50 && ship.integrity >= 50 -> 0.75
                    ship.materials < 50 && ship.cryopods >= 50 -> 0.5
                    else -> 0.25
                }
                habitability in 61.0..80.0 -> when {
                    ship.materials >= 100 && ship.cryopods >= 100 -> 1.0
                    ship.materials < 100 && ship.cryopods >= 100 && ship.integrity >= 75 -> 0.75
                    ship.materials < 100 && ship.cryopods >= 100 -> 0.5
                    else -> 0.25
                }
                habitability in 41.0..60.0 -> when {
                    ship.materials >= 300 && ship.cryopods >= 150 -> 1.0
                    else -> 0.25
                }
                else -> 0.25
            }

            val finalMultiplier = habitabilityMultiplier * successMultiplier
            return ExoplanetSettlement(habitability = habitability, ship = ship, calculatedMultiplier = finalMultiplier)
        }
    }
}
