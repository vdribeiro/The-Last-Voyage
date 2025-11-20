package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.screen.achievement.AchievementScreen
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.achievementScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<Screen.Achievement, AchievementStore>(
    navController = navController,
    store = { AchievementStore(achievementUseCases = useCases.achievement) },
    screen = { AchievementScreen(store = it) }
)
