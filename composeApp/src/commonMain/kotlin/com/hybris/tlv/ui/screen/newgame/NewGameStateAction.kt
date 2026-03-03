package com.hybris.tlv.ui.screen.newgame

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hybris.tlv.domain.usecase.ship.model.Engine
import com.hybris.tlv.domain.usecase.ship.model.ShipPrototype

internal sealed interface NewGameAction {
    data object Back: NewGameAction
    data class SelectEngine(val engine: Engine): NewGameAction
    data class SelectShip(val ship: ShipPrototype): NewGameAction
}

internal data class NewGameState(
    val loading: Boolean = true,
    val shipState: ShipState? = null,
    val engines: ImmutableList<Engine> = persistentListOf(),
)

internal data class ShipState(
    val totalPoints: Int,
    val sensorRange: AttributePoint,
    val fuel: AttributePoint,
    val materials: AttributePoint,
    val cryopods: AttributePoint,
    val engine: Engine
) {
    val assignedPoints: Int
        get() = sensorRange.assignedPoints +
                fuel.assignedPoints +
                materials.assignedPoints +
                cryopods.assignedPoints +
                engine.cost
    val remainingPoints: Int
        get() = totalPoints - assignedPoints
}

internal data class AttributePoint(
    val max: Int = 10,
    val min: Int = 0,
    val interval: Int = 1,
    val initialValue: Int = 0
) {
    init {
        if (max <= 0) throw IllegalArgumentException("max must be greater than 0")
        if (min < 0) throw IllegalArgumentException("min must be greater or equal to 0")
        if (max <= min) throw IllegalArgumentException("max must be greater than min")
        if (interval <= 0) throw IllegalArgumentException("interval must be greater than 0")
        if ((max - min) % interval != 0) throw IllegalArgumentException("The min-max range must be a multiple of the interval.")
    }

    var value: Int = initialValue.coerceIn(minimumValue = min, maximumValue = max)
        private set(newValue) {
            field = newValue.coerceIn(minimumValue = min, maximumValue = max)
        }
    val assignedPoints: Int get() = (value - min) / interval

    fun increment() {
        if (value < max) value += interval
    }

    fun decrement() {
        if (value > min) value -= interval
    }
}
