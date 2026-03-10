package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.domain.usecase.ship.model.Ship
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.typeMapOf
import com.hybris.tlv.ui.screen.StoreFactory
import com.hybris.tlv.ui.screen.event.EventScreen

internal fun NavGraphBuilder.eventScreen(storeFactory: StoreFactory) =
    composable<Screen.Event>(typeMap = typeMapOf<Ship>()) {
        val screen = it.toRoute<Screen.Event>()
        EventScreen(store = viewModel { storeFactory.getEventStore(ship = screen.ship) })
    }