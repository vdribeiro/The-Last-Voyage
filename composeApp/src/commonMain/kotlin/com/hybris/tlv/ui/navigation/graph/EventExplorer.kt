package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.ui.navigation.Screen

internal fun NavGraphBuilder.eventExplorerScreen(
    useCases: UseCases
) = composable<Screen.EventExplorer> {
//    EventExplorerScreen(store = viewModel {
//        EventExplorerStore(eventUseCases = useCases.event)
//    })
}
