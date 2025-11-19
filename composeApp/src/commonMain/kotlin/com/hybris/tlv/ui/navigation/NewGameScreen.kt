package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.newgame.NewGameStateBuilder
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.newGameScreen(useCases: UseCases) =
    graph<NewGameScreen, NewGameStore>(
        store = {
            NewGameStore(
                stateBuilder = NewGameStateBuilder.Default,
                shipUseCases = useCases.ship,
                catastropheUseCases = useCases.catastrophe,
                gameSessionUseCases = useCases.gameSession
            )
        },
        screen = { NewGameScreen(store = it) }
    )

@Serializable
internal data object NewGameScreen: Screen
