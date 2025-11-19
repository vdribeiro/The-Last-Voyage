package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.score.ScoreScreen
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.scoreScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<ScoreScreen, ScoreStore>(
    navController = navController,
    store = { ScoreStore(gameSessionUseCases = useCases.gameSession) },
    screen = { ScoreScreen(store = it) }
)

@Serializable
internal data object ScoreScreen: Screen
