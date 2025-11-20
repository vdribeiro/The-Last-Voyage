package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.mainMenuScreen(
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases
) = graph<Screen.MainMenu, MainMenuStore>(
    navController = navController,
    store = {
        MainMenuStore(
            config = config,
            gameSessionUseCases = useCases.gameSession,
        )
    },
    screen = { MainMenuScreen(store = it) }
)
