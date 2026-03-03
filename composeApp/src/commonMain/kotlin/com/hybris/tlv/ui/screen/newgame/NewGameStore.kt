package com.hybris.tlv.ui.screen.newgame

import kotlin.concurrent.Volatile
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.domain.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.domain.usecase.ship.ShipUseCases
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MAX_CRYOPODS
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MAX_FUEL
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MAX_MATERIALS
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MAX_SENSOR_RANGE
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MIN_CRYOPODS
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MIN_FUEL
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MIN_MATERIALS
import com.hybris.tlv.domain.usecase.ship.model.Ship.Companion.MIN_SENSOR_RANGE
import com.hybris.tlv.domain.usecase.ship.model.ShipPrototype
import com.hybris.tlv.domain.usecase.space.model.Formula
import com.hybris.tlv.test.VisibleForTesting
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Store

internal class NewGameStore(
    private val shipUseCases: ShipUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<NewGameState, NewGameAction>(
    initialState = NewGameState()
) {
    @VisibleForTesting
    @Volatile
    internal var selectedShip: ShipPrototype? = null
    @VisibleForTesting
    @Volatile
    internal var selectedFormula: Formula? = null

    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val engines = shipUseCases.getEngines().toPersistentList()
        if (engines.isEmpty()) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: no engines on setup()"))
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

        updateState {
            it.copy(
                loading = false,
                shipState = shipState,
                engines = engines,
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun next(state: NewGameState) = launch(id = "start") {
        Telemetry.info(tag = TAG, message = "Start game")
        val selectedShip = this@NewGameStore.selectedShip
        if (selectedShip == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing ship prototype on startGame()"))
            return@launch
        }

        val selectedEngine = state.shipState?.engine
        if (selectedEngine == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing engine on startGame()"))
            return@launch
        }

        // TODO - allow formula selection in 'Advanced Screen'
        val selectedFormula = this@NewGameStore.selectedFormula ?: Formula()

        val gameSession = gameSessionUseCases.startGame(
            GameSessionPrototype(
                ship = selectedShip,
                engine = selectedEngine,
                formula = selectedFormula
            )
        )
        Telemetry.info(tag = TAG, message = "New session: $gameSession")
        navigate(screen = Screen.Catastrophe)
    }

    override fun reducer(state: NewGameState, action: NewGameAction) {
        when (action) {
            NewGameAction.Back -> navigate(screen = Screen.MainMenu)
            is NewGameAction.SelectEngine -> updateState { it.copy(shipState = it.shipState?.copy(engine = action.engine)) }
            is NewGameAction.SelectShip -> {
                selectedShip = action.ship
                next(state = state)
            }
        }
    }

    companion object {
        private const val TAG = "NewGameStore"
    }
}
