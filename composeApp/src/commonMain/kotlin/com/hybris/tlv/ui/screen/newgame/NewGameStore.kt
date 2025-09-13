package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula
import kotlinx.coroutines.Job

internal class NewGameStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    private val catastropheUseCases: CatastropheUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<NewGameAction, NewGameState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = NewGameState(
        loading = true,
        currentContent = Content.SHIP,
        selectedCatastrophe = null,
        shipState = ShipState(
            sensorRange = ShipState.Point(max = 10, min = 1, interval = 1, initialValue = 3),
            materials = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
            fuel = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
            cryopods = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        ),
        formula = Formula()
    )
) {
    private var selectedShip: ShipPrototype? = null

    init {
        setup()
    }

    private fun setup(): Job = launch {
        val selectedCatastrophe = catastropheUseCases.getRandomCatastrophe()
        if (selectedCatastrophe == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackStateBuilder(tag = TAG, message = "Invalid state: missing catastrophe on setup()"))
            return@launch
        }
        updateState {
            it.copy(
                loading = false,
                selectedCatastrophe = selectedCatastrophe
            )
        }
    }

    private fun startGame(state: NewGameState) = launch {
        val selectedShip = selectedShip
        if (selectedShip == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackStateBuilder(tag = TAG, message = "Invalid state: missing ship prototype on startGame()"))
            return@launch
        }

        gameSessionUseCases.startGame(
            GameSessionPrototype(
                ship = selectedShip,
                formula = state.formula
            )
        )
        navigate(screen = Screen.GAME)
    }

    override fun back(state: NewGameState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: NewGameState, action: NewGameAction) {
        when (action) {
            is NewGameAction.SelectShip -> selectedShip = action.ship
            is NewGameAction.SelectFormula -> updateState { it.copy(formula = action.formula) }
            NewGameAction.Ship -> updateState { it.copy(currentContent = Content.SHIP) }
            NewGameAction.Advanced -> updateState { it.copy(currentContent = Content.ADVANCED) }
            NewGameAction.Start -> updateState { it.copy(currentContent = Content.START) }
            NewGameAction.StartGame -> startGame(state = state)
        }
    }

    companion object {
        private const val TAG = "NewGameStore"
    }
}
