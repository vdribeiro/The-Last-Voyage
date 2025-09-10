package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.formula.Habitability
import kotlinx.coroutines.Job
import com.hybris.tlv.ui.screen.mainmenu.Content as MainMenuContent

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
    override fun setup(state: GameState): Job = launch {
        val loading = state.loading ?: false
        val tutorial = state.tutorial ?: Tutorial.NO
        val currentContent = state.currentContent ?: Content.SYSTEM

        val gameSession = state.gameSession ?: gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing game session on setup()"))
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
        // Calculate habitability for each planet of the current stellar host
        val currentStellarHost = state.currentStellarHost ?: spaceUseCases.getStellarHost(id = currentStellarHostId)?.apply {
            planets.forEach { planet ->
                planet.score = Habitability.calculateScores(
                    stellarHost = this,
                    planet = planet,
                    formula = gameSession.formula
                )
            }
        }
        if (currentStellarHost == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing stellar host on setup()"))
            return@launch
        }

        // Get and set the visited stellar hosts
        var visited = updatedGameSession.visitedStellarHosts.ifEmpty { setOf(currentStellarHostId) }
        // Get the stars nearest to the current stellar host by sensor range
        val nearStellarHosts = state.nearStellarHosts ?: spaceUseCases.getNearestStars(
            stellarHost = currentStellarHost,
            n = ship.sensorRange,
            visited = visited
        ).ifEmpty {
            // Nowhere to go, clear visited and recalculate
            // TODO - achievement here!
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

        updateState {
            it.copy(
                loading = loading,
                tutorial = tutorial,
                currentContent = currentContent,
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
            GameAction.NextTutorial -> tutorial(state = state)
            is GameAction.ChangeTab -> if (state.tutorial == Tutorial.NO) updateState { it.copy(currentContent = action.content) }
            is GameAction.Travel -> travel(state = state, action = action)
            is GameAction.Settle -> settle(state = state, action = action)
        }
    }

    private fun tutorial(state: GameState) =
        when (state.tutorial) {
            null, Tutorial.NO -> {}
            Tutorial.YES -> updateState { it.copy(tutorial = Tutorial.SHIP) }
            Tutorial.SHIP -> updateState { it.copy(tutorial = Tutorial.SYSTEM) }
            Tutorial.SYSTEM -> updateState { it.copy(tutorial = Tutorial.TRAVEL) }
            Tutorial.TRAVEL -> navigate(screen = Screen.MAIN_MENU)
        }

    private fun travel(state: GameState, action: GameAction.Travel): Job = launch {
        if (state.tutorial != Tutorial.NO) return@launch

        if (state.gameSession == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing game session on travel()"))
            return@launch
        }

        val stellarHost = state.nearStellarHosts?.find { it.id == action.stellarHost.id }
        if (stellarHost == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing stellar host on travel()"))
            return@launch
        }

        gameSessionUseCases.travel(gameSession = state.gameSession, stellarHost = stellarHost)
        navigate(screen = Screen.EVENT)
    }

    private fun settle(state: GameState, action: GameAction.Settle): Job = launch {
        if (state.tutorial != Tutorial.NO) return@launch

        if (state.gameSession == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing game session on settle()"))
            return@launch
        }

        gameSessionUseCases.settle(gameSession = state.gameSession, planet = action.planet)
        navigate(screen = Screen.GAME_OVER)
    }

    companion object {
        private const val TAG = "GameStore"
    }
}
