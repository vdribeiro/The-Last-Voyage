package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.typeMapOf
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.ship.model.Ship

internal fun NavGraphBuilder.eventScreen(
    useCases: UseCases
) = composable<Screen.Event>(
    typeMap = typeMapOf<Ship>()
) {
    val screen = it.toRoute<Screen.Event>()
    _root_ide_package_.com.hybris.tlv.ui.screen.event.EventScreen(store = viewModel {
        _root_ide_package_.com.hybris.tlv.ui.screen.event.EventStore(
            ship = screen.ship,
            eventUseCases = useCases.event,
            gameSessionUseCases = useCases.gameSession,
        )
    })
}