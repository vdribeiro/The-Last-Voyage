package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases

internal class AchievementStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    private val achievementUseCases: AchievementUseCases
): Store<AchievementAction, AchievementState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = AchievementState(
        loading = true,
        achievements = emptyList()
    )
) {
    init {
        setup()
    }

    private fun setup() = launch {
        val achievements = achievementUseCases.getAchievements()
        updateState {
            it.copy(
                loading = false,
                achievements = achievements
            )
        }
    }

    override fun back(state: AchievementState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }
}
