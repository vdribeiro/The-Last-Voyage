package com.hybris.tlv.ui.screen.newgame

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hybris.tlv.domain.usecase.ship.model.Engine
import com.hybris.tlv.domain.usecase.ship.model.ShipPrototype
import com.hybris.tlv.ui.theme.component.button.AttributePoint

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
