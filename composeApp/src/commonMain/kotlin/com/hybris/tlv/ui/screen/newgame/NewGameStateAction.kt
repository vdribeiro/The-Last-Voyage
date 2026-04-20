package com.hybris.tlv.ui.screen.newgame

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hybris.tlv.domain.ship.Engine
import com.hybris.tlv.domain.ship.Ship.Companion.MAX_CRYOPODS
import com.hybris.tlv.domain.ship.Ship.Companion.MAX_FUEL
import com.hybris.tlv.domain.ship.Ship.Companion.MAX_MATERIALS
import com.hybris.tlv.domain.ship.Ship.Companion.MAX_SENSOR_RANGE
import com.hybris.tlv.domain.ship.Ship.Companion.MIN_CRYOPODS
import com.hybris.tlv.domain.ship.Ship.Companion.MIN_FUEL
import com.hybris.tlv.domain.ship.Ship.Companion.MIN_MATERIALS
import com.hybris.tlv.domain.ship.Ship.Companion.MIN_SENSOR_RANGE

internal sealed interface NewGameAction {
    data object Back: NewGameAction
    data class SelectEngine(val engine: Engine): NewGameAction
    data object SelectShip: NewGameAction
    data class Increment(val attributePoint: AttributePoint): NewGameAction
    data class Decrement(val attributePoint: AttributePoint): NewGameAction
}

internal data class NewGameState(
    val loading: Boolean = true,
    val sensorRange: AttributePoint = AttributePoint(type = Attribute.SENSOR_RANGE, max = MAX_SENSOR_RANGE, min = MIN_SENSOR_RANGE, interval = 1, value = 4),
    val fuel: AttributePoint = AttributePoint(type = Attribute.FUEL, max = MAX_FUEL, min = MIN_FUEL, interval = 100, value = 1000),
    val materials: AttributePoint = AttributePoint(type = Attribute.MATERIALS, max = MAX_MATERIALS, min = MIN_MATERIALS, interval = 100, value = 500),
    val cryopods: AttributePoint = AttributePoint(type = Attribute.CRYOPODS, max = MAX_CRYOPODS, min = MIN_CRYOPODS, interval = 100, value = 500),
    val remainingPoints: Int = 0,
    val engines: ImmutableList<Engine> = persistentListOf(),
    val selectedEngine: Engine? = null,
)

internal enum class Attribute {
    SENSOR_RANGE,
    FUEL,
    MATERIALS,
    CRYOPODS
}

internal data class AttributePoint(
    val type: Attribute,
    val max: Int = 10,
    val min: Int = 0,
    val interval: Int = 1,
    val value: Int = 0
) {
    init {
        if (max <= 0) throw IllegalArgumentException("max must be greater than 0")
        if (min < 0) throw IllegalArgumentException("min must be greater or equal to 0")
        if (max <= min) throw IllegalArgumentException("max must be greater than min")
        if (interval <= 0) throw IllegalArgumentException("interval must be greater than 0")
        if ((max - min) % interval != 0) throw IllegalArgumentException("The min-max range must be a multiple of the interval.")
    }

    val assignedPoints: Int get() = (value - min) / interval
}
