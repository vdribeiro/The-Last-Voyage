package com.hybris.tlv.ui.screen.achievement

import kotlinx.coroutines.Job
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases

internal class AchievementStore(
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: AchievementStateBuilder,
    private val achievementUseCases: AchievementUseCases
): Store<AchievementState, Unit>(
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        AchievementStateBuilder.Default -> AchievementState()
        is AchievementStateBuilder.FromState -> stateBuilder.state
    }
) {
    init {
        when (stateBuilder) {
            AchievementStateBuilder.Default -> setup()
            is AchievementStateBuilder.FromState -> {}
        }
    }

    override fun getSavableState(state: AchievementState): Any =
        AchievementStateBuilder.FromState(state = state)

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val achievements = achievementUseCases.getAchievements()
        updateState {
            it.copy(
                loading = false,
                achievements = achievements
            )
        }
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    companion object {
        private const val TAG = "AchievementStore"
    }
}
