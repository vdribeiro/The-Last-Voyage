package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.cheat.CheatScreen
import com.hybris.tlv.ui.screen.cheat.CheatStore

internal fun NavGraphBuilder.cheatScreen(
    config: ConfigManager,
) = composable<Screen.Cheat> {
    CheatScreen(store = viewModel {
        CheatStore(config = config)
    })
}
