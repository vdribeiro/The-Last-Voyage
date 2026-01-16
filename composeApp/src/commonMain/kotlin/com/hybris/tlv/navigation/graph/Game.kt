package com.hybris.tlv.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.navigation.typeMapOf
import com.hybris.tlv.screen.game.GameScreen
import com.hybris.tlv.screen.game.GameStore
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.ship.model.Ship

internal fun NavGraphBuilder.gameScreen(
    config: ConfigManager,
    useCases: UseCases
) = composable<Screen.Game>(
    typeMap = typeMapOf<Ship>()
) {
    val screen = it.toRoute<Screen.Game>()
    GameScreen(store = viewModel {
        GameStore(
            ship = screen.ship,
            config = config,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            gameSessionUseCases = useCases.gameSession
        )
    })
}
