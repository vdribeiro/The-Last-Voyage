package com.hybris.tlv.ui.screen.gameover

import kotlinx.coroutines.Job
import com.hybris.tlv.core.locale.getLocalDateTime
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.achievement.AchievementUseCases
import com.hybris.tlv.domain.usecase.achievement.model.Achievement
import com.hybris.tlv.domain.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.test.VisibleOnlyForTesting
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Store

internal class GameOverStore(
    private val gameSessionUseCases: GameSessionUseCases,
    private val achievementUseCases: AchievementUseCases
): Store<GameOverState, GameOverAction>(
    initialState = GameOverState()
) {
    @VisibleOnlyForTesting
    internal var achievements: List<Achievement> = emptyList()
    @VisibleOnlyForTesting
    internal var index: Int = 0

    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing game session on setup()"))
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Get game over")
        val gameOver = gameSessionUseCases.getGameOver(gameSession = gameSession)
        val updatedGameSession = gameSessionUseCases.score(
            gameSession = gameSession,
            gameOver = gameOver
        ).let { it.copy(utc = getLocalDateTime(utc = it.utc)) }

        Telemetry.info(tag = TAG, message = "Check achievements")
        val achievements = achievementUseCases.updateAchievements(gameSession = updatedGameSession)

        this@GameOverStore.achievements = achievements
        updateState {
            it.copy(
                loading = false,
                gameSession = updatedGameSession,
                gameOver = gameOver,
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun nextContent(state: GameOverState) {
        when (state.currentContent) {
            Content.MESSAGE -> updateState {
                it.copy(
                    currentContent = Content.SCORE,
                    achievement = achievements.firstOrNull()
                )
            }

            Content.SCORE -> navigate(screen = Screen.MainMenu)
        }
    }

    private fun nextAchievement(): Job = launch {
        index++
        val achievement = achievements.getOrNull(index = index)
        updateState { it.copy(achievement = achievement) }
    }

    override fun back(state: GameOverState) {}

    override fun reducer(state: GameOverState, action: GameOverAction) {
        when (action) {
            GameOverAction.Next -> nextContent(state = state)
            GameOverAction.NextAchievement -> nextAchievement()
        }
    }

    companion object {
        private const val TAG = "GameOverStore"
    }
}
