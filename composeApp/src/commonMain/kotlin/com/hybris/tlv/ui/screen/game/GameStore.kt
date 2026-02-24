package com.hybris.tlv.ui.screen.game

import kotlin.concurrent.Volatile
import kotlinx.coroutines.Job
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.domain.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.domain.usecase.gamesession.model.GameSession
import com.hybris.tlv.domain.usecase.ship.ShipUseCases
import com.hybris.tlv.domain.usecase.ship.model.Ship
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MAX_CRYOPODS
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MAX_FUEL
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MAX_INTEGRITY
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MAX_MATERIALS
import com.hybris.tlv.domain.usecase.space.SUN
import com.hybris.tlv.domain.usecase.space.SpaceUseCases
import com.hybris.tlv.domain.usecase.space.formula.Habitability
import com.hybris.tlv.test.VisibleForTesting
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Store

internal class GameStore(
    ship: Ship?,
    private val config: ConfigManager,
    private val shipUseCases: ShipUseCases,
    private val spaceUseCases: SpaceUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<GameState, GameAction>(
    initialState = GameState(ship = ship)
) {
    @VisibleForTesting
    @Volatile
    internal var gameSession: GameSession? = null

    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing game session on setup()"))
            return@launch
        }

        val preferences = config.preferences
        Telemetry.info(tag = TAG, message = "Attempt to repair the ship if the integrity is critically low")
        val ship = shipUseCases.repairShip(ship = gameSession.ship).let { ship ->
            ship.copy(
                sensorRange = if (preferences.cheatSensorRange) 100 else ship.sensorRange,
                integrity = if (preferences.cheatIntegrity) MAX_INTEGRITY else ship.integrity,
                materials = if (preferences.cheatMaterials) MAX_MATERIALS else ship.materials,
                fuel = if (preferences.cheatFuel) MAX_FUEL else ship.fuel,
                cryopods = if (preferences.cheatCryopods) MAX_CRYOPODS else ship.cryopods
            )
        }

        val updatedGameSession = gameSession.copy(ship = ship)
        gameSessionUseCases.updateGameSession(gameSession = updatedGameSession)

        if (gameSessionUseCases.isGameOver(gameSession = updatedGameSession)) {
            navigate(screen = Screen.GameOver)
            return@launch
        }
        Telemetry.info(tag = TAG, message = "Ship repaired: $ship")

        val currentStellarHostId = updatedGameSession.currentStellarHostId ?: SUN
        Telemetry.info(tag = TAG, message = "Current stellar host the player is in: $currentStellarHostId")

        Telemetry.info(tag = TAG, message = "Calculate the habitability for each planet of the current stellar host")
        val currentStellarHost = spaceUseCases.getStellarHost(id = currentStellarHostId)?.apply {
            planets.forEach { planet ->
                planet.score = Habitability.calculateScores(
                    stellarHost = this,
                    planet = planet,
                    formula = gameSession.formula
                )
                // Special case: Earth is inhabitable in-game
                if (planet.id == "3earth") planet.score = planet.score?.copy(habitabilityScore = 0.0)
            }
        }
        if (currentStellarHost == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing stellar host on setup()"))
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Get and set the visited stellar hosts")
        var visited = updatedGameSession.visitedStellarHosts.ifEmpty { setOf(currentStellarHostId) }

        Telemetry.info(tag = TAG, message = "Get the stars nearest to the current stellar host by sensor range")
        val nearStellarHosts = spaceUseCases.getNearestStars(
            stellarHost = currentStellarHost,
            n = ship.sensorRange,
            visited = visited
        ).ifEmpty {
            Telemetry.info(tag = TAG, message = "Nowhere to go, clear visited hosts and recalculate")
            visited = setOf(currentStellarHost.id)
            spaceUseCases.getNearestStars(
                stellarHost = currentStellarHost,
                n = ship.sensorRange,
                visited = visited
            )
        }

        val finalUpdatedGameSession = updatedGameSession.copy(
            currentStellarHostId = currentStellarHostId,
            visitedStellarHosts = visited,
        )

        this@GameStore.gameSession = gameSessionUseCases.updateGameSession(gameSession = finalUpdatedGameSession)
        updateState {
            it.copy(
                loading = false,
                ship = finalUpdatedGameSession.ship,
                currentStellarHost = currentStellarHost,
                nearStellarHosts = nearStellarHosts,
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun travel(state: GameState, action: GameAction.Travel): Job = launch(id = "travel") {
        Telemetry.info(tag = TAG, message = "Travelled to ${action.stellarHost}")

        val gameSession = this@GameStore.gameSession
        if (gameSession == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing game session on travel()"))
            return@launch
        }

        val stellarHost = state.nearStellarHosts.find { it.id == action.stellarHost.id }
        if (stellarHost == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing stellar host on travel()"))
            return@launch
        }

        this@GameStore.gameSession = gameSessionUseCases.travel(gameSession = gameSession, stellarHost = stellarHost)
        navigate(screen = Screen.Event(ship = gameSession.ship))
    }

    private fun settle(action: GameAction.Settle): Job = launch(id = "settle") {
        Telemetry.info(tag = TAG, message = "Settled on ${action.planet}")
        val gameSession = this@GameStore.gameSession
        if (gameSession == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing game session on settle()"))
            return@launch
        }

        this@GameStore.gameSession = gameSessionUseCases.settle(gameSession = gameSession, planet = action.planet)
        navigate(screen = Screen.GameOver)
    }

    override fun navigateBack(state: GameState) {
        navigate(screen = Screen.MainMenu)
    }

    override fun reducer(state: GameState, action: GameAction) {
        when (action) {
            is GameAction.ChangeTab -> updateState { it.copy(currentContent = action.content) }
            is GameAction.Travel -> travel(state = state, action = action)
            is GameAction.Settle -> settle(action = action)
        }
    }

    companion object {
        private const val TAG = "GameStore"
    }
}
