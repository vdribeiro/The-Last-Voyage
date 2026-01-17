package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.domain.usecase.UseCases

internal fun NavGraphBuilder.mainMenuScreen(
    config: ConfigManager,
    useCases: UseCases
) = composable<Screen.MainMenu> {
    MainMenuScreen(store = viewModel {
        MainMenuStore(
            config = config,
            gameSessionUseCases = useCases.gameSession,
        )
    })
}
