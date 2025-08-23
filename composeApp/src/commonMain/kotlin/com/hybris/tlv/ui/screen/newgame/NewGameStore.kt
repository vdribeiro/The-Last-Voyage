package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.error.ErrorState
import com.hybris.tlv.ui.screen.newgame.state.ShipState
import com.hybris.tlv.ui.screen.newgame.state.ShipState.Point
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.earth.EarthUseCases
import com.hybris.tlv.usecase.earth.model.Catastrophe
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula

internal sealed interface NewGameAction {
    data object Back: NewGameAction
    data class SelectShip(val ship: ShipPrototype): NewGameAction
    data class SelectFormula(val formula: Formula): NewGameAction
    data object Ship: NewGameAction
    data object Advanced: NewGameAction
    data object Start: NewGameAction
    data object StartGame: NewGameAction
}

internal data class NewGameState(
    val currentContent: Content? = null,
    val catastrophes: List<Catastrophe> = emptyList(),
    val selectedCatastrophe: Catastrophe? = null,
    val selectedShip: ShipPrototype? = null,
    val shipState: ShipState = ShipState(
        sensorRange = Point(max = 10, min = 1, interval = 1, initialValue = 3),
        materials = Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        fuel = Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        cryopods = Point(max = 1000, min = 0, interval = 100, initialValue = 100),
    ),
    val formula: Formula = Formula(),
)

internal enum class Content {
    SHIP,
    ADVANCED,
    START
}

internal class NewGameStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: NewGameState,
    private val earthUseCases: EarthUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<NewGameAction, NewGameState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {

    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val catastrophes = earthUseCases.getCatastrophes()
        updateState {
            it.copy(
                currentContent = Content.SHIP,
                catastrophes = catastrophes,
            )
        }
    }

    private fun startGame(state: NewGameState) = launchInPipeline {
        if (state.selectedShip == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing ship prototype")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.NEW_GAME,
                    throwable = IllegalStateException("Invalid state: missing ship prototype"),
                    identifier = "NewGameStore:startGame"
                )
            )
            return@launchInPipeline
        }

        gameSessionUseCases.startGame(
            GameSessionPrototype(
                ship = state.selectedShip,
                formula = state.formula
            )
        )
        navigate(screen = Screen.GAME)
    }

    override fun reducer(state: NewGameState, action: NewGameAction) {
        when (action) {
            NewGameAction.Back -> {
                when (state.currentContent) {
                    null,
                    Content.SHIP,
                    Content.ADVANCED,
                    Content.START -> navigate(screen = Screen.MAIN_MENU)
                }
            }

            is NewGameAction.SelectShip -> updateState {
                it.copy(selectedShip = action.ship)
            }

            is NewGameAction.SelectFormula -> updateState {
                it.copy(formula = action.formula)
            }

            NewGameAction.Ship -> updateState {
                it.copy(currentContent = Content.SHIP)
            }

            NewGameAction.Advanced -> updateState {
                it.copy(currentContent = Content.ADVANCED)
            }

            NewGameAction.Start -> updateState {
                it.copy(
                    currentContent = Content.START,
                    selectedCatastrophe = state.catastrophes.random(),
                )
            }

            NewGameAction.StartGame -> startGame(state = state)
        }
    }

    companion object {
        private const val TAG = "NewGameStore"
    }
}
