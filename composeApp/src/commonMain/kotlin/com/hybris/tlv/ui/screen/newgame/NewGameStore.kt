package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype

internal class NewGameStore(
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

    override fun back(state: NewGameState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: NewGameState, action: NewGameAction) {
        when (action) {
            is NewGameAction.SelectShip -> updateState { it.copy(selectedShip = action.ship) }
            is NewGameAction.SelectFormula -> updateState { it.copy(formula = action.formula) }
            NewGameAction.Ship -> updateState { it.copy(currentContent = Content.SHIP) }
            NewGameAction.Advanced -> updateState { it.copy(currentContent = Content.ADVANCED) }
            NewGameAction.Start -> start()
            NewGameAction.StartGame -> startGame(state = state)
        }
    }

    private fun start() = launch {
        val catastrophe = catastropheUseCases.getRandomCatastrophe()
        if (catastrophe == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing catastrophe")
            navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.NEW_GAME,
                    message = IllegalStateException("Invalid state: missing catastrophe"),
                    tag = "NewGameStore:reducer:Start"
                )
            )
            return@launch
        }

        updateState {
            it.copy(
                currentContent = Content.START,
                selectedCatastrophe = catastrophe,
            )
        }
    }

    private fun startGame(state: NewGameState) = launch {
        if (state.selectedShip == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing ship prototype")
            navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.NEW_GAME,
                    message = IllegalStateException("Invalid state: missing ship prototype"),
                    tag = "NewGameStore:startGame"
                )
            )
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
