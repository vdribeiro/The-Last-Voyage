package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.gameover.GameOverScreen
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.gameOverScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<GameOverScreen, GameOverStore>(
    navController = navController,
    store = {
        GameOverStore(
            gameSessionUseCases = useCases.gameSession,
            achievementUseCases = useCases.achievement
        )
    },
    screen = { GameOverScreen(store = it) }
)

@Serializable
internal data object GameOverScreen: Screen
