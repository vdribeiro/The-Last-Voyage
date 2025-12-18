package com.hybris.tlv.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.screen.help.HelpScreen
import com.hybris.tlv.screen.help.HelpStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.helpScreen(
    config: ConfigManager,
    useCases: UseCases
) = composable<Screen.Help> {
    HelpScreen(store = viewModel {
        HelpStore(
            config = config,
            syncUseCases = useCases.sync
        )
    })
}
