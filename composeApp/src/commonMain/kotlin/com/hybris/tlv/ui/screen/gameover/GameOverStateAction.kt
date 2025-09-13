package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface GameOverAction {
    data object Continue: GameOverAction
}

internal data class GameOverState(
    val loading: Boolean,
    val currentContent: Content,
    val gameSession: GameSession?,
    val gameOver: GameOver?
)

internal enum class Content {
    MESSAGE,
    SCORE
}
