package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.stellarExplorerScreen(
    useCases: UseCases
) = composable<Screen.StellarExplorer> {
    _root_ide_package_.com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen(store = viewModel {
        _root_ide_package_.com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore(spaceUseCases = useCases.space)
    })
}
