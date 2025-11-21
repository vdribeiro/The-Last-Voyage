package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.gameover.GameOverScreen
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.gameOverScreen(
    navController: NavHostController,
    useCases: UseCases
) = composable<Screen.GameOver, GameOverStore>(
    navController = navController,
    store = {
        GameOverStore(
            gameSessionUseCases = useCases.gameSession,
            achievementUseCases = useCases.achievement
        )
    },
    GameOverScreen(store = viewModel { }
)
