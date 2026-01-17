package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.screen.stellarexplorer.StellarExplorerScreen
import com.hybris.tlv.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.stellarExplorerScreen(
    useCases: UseCases
) = composable<Screen.StellarExplorer> {
    StellarExplorerScreen(store = viewModel {
        StellarExplorerStore(spaceUseCases = useCases.space)
    })
}
