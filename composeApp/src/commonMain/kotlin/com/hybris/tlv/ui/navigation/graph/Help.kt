package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.screen.help.HelpScreen
import com.hybris.tlv.ui.screen.help.HelpStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.helpScreen(
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases
) = graph<Screen.Help, HelpStore>(
    navController = navController,
    store = {
        HelpStore(
            config = config,
            learningUseCases = useCases.learning
        )
    },
    screen = { HelpScreen(store = it) }
)
