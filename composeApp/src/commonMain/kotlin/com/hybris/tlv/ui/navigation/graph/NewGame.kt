package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.newGameScreen(
    useCases: UseCases
) = composable<Screen.NewGame> {
    _root_ide_package_.com.hybris.tlv.ui.screen.newgame.NewGameScreen(store = viewModel {
        _root_ide_package_.com.hybris.tlv.ui.screen.newgame.NewGameStore(
            shipUseCases = useCases.ship,
            gameSessionUseCases = useCases.gameSession
        )
    })
}
