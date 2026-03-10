package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.help.HelpScreen
import com.hybris.tlv.ui.screen.help.HelpStore

internal fun NavGraphBuilder.helpScreen(
    config: ConfigManager,
    useCases: UseCases
) = composable<Screen.Help> {
    HelpScreen(store = viewModel {
        HelpStore(
            config = config,
            archiveUseCases = useCases.archive
        )
    })
}
