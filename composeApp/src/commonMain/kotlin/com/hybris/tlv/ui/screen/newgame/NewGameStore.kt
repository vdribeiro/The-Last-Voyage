package com.hybris.tlv.ui.screen.newgame

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
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
        is NewGameStateBuilder.FromSavableState -> stateBuilder.state
    }
) {
    @VisibleForTesting
    internal var selectedShip: ShipPrototype? = null

    init {
        when (stateBuilder) {
            NewGameStateBuilder.Default -> setup()
            is NewGameStateBuilder.FromSavableState -> selectedShip = stateBuilder.selectedShip
        }
    }

    override fun getSavableState(state: NewGameState): Any? =
        NewGameStateBuilder.FromSavableState(state = state, selectedShip = selectedShip)

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val selectedCatastrophe = catastropheUseCases.getRandomCatastrophe()
        if (selectedCatastrophe == null) {
            error(tag = TAG, message = "Invalid state: missing catastrophe on setup()")
            return@launch
        }
        val shipState = ShipState(
            engineId = "engine__liquid_fuel_rocket",
            totalPoints = 16,
            sensorRange = AttributePoint(max = 10, min = 1, interval = 1, initialValue = 4),
            fuel = AttributePoint(max = 1000, min = 100, interval = 100, initialValue = 700),
            materials = AttributePoint(max = 1000, min = 100, interval = 100, initialValue = 500),
            cryopods = AttributePoint(max = 1000, min = 100, interval = 100, initialValue = 400),
        )
        updateState {
            it.copy(
                loading = false,
                selectedCatastrophe = selectedCatastrophe,
                shipState = shipState
            )
        }
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun startGame() = launch {
        Telemetry.info(tag = TAG, message = "Start game")
        val selectedShip = this@NewGameStore.selectedShip
        if (selectedShip == null) {
            error(tag = TAG, message = "Invalid state: missing ship prototype on startGame()")
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Selected ship: $selectedShip")
        gameSessionUseCases.startGame(
            GameSessionPrototype(
                ship = selectedShip,
                formula = Formula()
            )
        )
        navigate(screen = Screen.Game)
    }

    override fun goBack(state: NewGameState) {
        navigate(screen = Screen.MainMenu)
    }

    override fun reducer(state: NewGameState, action: NewGameAction) {
        when (action) {
            is NewGameAction.SelectShip -> selectedShip = action.ship
            NewGameAction.Ship -> updateState { it.copy(currentContent = Content.SHIP) }
            NewGameAction.Start -> updateState { it.copy(currentContent = Content.START) }
            NewGameAction.StartGame -> startGame()
        }
    }

    companion object {
        private const val TAG = "NewGameStore"
    }
}
