package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.space.model.Formula
import kotlinx.coroutines.Job

private class NewGameStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: NewGameState,
    private val catastropheUseCases: CatastropheUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<NewGameAction, NewGameState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    override fun setup(state: NewGameState): Job = launch {
        val currentContent = state.currentContent ?: Content.SHIP
        val selectedCatastrophe = state.selectedCatastrophe ?: catastropheUseCases.getRandomCatastrophe()
        if (selectedCatastrophe == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing catastrophe on setup()"))
            return@launch
        }
        val selectedShip = state.selectedShip
        val shipState = state.shipState ?: ShipState(
            sensorRange = ShipState.Point(max = 10, min = 1, interval = 1, initialValue = 3),
            materials = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
            fuel = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
            cryopods = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        )
        val formula = state.formula ?: Formula()
        updateState {
            it.copy(
                currentContent = currentContent,
                selectedCatastrophe = selectedCatastrophe,
                selectedShip = selectedShip,
                shipState = shipState,
                formula = formula
            )
        }
    }

    override fun back(state: NewGameState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: NewGameState, action: NewGameAction) {
        when (action) {
            is NewGameAction.SelectShip -> updateState { it.copy(selectedShip = action.ship) }
            is NewGameAction.SelectFormula -> updateState { it.copy(formula = action.formula) }
            NewGameAction.Ship -> updateState { it.copy(currentContent = Content.SHIP) }
            NewGameAction.Advanced -> updateState { it.copy(currentContent = Content.ADVANCED) }
            NewGameAction.Start -> updateState { it.copy(currentContent = Content.START) }
            NewGameAction.StartGame -> startGame(state = state)
        }
    }

    private fun startGame(state: NewGameState) = launch {
        if (state.selectedShip == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing ship prototype on startGame()"))
            return@launch
        }
        if (state.formula == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing formula on startGame()"))
            return@launch
        }

        gameSessionUseCases.startGame(
            GameSessionPrototype(
                ship = state.selectedShip,
                formula = state.formula
            )
        )
        navigate(screen = Screen.GAME)
    }

    companion object {
        private const val TAG = "NewGameStore"
    }
}

internal fun createNewGameStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: NewGameState,
    catastropheUseCases: CatastropheUseCases,
    gameSessionUseCases: GameSessionUseCases
): Store<NewGameAction, NewGameState> = NewGameStore(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState,
    catastropheUseCases = catastropheUseCases,
    gameSessionUseCases = gameSessionUseCases
)
