package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.error.ErrorState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.formula.Habitability
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

internal sealed interface GameAction {
    data class ChangeTab(val content: Content): GameAction
    data class Travel(val stellarHost: StellarHost): GameAction
    data class Settle(val planet: Planet): GameAction
}

internal data class GameState(
    val gameSession: GameSession? = null,
    val currentContent: Content = Content.SYSTEM,
    val stellarHosts: List<StellarHost> = emptyList(),
    val currentStellarHost: StellarHost? = null,
    val nearStellarHosts: List<StellarHost> = emptyList(),
    val visitedStellarHosts: Set<String> = emptySet(),
)

internal enum class Content {
    TRAVEL,
    SYSTEM,
    SHIP
}

internal class GameStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: GameState,
    private val shipUseCases: ShipUseCases,
    private val spaceUseCases: SpaceUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<GameAction, GameState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: missing game session"),
                    identifier = "GameStore:setup"
                )
            )
            return@launchInPipeline
        }

        val ship = shipUseCases.repairShip(ship = gameSession.ship)
        val updatedGameSession = gameSession.copy(ship = ship)
        gameSessionUseCases.updateGameSession(gameSession = updatedGameSession)

        if (gameSessionUseCases.isGameOver(gameSession = updatedGameSession)) {
            navigate(screen = Screen.GAME_OVER)
            return@launchInPipeline
        }

        val stellarHosts = spaceUseCases.getExoplanets()
        val currentStellarHost = if (updatedGameSession.currentStellarHostId == null) {
            stellarHosts.firstOrNull()
        } else stellarHosts.find { it.id == updatedGameSession.currentStellarHostId }
        if (currentStellarHost == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing stellar host")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: missing stellar host"),
                    identifier = "GameStore:setup"
                )
            )
            return@launchInPipeline
        }

        var visited = updatedGameSession.visitedStellarHosts.ifEmpty {
            stellarHosts.firstOrNull()?.let { setOf(it.id) }.orEmpty()
        }
        if (visited.isEmpty()) {
            Logger.error(tag = TAG, message = "Invalid state: empty visited")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: empty visited"),
                    identifier = "GameStore:setup"
                )
            )
            return@launchInPipeline
        }

        var nearStellarHosts = spaceUseCases.getNearestStars(
            stellarHost = currentStellarHost,
            stellarHosts = stellarHosts,
            n = ship.sensorRange,
            visited = visited
        )

        // Nowhere to go, clear visited and recalculate
        if (nearStellarHosts.isEmpty()) {
            visited = setOf(currentStellarHost.id)
            nearStellarHosts = spaceUseCases.getNearestStars(
                stellarHost = currentStellarHost,
                stellarHosts = stellarHosts,
                n = ship.sensorRange,
                visited = visited
            )
        }

        currentStellarHost.planets.forEach { planet ->
            planet.score = Habitability.calculateScores(
                stellarHost = currentStellarHost,
                planet = planet,
                formula = gameSession.formula
            )
        }

        updateState {
            it.copy(
                gameSession = updatedGameSession,
                stellarHosts = stellarHosts,
                currentStellarHost = currentStellarHost,
                nearStellarHosts = nearStellarHosts,
                visitedStellarHosts = visited
            )
        }
    }

    private fun travel(state: GameState, action: GameAction.Travel) = launchInPipeline {
        val stellarHost = state.stellarHosts.find { it.id == action.stellarHost.id }
        if (state.gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: missing game session"),
                    identifier = "GameStore:travel"
                )
            )
            return@launchInPipeline
        }
        if (stellarHost == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing current stellar host")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: missing current stellar host"),
                    identifier = "GameStore:travel"
                )
            )
            return@launchInPipeline
        }

        gameSessionUseCases.travel(gameSession = state.gameSession, stellarHost = stellarHost)

        // Hidden Cheat: If you go to the main menu in the event screen, you will circumvent the event
        navigate(screen = Screen.EVENT)
    }

    private fun settle(state: GameState, action: GameAction.Settle) = launchInPipeline {
        if (state.gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: missing game session"),
                    identifier = "GameStore:settle"
                )
            )
            return@launchInPipeline
        }

        gameSessionUseCases.settle(gameSession = state.gameSession, planet = action.planet)
        navigate(screen = Screen.GAME_OVER)
    }

    override fun setBackNavigation(state: GameState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: GameState, action: GameAction) {
        when (action) {
            is GameAction.ChangeTab -> updateState { it.copy(currentContent = action.content) }
            is GameAction.Travel -> travel(state = state, action = action)
            is GameAction.Settle -> settle(state = state, action = action)
        }
    }

    companion object {
        private const val TAG = "GameStore"
    }
}
