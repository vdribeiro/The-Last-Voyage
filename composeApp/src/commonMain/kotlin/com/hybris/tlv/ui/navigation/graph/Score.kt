package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.score.ScoreScreen
import com.hybris.tlv.ui.screen.score.ScoreStore

internal fun NavGraphBuilder.scoreScreen(
    useCases: UseCases
) = composable<Screen.Score> {
    ScoreScreen(store = viewModel {
        ScoreStore(gameSessionUseCases = useCases.gameSession)
    })
}


