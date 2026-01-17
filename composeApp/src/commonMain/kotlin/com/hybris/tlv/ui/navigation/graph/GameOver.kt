package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.screen.gameover.GameOverScreen
import com.hybris.tlv.screen.gameover.GameOverStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.gameOverScreen(
    useCases: UseCases
) = composable<Screen.GameOver> {
    GameOverScreen(store = viewModel {
        GameOverStore(
            gameSessionUseCases = useCases.gameSession,
            achievementUseCases = useCases.achievement
        )
    })
}
