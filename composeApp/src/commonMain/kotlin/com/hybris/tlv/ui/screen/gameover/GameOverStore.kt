package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession
import kotlinx.coroutines.Job

internal sealed interface GameOverAction {
    data object Continue: GameOverAction
}

internal data class GameOverState(
    val currentContent: Content = Content.MESSAGE,
    val gameSession: GameSession? = null,
    val gameOverMessage: String? = null
)

internal enum class Content {
    MESSAGE,
    SCORE
}

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
    init {
        setup()
    }

    private fun setup(): Job = launch {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.GAME_OVER,
                    throwable = IllegalStateException("Invalid state: missing game session"),
                    identifier = "GameOverStore:setup"
                )
            )
            return@launch
        }

        val gameOver = gameSessionUseCases.getGameOver(gameSession = gameSession)
        val updatedGameSession = gameSessionUseCases.score(gameSession = gameSession, gameOver = gameOver)

        updateState {
            it.copy(
                gameSession = updatedGameSession.copy(utc = getLocalDateTime(utc = updatedGameSession.utc)),
                gameOverMessage = gameOver.displayName
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
