package com.hybris.tlv.ui.screen.game

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.formula.Habitability
import kotlinx.coroutines.Job

internal class GameStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: GameStateBuilder,
    private val shipUseCases: ShipUseCases,
    private val spaceUseCases: SpaceUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<GameState, GameAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        GameStateBuilder.Default -> GameState()
        is GameStateBuilder.FromState -> stateBuilder.state
    }
) {
    @get:VisibleForTesting
    internal var gameSession: GameSession? = null

    init {
        when (stateBuilder) {
            GameStateBuilder.Default -> setup()
            is GameStateBuilder.FromState -> gameSession = stateBuilder.gameSession
        }
    }

    override fun getSavableState(state: GameState): Any? =
        GameStateBuilder.FromState(state = state, gameSession = gameSession)

    private fun setup(): Job = launch {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            navigate(screen = Screen.Feedback, stateBuilder = FeedbackStateBuilder.Error(tag = TAG, message = "Invalid state: missing game session on setup()"))
            return@launch
        }

        // Attempt to repair the ship if the integrity is critically low
        val ship = shipUseCases.repairShip(ship = gameSession.ship)
        val updatedGameSession = gameSession.copy(ship = ship)
        gameSessionUseCases.updateGameSession(gameSession = updatedGameSession)

        if (gameSessionUseCases.isGameOver(gameSession = updatedGameSession)) {
            navigate(screen = Screen.GameOver)
            return@launch
        }

        // Get the current stellar host which the player is in
        val currentStellarHostId = updatedGameSession.currentStellarHostId ?: "sol"
        // Calculate habitability for each planet of the current stellar host
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
            navigate(screen = Screen.Feedback, stateBuilder = FeedbackStateBuilder.Error(tag = TAG, message = "Invalid state: missing stellar host on setup()"))
            return@launch
        }

        // Get and set the visited stellar hosts
        var visited = updatedGameSession.visitedStellarHosts.ifEmpty { setOf(currentStellarHostId) }
        // Get the stars nearest to the current stellar host by sensor range
        val nearStellarHosts = spaceUseCases.getNearestStars(
            stellarHost = currentStellarHost,
            n = ship.sensorRange,
            visited = visited
        ).ifEmpty {
            // Nowhere to go, clear visited and recalculate
            // TODO: achievement here!
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
        gameSessionUseCases.updateGameSession(gameSession = finalUpdatedGameSession)

        this@GameStore.gameSession = finalUpdatedGameSession
        updateState {
            it.copy(
                loading = false,
                ship = finalUpdatedGameSession.ship,
                currentStellarHost = currentStellarHost,
                nearStellarHosts = nearStellarHosts,
            )
        }
    }

    private fun travel(state: GameState, action: GameAction.Travel): Job = launch {
        val gameSession = this@GameStore.gameSession
        if (gameSession == null) {
            navigate(screen = Screen.Feedback, stateBuilder = FeedbackStateBuilder.Error(tag = TAG, message = "Invalid state: missing game session on travel()"))
            return@launch
        }

        val stellarHost = state.nearStellarHosts.find { it.id == action.stellarHost.id }
        if (stellarHost == null) {
            navigate(screen = Screen.Feedback, stateBuilder = FeedbackStateBuilder.Error(tag = TAG, message = "Invalid state: missing stellar host on travel()"))
            return@launch
        }

        this@GameStore.gameSession = gameSessionUseCases.travel(gameSession = gameSession, stellarHost = stellarHost)
        navigate(screen = Screen.Event)
    }

    private fun settle(action: GameAction.Settle): Job = launch {
        val gameSession = this@GameStore.gameSession
        if (gameSession == null) {
            navigate(screen = Screen.Feedback, stateBuilder = FeedbackStateBuilder.Error(tag = TAG, message = "Invalid state: missing game session on settle()"))
            return@launch
        }

        this@GameStore.gameSession = gameSessionUseCases.settle(gameSession = gameSession, planet = action.planet)
        navigate(screen = Screen.GameOver)
    }

    override fun goBack(state: GameState): () -> Unit = {
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
