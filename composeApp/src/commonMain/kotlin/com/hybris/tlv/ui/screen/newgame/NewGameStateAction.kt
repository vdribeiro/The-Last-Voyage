package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.theme.component.button.AttributePoint
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.ShipPrototype

internal sealed interface NewGameAction {
    data class SelectEngine(val engine: Engine): NewGameAction
    data class SelectShip(val ship: ShipPrototype): NewGameAction
}

internal data class NewGameState(
    val loading: Boolean = true,
    val shipState: ShipState? = null,
    val engines: List<Engine> = emptyList(),
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
