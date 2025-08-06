package com.hybris.tlv.ui.screen.achievement

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.achievement.model.Achievement

internal sealed interface AchievementAction {
    data object Back: AchievementAction
}

internal data class AchievementState(
    val achievements: List<Achievement> = emptyList()
)

internal class AchievementStore(
    dispatcher: Dispatcher,
    navigation: Navigation,
    initialState: AchievementState,
    private val achievementUseCases: AchievementUseCases
): Store<AchievementAction, AchievementState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val achievements = achievementUseCases.getAchievements()
        updateState { it.copy(achievements = achievements) }
    }

    override fun reducer(state: AchievementState, action: AchievementAction) {
        when (action) {
            AchievementAction.Back -> navigate(screen = Navigation.Screen.MAIN_MENU)
        }
    }
}
