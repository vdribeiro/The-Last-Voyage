package com.hybris.tlv.ui.screen.newgame

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula

internal sealed interface NewGameAction {
    data class SelectShip(val ship: ShipPrototype): NewGameAction
    data class SelectFormula(val formula: Formula): NewGameAction
    data object Ship: NewGameAction
    data object Advanced: NewGameAction
    data object Start: NewGameAction
    data object StartGame: NewGameAction
}

internal sealed interface NewGameStateBuilder {
    data object Load: NewGameStateBuilder
    data class FromState(val state: NewGameState, val selectedShip: ShipPrototype?): NewGameStateBuilder
}

internal data class NewGameState(
    val loading: Boolean = true,
    val currentContent: Content = Content.SHIP,
    val selectedCatastrophe: Catastrophe? = null,
    val shipState: ShipState = ShipState(
        sensorRange = ShipState.Point(max = 10, min = 1, interval = 1, initialValue = 3),
        materials = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        fuel = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        cryopods = ShipState.Point(max = 1000, min = 0, interval = 100, initialValue = 100),
    ),
    val formula: Formula = Formula()
)

internal enum class Content {
    SHIP,
    ADVANCED,
    START
}

@Stable
internal data class ShipState(
    val sensorRange: Point,
    val fuel: Point,
    val materials: Point,
    val cryopods: Point,
) {
    @Stable
    data class Point(
        val max: Int,
        val min: Int,
        val interval: Int,
        val initialValue: Int
    ) {
        init {
            if (max <= 0) throw IllegalArgumentException("max must be greater than 0")
            if (min < 0) throw IllegalArgumentException("min must be greater or equal to 0")
            if (max <= min) throw IllegalArgumentException("max must be greater than min")
            if (interval <= 0) throw IllegalArgumentException("interval must be greater than 0")
        }

        private var _value: Int by mutableStateOf(value = initialValue.coerceIn(minimumValue = min, maximumValue = max))

        var value: Int
            get() = _value
            set(newValue) {
                _value = newValue.coerceIn(minimumValue = min, maximumValue = max)
            }

        val assignedPoints: Int get() = (value - min) / interval

        val maxAssignablePoints: Int get() = (max - min) / interval

        fun increment() {
            if (value < max) value += interval
        }

        fun decrement() {
            if (value > min) value -= interval
        }
    }

    val maxPoints: Int =
        sensorRange.maxAssignablePoints +
                materials.maxAssignablePoints +
                fuel.maxAssignablePoints +
                cryopods.maxAssignablePoints

    val assignedPoints: Int
        get() = sensorRange.assignedPoints +
                materials.assignedPoints +
                fuel.assignedPoints +
                cryopods.assignedPoints
    val remainingPoints get() = maxPoints - assignedPoints
}
