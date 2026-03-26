package com.hybris.tlv.ui.screen.newgame

import kotlin.concurrent.Volatile
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.flag.FeatureFlags.flags
import com.hybris.tlv.domain.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.domain.usecase.gamesession.model.GameSessionPrototype
import com.hybris.tlv.domain.usecase.ship.ShipUseCases
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
    internal var selectedFormula: Formula? = null
    @VisibleForTesting
    @Volatile
    internal var selectedShip: ShipPrototype? = null

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
        val selectedEngine = engines.find { it.cost == 5 } ?: engines.first()

        updateState {
            it.copy(
                loading = false,
                engines = if (flags.engines) engines else persistentListOf(),
                selectedEngine = selectedEngine
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun selectShip(state: NewGameState): Job = launch(id = "start") {
        val assignedPoints = state.sensorRange.assignedPoints +
                state.fuel.assignedPoints +
                state.materials.assignedPoints +
                state.cryopods.assignedPoints +
                (state.selectedEngine?.cost ?: 0)

        selectedShip = ShipPrototype(
            assignedPoints = assignedPoints,
            sensorRange = state.sensorRange.value,
            fuel = state.fuel.value,
            materials = state.materials.value,
            cryopods = state.cryopods.value,
        )

        Telemetry.info(tag = TAG, message = "Start game")
        val selectedShip = this@NewGameStore.selectedShip
        if (selectedShip == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing ship prototype on startGame()"))
            return@launch
        }

        val selectedEngine = state.selectedEngine
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

    private fun increment(action: NewGameAction.Increment) {
        val point = with(receiver = action.attributePoint) {
            copy(value = (value + interval).coerceAtMost(maximumValue = max))
        }
        when (action.attributePoint.type) {
            Attribute.SENSOR_RANGE -> updateState { it.copy(sensorRange = point, remainingPoints = it.remainingPoints - 1) }
            Attribute.FUEL -> updateState { it.copy(fuel = point, remainingPoints = it.remainingPoints - 1) }
            Attribute.MATERIALS -> updateState { it.copy(materials = point, remainingPoints = it.remainingPoints - 1) }
            Attribute.CRYOPODS -> updateState { it.copy(cryopods = point, remainingPoints = it.remainingPoints - 1) }
        }
    }

    private fun decrement(action: NewGameAction.Decrement) {
        val point = with(receiver = action.attributePoint) {
            copy(value = (value - interval).coerceAtLeast(minimumValue = min))
        }
        when (action.attributePoint.type) {
            Attribute.SENSOR_RANGE -> updateState { it.copy(sensorRange = point, remainingPoints = it.remainingPoints + 1) }
            Attribute.FUEL -> updateState { it.copy(fuel = point, remainingPoints = it.remainingPoints + 1) }
            Attribute.MATERIALS -> updateState { it.copy(materials = point, remainingPoints = it.remainingPoints + 1) }
            Attribute.CRYOPODS -> updateState { it.copy(cryopods = point, remainingPoints = it.remainingPoints + 1) }
        }
    }

    override fun reducer(state: NewGameState, action: NewGameAction) {
        when (action) {
            NewGameAction.Back -> navigate(screen = Screen.MainMenu)
            is NewGameAction.SelectEngine -> updateState { it.copy(selectedEngine = action.engine) }
            is NewGameAction.SelectShip -> selectShip(state = state)
            is NewGameAction.Increment -> increment(action = action)
            is NewGameAction.Decrement -> decrement(action = action)
        }
    }

    companion object {
        private const val TAG = "NewGameStore"
    }
}
