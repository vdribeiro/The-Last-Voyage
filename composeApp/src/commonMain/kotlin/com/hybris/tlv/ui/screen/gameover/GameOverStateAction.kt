package com.hybris.tlv.ui.screen.gameover

import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface GameOverAction {
    data object Next: GameOverAction
    data object NextAchievement: GameOverAction
}

internal sealed interface GameOverStateBuilder {
    data object Default: GameOverStateBuilder
    data class FromState(
        val state: GameOverState,
        val achievements: List<Achievement>,
        val index: Int
    ): GameOverStateBuilder
}

internal data class GameOverState(
    val loading: Boolean = true,
    val currentContent: Content = Content.MESSAGE,
    val gameSession: GameSession? = null,
    val gameOver: GameOver? = null,
    val achievement: Achievement? = null,
)

internal enum class Content {
    MESSAGE,
    SCORE
}
