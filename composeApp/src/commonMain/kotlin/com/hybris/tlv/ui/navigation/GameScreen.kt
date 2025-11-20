package com.hybris.tlv.ui.navigation

import kotlin.reflect.typeOf
import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.game.GameScreen
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.ship.model.Ship

internal fun NavGraphBuilder.gameScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<GameScreen, GameStore>(
    navController = navController,
    typeMap = mapOf(typeOf<Ship?>() to serializableType<Ship?>()),
    store = {
        GameStore(
            ship = it.ship,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            gameSessionUseCases = useCases.gameSession
        )
    },
    screen = { GameScreen(store = it) }
)

@Serializable
internal data class GameScreen(val ship: Ship? = null): Screen
