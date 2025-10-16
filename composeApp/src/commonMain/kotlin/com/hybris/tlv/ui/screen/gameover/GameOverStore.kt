package com.hybris.tlv.ui.screen.gameover

import kotlinx.coroutines.Job
import androidx.annotation.VisibleForTesting
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.translation.getTranslation

internal class GameOverStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: GameOverStateBuilder,
    private val gameSessionUseCases: GameSessionUseCases,
    private val achievementUseCases: AchievementUseCases
): Store<GameOverState, GameOverAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        GameOverStateBuilder.Default -> GameOverState()
        is GameOverStateBuilder.FromSavableState -> stateBuilder.state
    }
) {
    @get:VisibleForTesting
    internal val achievements: MutableList<Achievement> = mutableListOf()

    init {
        when (stateBuilder) {
            GameOverStateBuilder.Default -> setup()
            is GameOverStateBuilder.FromSavableState -> {
                achievements.addAll(elements = stateBuilder.achievements)
            }
        }
    }

    override fun getSavableState(state: GameOverState): Any? =
        GameOverStateBuilder.FromSavableState(state = state, achievements = achievements)

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

        Telemetry.info(tag = TAG, message = "Check achievements")
        this@GameOverStore.achievements.addAll(elements = achievementUseCases.updateAchievements(gameSession = updatedGameSession))

        updateState {
            it.copy(
                loading = false,
                gameSession = updatedGameSession,
                gameOver = gameOver
            )
        }
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun next(): Job = launch {
        val newAchievementTranslation = getTranslation(key = "achievements_screen__new")
        val achievements = achievements.map { achievement -> "$newAchievementTranslation: ${getTranslation(key = achievement.id)}" }
        updateState {
            it.copy(
                currentContent = Content.SCORE,
                achievements = achievements
            )
        }
    }

    override fun goBack(state: GameOverState) {}

    override fun reducer(state: GameOverState, action: GameOverAction) {
        when (action) {
            GameOverAction.Next -> when (state.currentContent) {
                Content.MESSAGE -> next()
                Content.SCORE -> navigate(screen = Screen.MainMenu)
            }
        }
    }

    companion object {
        private const val TAG = "GameOverStore"
    }
}
