package com.hybris.tlv.ui.screen.game

import kotlinx.coroutines.Job
import androidx.annotation.VisibleForTesting
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.EventScreen
import com.hybris.tlv.ui.navigation.GameOverScreen
import com.hybris.tlv.ui.navigation.MainMenuScreen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.formula.Habitability

internal class GameStore(
    ship: Ship?,
    private val shipUseCases: ShipUseCases,
    private val spaceUseCases: SpaceUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<GameState, GameAction>(
    initialState = GameState(ship = ship)
) {
    @get:VisibleForTesting
    internal var gameSession: GameSession? = null

    init {
        setup()
    }

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            feedback(tag = TAG, message = "Invalid state: missing game session on setup()")
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Attempt to repair the ship if the integrity is critically low")
        val ship = shipUseCases.repairShip(ship = gameSession.ship)
        val updatedGameSession = gameSession.copy(ship = ship)
        gameSessionUseCases.updateGameSession(gameSession = updatedGameSession)

        if (gameSessionUseCases.isGameOver(gameSession = updatedGameSession)) {
            navigate(screen = GameOverScreen)
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Get the current stellar host the player is in")
        val currentStellarHostId = updatedGameSession.currentStellarHostId ?: "sol"
        Telemetry.info(tag = TAG, message = "Calculate the habitability for each planet of the current stellar host")
        val currentStellarHost = spaceUseCases.getStellarHost(id = currentStellarHostId)?.apply {
            planets.forEach { planet ->
                planet.score = Habitability.calculateScores(
                    stellarHost = this,
                    planet = planet,
                    formula = gameSession.formula
                )
            }
        }
        if (currentStellarHost == null) {
            feedback(tag = TAG, message = "Invalid state: missing stellar host on setup()")
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

    private fun travel(state: GameState, action: GameAction.Travel): Job = launch {
        Telemetry.info(tag = TAG, message = "Travelled to ${action.stellarHost}")
        val gameSession = this@GameStore.gameSession
        if (gameSession == null) {
            feedback(tag = TAG, message = "Invalid state: missing game session on travel()")
            return@launch
        }

        val stellarHost = state.nearStellarHosts.find { it.id == action.stellarHost.id }
        if (stellarHost == null) {
            feedback(tag = TAG, message = "Invalid state: missing stellar host on travel()")
            return@launch
        }

        this@GameStore.gameSession = gameSessionUseCases.travel(gameSession = gameSession, stellarHost = stellarHost)
        navigate(screen = EventScreen(ship = gameSession.ship))
    }

    private fun settle(action: GameAction.Settle): Job = launch {
        Telemetry.info(tag = TAG, message = "Settled on ${action.planet}")
        val gameSession = this@GameStore.gameSession
        if (gameSession == null) {
            feedback(tag = TAG, message = "Invalid state: missing game session on settle()")
            return@launch
        }

        this@GameStore.gameSession = gameSessionUseCases.settle(gameSession = gameSession, planet = action.planet)
        navigate(screen = GameOverScreen)
    }

    override fun back(state: GameState) {
        navigate(screen = MainMenuScreen)
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
