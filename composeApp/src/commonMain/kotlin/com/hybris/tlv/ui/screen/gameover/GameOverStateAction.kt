package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal data class GameOverState(
    val currentContent: Content = Content.MESSAGE,
    val gameSession: GameSession? = null,
    val gameOver: GameOver? = null
)

internal enum class Content {
    MESSAGE,
    SCORE
}

internal sealed interface GameOverAction {
    data object Continue: GameOverAction
}
