package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.gameOverScreen(
    useCases: UseCases
) = composable<Screen.GameOver> {
    _root_ide_package_.com.hybris.tlv.ui.screen.gameover.GameOverScreen(store = viewModel {
        _root_ide_package_.com.hybris.tlv.ui.screen.gameover.GameOverStore(
            gameSessionUseCases = useCases.gameSession,
            achievementUseCases = useCases.achievement
        )
    })
}
