package com.hybris.tlv.ui.screen.gameover

import kotlinx.coroutines.Job
import androidx.annotation.VisibleForTesting
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.MainMenu
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases

internal class GameOverStore(
    config: ConfigManager,
    private val gameSessionUseCases: GameSessionUseCases,
    private val achievementUseCases: AchievementUseCases
): Store<GameOverState, GameOverAction>(
    config = config,
    initialState = GameOverState()
) {
    @get:VisibleForTesting
    internal var achievements: List<Achievement>? = null
    @get:VisibleForTesting
    internal var index: Int = 0

    init {
        setup()
    }

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            feedback(tag = TAG, message = "Invalid state: missing game session on setup()")
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

    override fun back(state: GameOverState) {}

    override fun reducer(state: GameOverState, action: GameOverAction) {
        when (action) {
            GameOverAction.Next -> when (state.currentContent) {
                Content.MESSAGE -> updateState {
                    it.copy(
                        currentContent = Content.SCORE,
                        achievement = achievements?.getOrNull(index = index)
                    )
                }

                Content.SCORE -> navigate(screen = Screen.MainMenu)
            }

            GameOverAction.NextAchievement -> {
                index++
                updateState { it.copy(achievement = achievements?.getOrNull(index = index)) }
            }
        }
    }

    companion object {
        private const val TAG = "GameOverStore"
    }
}
