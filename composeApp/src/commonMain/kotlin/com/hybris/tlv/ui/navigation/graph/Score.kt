package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.score.ScoreScreen
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.scoreScreen(
    navController: NavHostController,
    useCases: UseCases
) = composable<Screen.Score, ScoreStore>(
    navController = navController,
    store = { ScoreStore(gameSessionUseCases = useCases.gameSession) },
    ScoreScreen(store = viewModel { }
)


