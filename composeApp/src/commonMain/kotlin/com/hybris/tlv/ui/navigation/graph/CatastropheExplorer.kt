package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.catastropheexplorer.CatastropheExplorerScreen
import com.hybris.tlv.ui.screen.catastropheexplorer.CatastropheExplorerStore

internal fun NavGraphBuilder.catastropheExplorerScreen(
    useCases: UseCases
) = composable<Screen.CatastropheExplorer> {
    CatastropheExplorerScreen(store = viewModel {
        CatastropheExplorerStore(catastropheUseCases = useCases.catastrophe)
    })
}
