package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.newGameScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<NewGameScreen, NewGameStore>(
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

@Serializable
internal data object NewGameScreen: Screen
