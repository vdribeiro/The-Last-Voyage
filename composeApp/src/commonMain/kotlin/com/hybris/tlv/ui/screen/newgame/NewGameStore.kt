package com.hybris.tlv.ui.screen.newgame

import kotlinx.coroutines.Job
import androidx.annotation.VisibleForTesting
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Game
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.button.AttributePoint
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_CRYOPODS
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_FUEL
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_MATERIALS
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_SENSOR_RANGE
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MIN_CRYOPODS
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MIN_FUEL
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MIN_MATERIALS
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MIN_SENSOR_RANGE
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula

internal class NewGameStore(
    config: ConfigManager,
    private val shipUseCases: ShipUseCases,
    private val catastropheUseCases: CatastropheUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<NewGameState, NewGameAction>(
    config = config,
    initialState = NewGameState()
) {
    @VisibleForTesting
    internal var selectedShip: ShipPrototype? = null

    init {
        setup()
    }

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")

        val engines = shipUseCases.getEngines()
        if (engines.isEmpty()) {
            feedback(tag = TAG, message = "Invalid state: no engines on setup()")
            return@launch
        }
        val shipState = ShipState(
            totalPoints = 25,
            sensorRange = AttributePoint(max = MAX_SENSOR_RANGE, min = MIN_SENSOR_RANGE, interval = 1, initialValue = 4),
            fuel = AttributePoint(max = MAX_FUEL, min = MIN_FUEL, interval = 100, initialValue = 1000),
            materials = AttributePoint(max = MAX_MATERIALS, min = MIN_MATERIALS, interval = 100, initialValue = 500),
            cryopods = AttributePoint(max = MAX_CRYOPODS, min = MIN_CRYOPODS, interval = 100, initialValue = 500),
            engine = engines.find { it.cost == 5 } ?: engines.first()
        )

        val selectedCatastrophe = catastropheUseCases.getRandomCatastrophe()
        if (selectedCatastrophe == null) {
            feedback(tag = TAG, message = "Invalid state: missing catastrophe on setup()")
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

    private fun next(state: NewGameState): Job = launch {
        when (state.currentContent) {
            Content.SHIP -> updateState { it.copy(currentContent = Content.START) }
            Content.START -> {
                Telemetry.info(tag = TAG, message = "Start game")
                val selectedShip = this@NewGameStore.selectedShip
                if (selectedShip == null) {
                    feedback(tag = TAG, message = "Invalid state: missing ship prototype on startGame()")
                    return@launch
                }

                val selectedEngine = state.shipState?.engine
                if (selectedEngine == null) {
                    feedback(tag = TAG, message = "Invalid state: missing engine on startGame()")
                    return@launch
                }

                val gameSession = gameSessionUseCases.startGame(
                    GameSessionPrototype(
                        ship = selectedShip,
                        engine = selectedEngine,
                        formula = Formula()
                    )
                )
                Telemetry.info(tag = TAG, message = "New session: $gameSession")
                navigate(screen = Game())
            }
        }
    }

    override fun back(state: NewGameState) {
        navigate(screen = Screen.MainMenu)
    }

    override fun reducer(state: NewGameState, action: NewGameAction) {
        when (action) {
            is NewGameAction.SelectShip -> {
                selectedShip = action.ship
                next(state = state)
            }

            is NewGameAction.SelectEngine -> updateState { it.copy(shipState = it.shipState?.copy(engine = action.engine)) }
            NewGameAction.Next -> next(state = state)
        }
    }

    companion object {
        private const val TAG = "NewGameStore"
    }
}
