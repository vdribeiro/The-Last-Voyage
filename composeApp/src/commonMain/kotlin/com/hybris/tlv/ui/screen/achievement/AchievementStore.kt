package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import kotlinx.coroutines.Job

internal class AchievementStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: AchievementStateBuilder,
    private val achievementUseCases: AchievementUseCases
): Store<AchievementState, Unit>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        AchievementStateBuilder.Default -> AchievementState()
    }
) {
    init {
        when (stateBuilder) {
            AchievementStateBuilder.Default -> setup()
        }
    }

    private fun setup(): Job = launch {
        val achievements = achievementUseCases.getAchievements()
        updateState {
            it.copy(
                loading = false,
                achievements = achievements
            )
        }
    }

    override fun back(state: AchievementState): () -> Unit = {
        navigate(screen = Screen.MainMenu)
    }
}
