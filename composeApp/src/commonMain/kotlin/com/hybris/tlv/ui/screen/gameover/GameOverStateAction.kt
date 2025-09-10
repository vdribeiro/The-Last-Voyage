package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface GameOverAction {
    data object Continue: GameOverAction
}

internal data class GameOverState(
    val currentContent: Content? = null,
    val gameSession: GameSession? = null,
    val gameOver: GameOver? = null
)

internal enum class Content {
    MESSAGE,
    SCORE
}
