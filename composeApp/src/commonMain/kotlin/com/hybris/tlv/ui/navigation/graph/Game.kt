package com.hybris.tlv.ui.navigation.graph

import kotlin.reflect.typeOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.navigation.serializableType
import com.hybris.tlv.ui.screen.game.GameScreen
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.ship.model.Ship

internal fun NavGraphBuilder.gameScreen(
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases
) = graph<Screen.Game, GameStore>(
    navController = navController,
    typeMap = mapOf(typeOf<Ship?>() to serializableType<Ship?>()),
    store = {
        GameStore(
            ship = it.ship,
            config = config,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            gameSessionUseCases = useCases.gameSession
        )
    },
    screen = { GameScreen(store = it) }
)
