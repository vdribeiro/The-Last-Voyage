package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.achievement.AchievementScreen
import com.hybris.tlv.ui.screen.achievement.AchievementStore

internal fun NavGraphBuilder.achievementScreen(
    useCases: UseCases
) = composable<Screen.Achievement> {
    AchievementScreen(store = viewModel {
        AchievementStore(achievementUseCases = useCases.achievement)
    })
}
