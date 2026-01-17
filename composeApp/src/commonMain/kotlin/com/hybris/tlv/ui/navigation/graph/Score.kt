package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.screen.score.ScoreScreen
import com.hybris.tlv.screen.score.ScoreStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.scoreScreen(
    useCases: UseCases
) = composable<Screen.Score> {
    ScoreScreen(store = viewModel {
        ScoreStore(gameSessionUseCases = useCases.gameSession)
    })
}


