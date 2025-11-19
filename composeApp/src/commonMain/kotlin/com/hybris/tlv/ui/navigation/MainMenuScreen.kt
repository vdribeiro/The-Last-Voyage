package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStateBuilder
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.mainMenuScreen(
    config: ConfigManager,
    useCases: UseCases
) {
    graph<MainMenuScreen, MainMenuState, MainMenuAction>(
        store = {
            MainMenuStore(
                stateBuilder = MainMenuStateBuilder.Default,
                config = config,
                gameSessionUseCases = useCases.gameSession,
            )
        },
        screen = { MainMenuScreen(store = it) }
    )
}

@Serializable
internal data object MainMenuScreen: Screen
