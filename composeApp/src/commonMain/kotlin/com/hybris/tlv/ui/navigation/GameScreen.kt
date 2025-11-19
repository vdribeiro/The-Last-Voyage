package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.game.GameScreen
import com.hybris.tlv.ui.screen.game.GameStateBuilder
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.gameScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<GameScreen, GameStore>(
    navController = navController,
    store = {
        GameStore(
            stateBuilder = it.stateBuilder,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            gameSessionUseCases = useCases.gameSession
        )
    },
    screen = { GameScreen(store = it) }
)

@Serializable
internal data class GameScreen(val stateBuilder: GameStateBuilder = GameStateBuilder.Default): Screen
