package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import kotlinx.coroutines.Job

internal class GameOverStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: GameOverState,
    private val gameSessionUseCases: GameSessionUseCases
): Store<GameOverAction, GameOverState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    override fun setup(state: GameOverState): Job = launch {
        val gameSession = state.gameSession ?: gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.GAME_OVER,
                    message = IllegalStateException("Invalid state: missing game session"),
                    tag = "GameOverStore:setup"
                )
            )
            return@launch
        }

        val currentContent = state.currentContent
        val gameOver = state.gameOver ?: gameSessionUseCases.getGameOver(gameSession = gameSession)
        val updatedGameSession = state.gameSession ?: gameSessionUseCases.score(
            gameSession = gameSession,
            gameOver = gameOver
        ).let { it.copy(utc = getLocalDateTime(utc = it.utc)) }

        updateState {
            it.copy(
                currentContent = currentContent,
                gameSession = updatedGameSession,
                gameOver = gameOver
            )
        }
    }

    override fun back(state: GameOverState): () -> Unit = {
        when (state.currentContent) {
            Content.MESSAGE -> {}
            Content.SCORE -> navigate(screen = Screen.MAIN_MENU)
        }
    }

    override fun reducer(state: GameOverState, action: GameOverAction) {
        when (action) {
            GameOverAction.Continue -> when (state.currentContent) {
                Content.MESSAGE -> updateState { it.copy(currentContent = Content.SCORE) }
                Content.SCORE -> navigate(screen = Screen.MAIN_MENU)
            }
        }
    }

    companion object {
        private const val TAG = "GameOverStore"
    }
}
