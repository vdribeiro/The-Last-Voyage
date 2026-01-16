package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen

internal fun NavGraphBuilder.cheatScreen(
    config: ConfigManager,
) = composable<Screen.Cheat> {
    _root_ide_package_.com.hybris.tlv.ui.screen.cheat.CheatScreen(store = viewModel {
        _root_ide_package_.com.hybris.tlv.ui.screen.cheat.CheatStore(config = config)
    })
}
