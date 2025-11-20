package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.screen.gameover.GameOverScreen
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.gameOverScreen(
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases
) = graph<Screen.GameOver, GameOverStore>(
    navController = navController,
    store = {
        GameOverStore(
            config = config,
            gameSessionUseCases = useCases.gameSession,
            achievementUseCases = useCases.achievement
        )
    },
    screen = { GameOverScreen(store = it) }
)
