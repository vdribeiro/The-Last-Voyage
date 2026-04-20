package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.domain.ship.Ship
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.typeMapOf
import com.hybris.tlv.ui.screen.StoreFactory
import com.hybris.tlv.ui.screen.game.GameScreen

internal fun NavGraphBuilder.gameScreen(storeFactory: StoreFactory) =
    composable<Screen.Game>(typeMap = typeMapOf<Ship>()) {
        val screen = it.toRoute<Screen.Game>()
        GameScreen(store = viewModel { storeFactory.getGameStore(ship = screen.ship) })
    }
