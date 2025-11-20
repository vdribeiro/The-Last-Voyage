package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.stellarExplorerScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<Screen.StellarExplorer, StellarExplorerStore>(
    navController = navController,
    store = { StellarExplorerStore(spaceUseCases = useCases.space) },
    screen = { StellarExplorerScreen(store = it) }
)
