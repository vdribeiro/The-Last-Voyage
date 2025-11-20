package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.newGameScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<Screen.NewGame, NewGameStore>(
    navController = navController,
    store = {
        NewGameStore(
            shipUseCases = useCases.ship,
            catastropheUseCases = useCases.catastrophe,
            gameSessionUseCases = useCases.gameSession
        )
    },
    screen = { NewGameScreen(store = it) }
)
