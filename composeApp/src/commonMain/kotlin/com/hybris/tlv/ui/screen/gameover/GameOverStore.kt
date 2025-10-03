package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import kotlinx.coroutines.Job

internal class GameOverStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: GameOverStateBuilder,
    private val gameSessionUseCases: GameSessionUseCases
): Store<GameOverState, GameOverAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        GameOverStateBuilder.Default -> GameOverState()
        is GameOverStateBuilder.FromSavableState -> stateBuilder.state
    }
) {
    init {
        when (stateBuilder) {
            GameOverStateBuilder.Default -> setup()
            is GameOverStateBuilder.FromSavableState -> {}
        }
    }

    override fun getSavableState(state: GameOverState): Any? =
        GameOverStateBuilder.FromSavableState(state = state)

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            error(tag = TAG, message = "Invalid state: missing game session on setup()")
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Get game over")
        val gameOver = gameSessionUseCases.getGameOver(gameSession = gameSession)
        val updatedGameSession = gameSessionUseCases.score(
            gameSession = gameSession,
            gameOver = gameOver
        ).let { it.copy(utc = getLocalDateTime(utc = it.utc)) }

        updateState {
            it.copy(
                loading = false,
                gameSession = updatedGameSession,
                gameOver = gameOver
            )
        }
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    override fun goBack(state: GameOverState) {}

    override fun reducer(state: GameOverState, action: GameOverAction) {
        when (action) {
            GameOverAction.Continue -> when (state.currentContent) {
                Content.MESSAGE -> updateState { it.copy(currentContent = Content.SCORE) }
                Content.SCORE -> navigate(screen = Screen.MainMenu)
            }
        }
    }

    companion object {
        private const val TAG = "GameOverStore"
    }
}
