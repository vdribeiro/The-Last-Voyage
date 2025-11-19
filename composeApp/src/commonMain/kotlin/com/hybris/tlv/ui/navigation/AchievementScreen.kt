package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.achievement.AchievementScreen
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.achievementScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<AchievementScreen, AchievementStore>(
    navController = navController,
    store = { AchievementStore(achievementUseCases = useCases.achievement) },
    screen = { AchievementScreen(store = it) }
)

@Serializable
internal data object AchievementScreen: Screen
