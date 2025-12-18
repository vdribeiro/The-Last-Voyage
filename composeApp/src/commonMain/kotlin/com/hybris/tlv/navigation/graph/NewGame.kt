package com.hybris.tlv.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.newGameScreen(
    useCases: UseCases
) = composable<Screen.NewGame> {
    NewGameScreen(store = viewModel {
        NewGameStore(
            shipUseCases = useCases.ship,
            catastropheUseCases = useCases.catastrophe,
            gameSessionUseCases = useCases.gameSession
        )
    })
}
