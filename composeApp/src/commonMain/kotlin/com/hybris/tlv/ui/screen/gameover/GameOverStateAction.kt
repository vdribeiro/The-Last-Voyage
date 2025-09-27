package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface GameOverAction {
    data object Continue: GameOverAction
}

internal sealed interface GameOverStateBuilder {
    data object Default: GameOverStateBuilder
    data class FromState(val state: GameOverState): GameOverStateBuilder
}

internal data class GameOverState(
    val loading: Boolean = true,
    val currentContent: Content = Content.MESSAGE,
    val gameSession: GameSession? = null,
    val gameOver: GameOver? = null
)

internal enum class Content {
    MESSAGE,
    SCORE
}
