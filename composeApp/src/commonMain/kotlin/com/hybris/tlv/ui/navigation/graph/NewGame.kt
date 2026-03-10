package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.StoreFactory
import com.hybris.tlv.ui.screen.newgame.NewGameScreen

internal fun NavGraphBuilder.newGameScreen(storeFactory: StoreFactory) =
    composable<Screen.NewGame> {
        NewGameScreen(store = viewModel { storeFactory.getNewGameStore() })
    }
