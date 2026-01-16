package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen

internal fun NavGraphBuilder.helpScreen(
    config: ConfigManager
) = composable<Screen.Help> {
    _root_ide_package_.com.hybris.tlv.ui.screen.help.HelpScreen(store = viewModel {
        _root_ide_package_.com.hybris.tlv.ui.screen.help.HelpStore(config = config)
    })
}
