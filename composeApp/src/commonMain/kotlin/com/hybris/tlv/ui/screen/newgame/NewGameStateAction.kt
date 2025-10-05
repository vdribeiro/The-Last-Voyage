package com.hybris.tlv.ui.screen.newgame

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.ShipPrototype

internal sealed interface NewGameAction {
    data class SelectShip(val ship: ShipPrototype): NewGameAction
    data object Continue: NewGameAction
}

internal sealed interface NewGameStateBuilder {
    data object Default: NewGameStateBuilder
    data class FromSavableState(
        val state: NewGameState,
        val selectedShip: ShipPrototype?,
        val selectedEngine: Engine?
    ): NewGameStateBuilder
}

internal data class NewGameState(
    val loading: Boolean = true,
    val currentContent: Content = Content.SHIP,
    val shipState: ShipState? = null,
    val engines: List<Engine> = emptyList(),
    val selectedCatastrophe: Catastrophe? = null,
)

internal enum class Content {
    SHIP,
    START
}

@Stable
internal data class ShipState(
    val totalPoints: Int,
    val sensorRange: AttributePoint,
    val fuel: AttributePoint,
    val materials: AttributePoint,
    val cryopods: AttributePoint,
) {
    val assignedPoints: Int
        get() = sensorRange.assignedPoints +
                fuel.assignedPoints +
                materials.assignedPoints +
                cryopods.assignedPoints
    val remainingPoints: Int
        get() = totalPoints - assignedPoints
}

@Stable
data class AttributePoint(
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
        if ((max - min) % interval != 0) throw IllegalArgumentException("The min-max range must be a multiple of the interval.")
    }

    private var _value: Int by mutableStateOf(value = initialValue.coerceIn(minimumValue = min, maximumValue = max))
    var value: Int
        get() = _value
        set(newValue) {
            _value = newValue.coerceIn(minimumValue = min, maximumValue = max)
        }
    val assignedPoints: Int get() = (value - min) / interval

    fun increment() {
        if (value < max) value += interval
    }

    fun decrement() {
        if (value > min) value -= interval
    }
}
