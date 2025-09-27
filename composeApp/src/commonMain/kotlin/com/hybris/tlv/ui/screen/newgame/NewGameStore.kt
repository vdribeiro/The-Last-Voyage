package com.hybris.tlv.ui.screen.newgame

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import kotlinx.coroutines.Job

internal class NewGameStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: NewGameStateBuilder,
    private val catastropheUseCases: CatastropheUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<NewGameState, NewGameAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        NewGameStateBuilder.Default -> NewGameState()
        is NewGameStateBuilder.FromState -> stateBuilder.state
    }
) {
    @VisibleForTesting
    internal var selectedShip: ShipPrototype? = null

    init {
        when (stateBuilder) {
            NewGameStateBuilder.Default -> setup()
            is NewGameStateBuilder.FromState -> selectedShip = stateBuilder.selectedShip
        }
    }

    private fun setup(): Job = launch {
        val selectedCatastrophe = catastropheUseCases.getRandomCatastrophe()
        if (selectedCatastrophe == null) {
            navigate(screen = Screen.Feedback, stateBuilder = FeedbackStateBuilder.Error(tag = TAG, message = "Invalid state: missing catastrophe on setup()"))
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
        val selectedShip = this@NewGameStore.selectedShip
        if (selectedShip == null) {
            navigate(screen = Screen.Feedback, stateBuilder = FeedbackStateBuilder.Error(tag = TAG, message = "Invalid state: missing ship prototype on startGame()"))
            return@launch
        }

        gameSessionUseCases.startGame(
            GameSessionPrototype(
                ship = selectedShip,
                formula = state.formula
            )
        )
        navigate(screen = Screen.Game)
    }

    override fun back(state: NewGameState): () -> Unit = {
        navigate(screen = Screen.MainMenu)
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

    override fun getStateBuilderForFeedback(): Any = NewGameStateBuilder.FromState(state = stateFlow.value, selectedShip = selectedShip)

    companion object {
        private const val TAG = "NewGameStore"
    }
}
