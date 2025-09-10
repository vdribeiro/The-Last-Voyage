package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases

internal class AchievementStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: AchievementState,
    private val achievementUseCases: AchievementUseCases
): Store<AchievementAction, AchievementState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    override fun setup(state: AchievementState) = launch {
        val achievements = state.achievements ?: achievementUseCases.getAchievements()
        updateState { it.copy(achievements = achievements) }
    }

    override fun back(state: AchievementState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: AchievementState, action: AchievementAction) {}
}
