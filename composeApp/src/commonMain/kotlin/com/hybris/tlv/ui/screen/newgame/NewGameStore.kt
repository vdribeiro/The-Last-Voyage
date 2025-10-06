package com.hybris.tlv.ui.screen.newgame

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.AttributePoint
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula
import kotlinx.coroutines.Job

internal class NewGameStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: NewGameStateBuilder,
    private val shipUseCases: ShipUseCases,
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
        NewGameStateBuilder.FromSavableState(
            state = state,
            selectedShip = selectedShip,
        )

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val shipState = ShipState(
            totalPoints = 20,
            sensorRange = AttributePoint(max = 10, min = 1, interval = 1, initialValue = 3),
            fuel = AttributePoint(max = 1000, min = 100, interval = 100, initialValue = 700),
            materials = AttributePoint(max = 1000, min = 100, interval = 100, initialValue = 500),
            cryopods = AttributePoint(max = 1000, min = 100, interval = 100, initialValue = 300),
        )

        val engines = shipUseCases.getEngines()
        if (engines.isEmpty()) {
            error(tag = TAG, message = "Invalid state: no engines on setup()")
            return@launch
        }

        val selectedCatastrophe = catastropheUseCases.getRandomCatastrophe()
        if (selectedCatastrophe == null) {
            error(tag = TAG, message = "Invalid state: missing catastrophe on setup()")
            return@launch
        }

        updateState {
            it.copy(
                loading = false,
                shipState = shipState,
                engines = engines,
                selectedCatastrophe = selectedCatastrophe,
            )
        }
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun startGame(state: NewGameState) = launch {
        Telemetry.info(tag = TAG, message = "Start game")
        val selectedShip = this@NewGameStore.selectedShip
        if (selectedShip == null) {
            error(tag = TAG, message = "Invalid state: missing ship prototype on startGame()")
            return@launch
        }

        val selectedEngine = state.selectedEngine
        if (selectedEngine == null) {
            error(tag = TAG, message = "Invalid state: missing engine on startGame()")
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Selected ship: $selectedShip")
        gameSessionUseCases.startGame(
            GameSessionPrototype(
                ship = selectedShip,
                engine = selectedEngine,
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
            is NewGameAction.SelectEngine -> updateState { it.copy(selectedEngine = action.engine) }
            NewGameAction.Continue -> when (state.currentContent) {
                Content.SHIP -> updateState { it.copy(currentContent = Content.START) }
                Content.START -> startGame(state = state)
            }
        }
    }

    companion object {
        private const val TAG = "NewGameStore"
    }
}
