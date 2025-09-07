package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.formula.Habitability
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import kotlinx.coroutines.Job
import com.hybris.tlv.ui.screen.mainmenu.Content as MainMenuContent

internal sealed interface GameAction {
    data object Next: GameAction
    data class ChangeTab(val content: Content): GameAction
    data class Travel(val stellarHost: StellarHost): GameAction
    data class Settle(val planet: Planet): GameAction
}

internal data class GameState(
    val loading: Boolean = true,
    val tutorial: Tutorial = Tutorial.NO,
    val gameSession: GameSession? = null,
    val currentContent: Content = Content.SYSTEM,
    val currentStellarHost: StellarHost? = null,
    val nearStellarHosts: List<StellarHost> = emptyList(),
)

internal enum class Content {
    TUTORIAL,
    SHIP,
    SYSTEM,
    TRAVEL,
}

internal enum class Tutorial {
    NO,
    YES,
    SHIP,
    TRAVEL,
    SYSTEM,
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
        setup(state = stateFlow.value)
    }

    private fun setup(state: GameState): Job = launch {
        if (state.tutorial == Tutorial.YES) {
            updateState {
                it.copy(
                    loading = false,
                    currentContent = Content.TUTORIAL
                )
            }
            return@launch
        }

        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: missing game session"),
                    identifier = "GameStore:setup"
                )
            )
            return@launch
        }

        // Attempt to repair the ship if the integrity is critically low
        val ship = shipUseCases.repairShip(ship = gameSession.ship)
        val updatedGameSession = gameSession.copy(ship = ship)
        gameSessionUseCases.updateGameSession(gameSession = updatedGameSession)

        if (gameSessionUseCases.isGameOver(gameSession = updatedGameSession)) {
            navigate(screen = Screen.GAME_OVER)
            return@launch
        }

        // Get the current stellar host which the player is in
        val currentStellarHostId = updatedGameSession.currentStellarHostId ?: "sol"
        val currentStellarHost = spaceUseCases.getStellarHost(id = currentStellarHostId)
        if (currentStellarHost == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing stellar host")
            navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: missing stellar host"),
                    identifier = "GameStore:setup"
                )
            )
            return@launch
        }

        // Get and set the visited stellar hosts
        var visited = updatedGameSession.visitedStellarHosts.ifEmpty { setOf(currentStellarHostId) }
        // Get the stars nearest to the current stellar host by sensor range
        var nearStellarHosts = spaceUseCases.getNearestStars(
            stellarHost = currentStellarHost,
            n = ship.sensorRange,
            visited = visited
        )
        // Nowhere to go, clear visited and recalculate
        if (nearStellarHosts.isEmpty()) {
            // TODO - achievement here!
            visited = setOf(currentStellarHost.id)
            nearStellarHosts = spaceUseCases.getNearestStars(
                stellarHost = currentStellarHost,
                n = ship.sensorRange,
                visited = visited
            )
        }

        // Calculate habitability for each planet of the current stellar host
        currentStellarHost.planets.forEach { planet ->
            planet.score = Habitability.calculateScores(
                stellarHost = currentStellarHost,
                planet = planet,
                formula = gameSession.formula
            )
        }

        val finalUpdatedGameSession = updatedGameSession.copy(
            currentStellarHostId = currentStellarHostId,
            visitedStellarHosts = visited,
        )
        gameSessionUseCases.updateGameSession(gameSession = finalUpdatedGameSession)

        updateState {
            it.copy(
                loading = false,
                gameSession = finalUpdatedGameSession,
                currentStellarHost = currentStellarHost,
                nearStellarHosts = nearStellarHosts,
            )
        }
    }

    override fun back(state: GameState): () -> Unit = {
        navigate(
            screen = Screen.MAIN_MENU,
            state = if (state.tutorial == Tutorial.NO) null else MainMenuState(currentContent = MainMenuContent.LEARN_MENU)
        )
    }

    override fun reducer(state: GameState, action: GameAction) {
        when (action) {
            GameAction.Next -> tutorial(state = state)
            is GameAction.ChangeTab -> if (state.tutorial == Tutorial.NO) updateState { it.copy(currentContent = action.content) }
            is GameAction.Travel -> if (state.tutorial == Tutorial.NO) travel(state = state, action = action)
            is GameAction.Settle -> if (state.tutorial == Tutorial.NO) settle(state = state, action = action)
        }
    }

    private fun tutorial(state: GameState) =
        when (state.tutorial) {
            Tutorial.NO -> {}
            Tutorial.YES -> updateState { it.copy(tutorial = Tutorial.SHIP) }
            Tutorial.SHIP -> updateState { it.copy(tutorial = Tutorial.SYSTEM) }
            Tutorial.SYSTEM -> updateState { it.copy(tutorial = Tutorial.TRAVEL) }
            Tutorial.TRAVEL -> navigate(screen = Screen.MAIN_MENU)
        }

    private fun travel(state: GameState, action: GameAction.Travel): Job = launch {
        val stellarHost = state.nearStellarHosts.find { it.id == action.stellarHost.id }
        if (state.gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: missing game session"),
                    identifier = "GameStore:travel"
                )
            )
            return@launch
        }
        if (stellarHost == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing current stellar host")
            navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: missing current stellar host"),
                    identifier = "GameStore:travel"
                )
            )
            return@launch
        }

        gameSessionUseCases.travel(gameSession = state.gameSession, stellarHost = stellarHost)
        navigate(screen = Screen.EVENT)
    }

    private fun settle(state: GameState, action: GameAction.Settle): Job = launch {
        if (state.gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.GAME,
                    throwable = IllegalStateException("Invalid state: missing game session"),
                    identifier = "GameStore:settle"
                )
            )
            return@launch
        }

        gameSessionUseCases.settle(gameSession = state.gameSession, planet = action.planet)
        navigate(screen = Screen.GAME_OVER)
    }

    companion object {
        private const val TAG = "GameStore"
    }
}
