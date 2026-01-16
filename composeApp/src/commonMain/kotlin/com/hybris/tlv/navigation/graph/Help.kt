package com.hybris.tlv.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.screen.help.HelpScreen
import com.hybris.tlv.screen.help.HelpStore

internal fun NavGraphBuilder.helpScreen(
    config: ConfigManager
) = composable<Screen.Help> {
    HelpScreen(store = viewModel {
        HelpStore(config = config)
    })
}
