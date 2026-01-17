package com.hybris.tlv.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.screen.cheat.CheatScreen
import com.hybris.tlv.screen.cheat.CheatStore

internal fun NavGraphBuilder.cheatScreen(
    config: ConfigManager,
) = composable<Screen.Cheat> {
    CheatScreen(store = viewModel {
        CheatStore(config = config)
    })
}
