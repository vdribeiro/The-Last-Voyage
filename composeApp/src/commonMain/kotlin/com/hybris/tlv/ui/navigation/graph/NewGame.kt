package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.newgame.NewGameStore

internal fun NavGraphBuilder.newGameScreen(
    useCases: UseCases
) = composable<Screen.NewGame> {
    NewGameScreen(store = viewModel {
        NewGameStore(
            shipUseCases = useCases.ship,
            gameSessionUseCases = useCases.gameSession
        )
    })
}
